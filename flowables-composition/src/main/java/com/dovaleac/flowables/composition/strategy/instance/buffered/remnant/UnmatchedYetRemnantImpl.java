package com.dovaleac.flowables.composition.strategy.instance.buffered.remnant;

import com.dovaleac.flowables.composition.strategy.instance.buffered.buffer.ReadBufferImpl;
import com.dovaleac.flowables.composition.strategy.instance.buffered.buffer.WriteBuffer;
import com.dovaleac.flowables.composition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsTrigger;
import com.dovaleac.flowables.composition.strategy.instance.buffered.capacity.NextAction;
import com.dovaleac.flowables.composition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
import com.dovaleac.flowables.composition.strategy.instance.buffered.exceptions.WriteBufferFrozenException;
import com.dovaleac.flowables.composition.strategy.instance.buffered.exceptions.WriteBufferFullException;
import com.dovaleac.flowables.composition.strategy.instance.buffered.guarder.SubscriberStatusGuarder;
import com.dovaleac.flowables.composition.tuples.InnerJoinTuple;
import com.dovaleac.flowables.composition.tuples.OnlyLeftTuple;
import com.dovaleac.flowables.composition.tuples.OnlyRightTuple;
import com.dovaleac.flowables.composition.tuples.OptionalTuple;
import com.github.oxo42.stateless4j.StateMachine;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.functions.Function;

import java.util.List;
import java.util.Map;

public class UnmatchedYetRemnantImpl<T, OT, KT, LT, RT>
    implements UnmatchedYetRemnant<UnmatchedYetRemnantImpl<OT, T, KT, LT, RT>, T, OT, KT, LT, RT> {

  private final StateMachine<UnmatchedYetRemnantState, UnmatchedYetRemnantTrigger> stateMachine =
      new StateMachine<>(
          UnmatchedYetRemnantState.IDLE, new UnmatchedYetRemnantStateMachine(this).getConfig());
  private final Map<KT, T> map;
  private final UnmatchedYetRemnantConfig config;
  private final ReadBufferImpl<OT, KT> readBuffer;
  private final boolean isLeft;
  private final FlowableEmitter<OptionalTuple<LT, RT>> emitter;
  private SubscriberStatusGuarder<T> guarder;

  private final WriteBuffer<T, OT, KT, LT, RT> writeBuffer;

  private final Object dontPollMoreReadsWhileAcceptingSyncLock = new Object();

  private UnmatchedYetRemnantImpl<OT, T, KT, LT, RT> otherRemnant;

  public UnmatchedYetRemnantImpl(
      Map<KT, T> initialMap,
      UnmatchedYetRemnantConfig config,
      Function<OT, KT> function,
      int maxBlocksForReadBuffer,
      boolean isLeft,
      FlowableEmitter<OptionalTuple<LT, RT>> emitter) {
    this.map = initialMap;
    this.config = config;
    this.isLeft = isLeft;
    this.emitter = emitter;
    this.readBuffer = new ReadBufferImpl<>(function, maxBlocksForReadBuffer);
    writeBuffer =
        new WriteBuffer<>(
            this, config.getMaxElementsInWriteBuffer(), config.getInitialMapForWriteBuffer());
  }

  UnmatchedYetRemnantImpl() {
    this(null, new UnmatchedYetRemnantConfig(), null, 1, false, null);
  }
  // OVERRIDEN METHODS

  @Override
  public void setOther(UnmatchedYetRemnantImpl<OT, T, KT, LT, RT> other) {
    this.otherRemnant = other;
  }

  @Override
  public void setGuarder(SubscriberStatusGuarder<T> guarder) {
    this.guarder = guarder;
  }

  @Override
  public Completable addToReadBuffer(List<OT> otherTypeElements) {
    stateMachine.fire(UnmatchedYetRemnantTrigger.PROCESS_READ);
    return readBuffer.push(otherTypeElements)
        ? Completable.complete()
        : Completable.error(new ReadBufferNotAvailableForNewElementsException());
  }

  /**
   * Recursive method for consuming the read buffer. As elements in the read buffer are arranged in
   * blocks, each poll gets a block. If the read buffer is empty, the query to the ReadBuffer will
   * return an empty Maybe, and so, the trigger `IT_WOULD_BE_BETTER_TO_WRITE` would be fired.
   *
   * <p>The results obtained, which come as a map, are then checked against the inner map, so that
   * if they match any of the elements in the map, their inner join gets emitted and the element in
   * this map gets removed from it. Those that don't match with any other element are packed as a
   * Map and sent to the other remnant for being written in its inner map.
   *
   * <p>About synchronizing this method with other processes: when a poll is over, first we check in
   * which state we are. There's a state dedicated to the moment in which there's a poll going on
   * and a sync request is received. That would let the poll go on, but it would change the state to
   * `WAITING_FOR_SYNCHRONIZEE`. When the poll finishes (that is, the elements get shipped to the
   * other remnant), if the state is the aforementioned one, then two things will happen: we don't
   * want to keep polling but instead we want to move to the `SYNCHRONIZEE` state, but first of all
   * we need to deal with elements that are going to get rejected in the other remnant's write
   * buffer.
   *
   * <p>When a remnant requires a sync, given this algorithm, if the other remnant is reading (that
   * is, generating new elements to go to the first remnant's write buffer), only one block of
   * elements will be requested to be added in the write buffer. They will be rejected because the
   * first remnant is already in the `WAITING_FOR_SYNCHRONIZER` state, so we have to think of what
   * to do with them. The thing is that these elements haven't found their match against the other's
   * remnant current inner map, but after the sync, new elements will be added to the inner map;
   * elements against whom the block hasn't tried to match. So, *before* the sync we need to add
   * these elements to the write buffer, so they're taken into account for the sync. There's no need
   * to match them before, because they will be matched as part of the sync. As the write buffer
   * would reject them because it's frozen, we need to add them forcibly
   *
   * <p>When this is done, we can transit to the `SYNCHRONIZEE` state.
   *
   * <p>In case nothing of this happens, we want to call pollRead() again, but increasing the acc.
   * What is that number for? For keeping account that every x tries, we want to check the
   * capacities of our buffers and, in case it's deemed necessary, try to change to a sync.
   *
   * @param acc the accumulator
   */
  // synchronize with accepting a sync request and sending one
  protected void pollRead(int acc) {
    synchronized (dontPollMoreReadsWhileAcceptingSyncLock) {
      readBuffer
          .pull()
          .doOnComplete(this::itWouldBeBetterToWrite)
          .map(Map::entrySet)
          .flatMapPublisher(Flowable::fromIterable)
          .filter(key -> tryToEmit(key.getKey(), key.getValue(), map))
          .toMap(Map.Entry::getKey, Map.Entry::getValue)
          .subscribe(
              ownTypeElements -> {
                otherRemnant
                    .addToWriteBuffer(ownTypeElements)
                    .subscribe(
                        () -> {
                          if (stateMachine.isInState(
                              UnmatchedYetRemnantState.WAITING_FOR_SYNCHRONIZEE)) {
                            // TODO: check the ownTypeElements against the writeBuffer!!
                            otherRemnant
                                .addForciblyToWriteBuffer(ownTypeElements)
                                .subscribe(
                                    () ->
                                        stateMachine.fire(
                                            UnmatchedYetRemnantTrigger
                                                .LAST_POLL_BEFORE_BEING_SYNCHRONIZED_IS_OVER));
                            return;
                          }
                          if (stateMachine.getState().consumesReadingBuffer()) {
                            return;
                          }
                          if (acc < config.getPollReadsForCheckCapacity()) {
                            pollRead(acc + 1);
                            return;
                          }
                          if (checkCapacity() == NextAction.READ) {
                            pollRead(0);
                          } else {
                            stateMachine.fire(
                                UnmatchedYetRemnantTrigger.IT_WOULD_BE_BETTER_TO_WRITE);
                          }
                        },
                        throwable -> System.out.println(throwable.getMessage()));
              });
    }
  }

  protected void consumeWriteBuffer() {
    map.putAll(writeBuffer.getAllElements());
    writeBuffer.clear();
    stateMachine.fire(UnmatchedYetRemnantTrigger.WRITE_BUFFER_DEPLETED);
  }

  private NextAction checkCapacity() {
    return config
        .getCheckCapacityStrategy()
        .getNextAction(readBuffer.getCapacity(), writeBuffer.getCapacity());
  }

  protected boolean tryToEmit(KT key, OT ot, Map<KT, T> map) {
    T oneT = map.get(key);
    if (oneT == null) {
      return true;
    } else {
      emitInnerJoin(ot, oneT);
      map.remove(oneT);
      return false;
    }
  }

  private void emitInnerJoin(OT ot, T oneT) {
    if (isLeft) {
      emitter.onNext(InnerJoinTuple.of((LT) oneT, (RT) ot));
    } else {
      emitter.onNext(InnerJoinTuple.of((LT) ot, (RT) oneT));
    }
  }

  private void emitSoleTuple(T oneT) {
    if (isLeft) {
      emitter.onNext(OnlyLeftTuple.of((LT) oneT));
    } else {
      emitter.onNext(OnlyRightTuple.of((RT) oneT));
    }
  }

  @Override
  public Completable addToWriteBuffer(Map<KT, T> ownTypeElements) {
    return Completable.create(
        completableEmitter -> {
          try {
            writeBuffer.addToQueue(ownTypeElements);
          } catch (WriteBufferFrozenException ex) {
            completableEmitter.onError(ex);
          } catch (WriteBufferFullException ex) {
            itWouldBeBetterToWrite();
          }
        });
  }

  public Completable addForciblyToWriteBuffer(Map<KT, T> ownTypeElements) {
    return Completable.create(
        completableEmitter -> writeBuffer.addForciblyToQueue(ownTypeElements));
  }

  @Override
  public Completable emitAllElements() {
    return Completable.fromAction(
        () -> Flowable.fromIterable(map.values()).forEach(this::emitSoleTuple));
  }

  // METHODS FOR TRANSITIONS

  void disableWriteBufferForFill() {
    writeBuffer.fire(WriteBufferAcceptNewInputsTrigger.FREEZE);
  }

  void enableConsumingReadingBuffer() {
    pollRead(0);
  }

  void enableConsumingWritingBuffer() {
    consumeWriteBuffer();
  }

  void requestSync() {
    otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_REQUESTED);
  }

  void rejectSync() {
    otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_REJECTED);
  }

  void acceptSync() {
    synchronized (dontPollMoreReadsWhileAcceptingSyncLock) {
      otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_ACCEPTED);
    }
  }

  Completable synchronize() {
    return Completable.fromAction(
        () -> {
          Map<KT, OT> otherRemnanoneTs = otherRemnant.writeBuffer.getAllElements();
          Map<KT, T> ownRemnanoneTs = writeBuffer.getAllElements();
          otherRemnanoneTs.entrySet().stream()
              .filter(
                  ktotEntry ->
                      tryToEmit(ktotEntry.getKey(), ktotEntry.getValue(), ownRemnanoneTs))
              .forEach(entry -> otherRemnant.map.put(entry.getKey(), entry.getValue()));
          map.putAll(ownRemnanoneTs);

          otherRemnanoneTs.clear();
          ownRemnanoneTs.clear();
        });
  }

  void syncFinished() {
    otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_FINISHED);
  }

  void notifyWriteIsSafe() {
    otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.WRITE_IS_SAFE_NOW);
  }

  public void itWouldBeBetterToWrite() {
    stateMachine.fire(UnmatchedYetRemnantTrigger.IT_WOULD_BE_BETTER_TO_WRITE);
  }

  public boolean isWriteBufferFrozen() {
    return !stateMachine.getState().allowsFillingWritingBuffer();
  }
}
