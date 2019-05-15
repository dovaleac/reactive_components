package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.ReadBufferImpl;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsTrigger;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferManager;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.WriteBufferFrozenException;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.WriteBufferFullException;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarder;
import com.dovaleac.flowablesComposition.tuples.InnerJoinTuple;
import com.dovaleac.flowablesComposition.tuples.OnlyLeftTuple;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import com.github.oxo42.stateless4j.StateMachine;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.functions.Function;

import java.util.List;
import java.util.Map;

public class UnmatchedYetRemnantImpl<T, OT, KT, LT, RT> implements UnmatchedYetRemnant<
    UnmatchedYetRemnantImpl<T, OT, KT, LT, RT>, T, OT, KT, LT, RT> {

  private final StateMachine<UnmatchedYetRemnantState, UnmatchedYetRemnantTrigger> stateMachine =
      new StateMachine<>(
          UnmatchedYetRemnantState.IDLE,
          new UnmatchedYetRemnantStateMachine(this).getConfig()
      );
  private final Map<KT, T> map;
  private final UnmatchedYetRemnantConfig config;
  private final ReadBufferImpl<OT, KT> readBuffer;
  private final boolean isLeft;
  private final FlowableEmitter<OptionalTuple<LT, RT>> emitter;
  private final SubscriberStatusGuarder<T, KT> guarder;

  private final WriteBufferManager<T, OT, KT, LT, RT> writeBuffer;

  private final Object dontPollMoreReadsWhileAcceptingSyncLock = new Object();

  private UnmatchedYetRemnantImpl<OT, T, KT, LT, RT> otherRemnant;

  public UnmatchedYetRemnantImpl(
      Map<KT, T> initialMap,
      UnmatchedYetRemnantConfig config,
      Function<OT, KT> function,
      int maxBlocksForReadBuffer,
      boolean isLeft,
      FlowableEmitter<OptionalTuple<LT, RT>> emitter,
      SubscriberStatusGuarder<T, KT> guarder) {
    this.map = initialMap;
    this.config = config;
    this.isLeft = isLeft;
    this.emitter = emitter;
    this.readBuffer = new ReadBufferImpl<>(function, maxBlocksForReadBuffer);
    writeBuffer = new WriteBufferManager<>(this,
        config.getMaxElementsInWriteBuffer(), config.getInitialMapForWriteBuffer());
    this.guarder = guarder;
  }

  //OVERRIDEN METHODS

  @Override
  public void setOther(UnmatchedYetRemnantImpl other) {
    this.otherRemnant = other;
  }

  @Override
  public Completable addToReadBuffer(List<OT> otherTypeElements) {
    stateMachine.fire(UnmatchedYetRemnantTrigger.PROCESS_READ);
    return readBuffer.push(otherTypeElements)
        ? Completable.complete()
        : Completable.error(new ReadBufferNotAvailableForNewElementsException());
  }

  //synchronize with accepting a sync request and sending one
  protected void pollRead(int acc) {
    synchronized (dontPollMoreReadsWhileAcceptingSyncLock) {
      readBuffer.pull()
          .doOnComplete(() -> stateMachine.fire(UnmatchedYetRemnantTrigger.IT_WOULD_BE_BETTER_TO_WRITE))
          .map(Map::entrySet)
          .flatMapPublisher(Flowable::fromIterable)
          .filter(key -> tryToEmit(key.getKey(), key.getValue()))
          .toMap(Map.Entry::getKey, Map.Entry::getValue)
          .subscribe(
              ownTypeElements -> {
                otherRemnant.addToWriteBuffer(ownTypeElements).subscribe(() -> {
                  if (acc >= config.getPollReadsForCheckCapacity()) {
                    /*
                    * Probably this next line is not enough for preventing problems before being
                    * synchronizee, because the state hasn't changed, as the previous acceptSync
                    * method is stopped due to the concurrency lock.
                    *
                    * Probably the solution for this is including a new state
                    * FinishingLastReadPoll, which will allow to include a previous if (state ==
                    * that) { fire change to SYNC_ACCEPTED; return; }
                    * */
                    if (checkCapacity() && stateMachine.getState().consumesReadingBuffer()) {
                      pollRead(acc + 1);
                    } else {
                      stateMachine.fire(UnmatchedYetRemnantTrigger.IT_WOULD_BE_BETTER_TO_WRITE);
                    }
                  }
                });

              }
          );
    }
  }

  protected void consumeWriteBuffer() {
    map.putAll(writeBuffer.getAllElements());
    writeBuffer.clear();
    stateMachine.fire(UnmatchedYetRemnantTrigger.WRITE_BUFFER_DEPLETED);
  }

  private boolean checkCapacity() {
    //use strategy
  }

  protected boolean tryToEmit(KT key, OT ot) {
    T t = map.get(key);
    if (t == null){
      return true;
    } else {
      if (isLeft) {
        emitter.onNext(InnerJoinTuple.of((LT) t, (RT) ot));
      } else {
        emitter.onNext(InnerJoinTuple.of((LT) ot, (RT) t));
      }
      return false;
    }
  }
  @Override
  public Completable addToWriteBuffer(Map<KT, T> ownTypeElements) {
    return Completable.create(completableEmitter -> {
      try {
        writeBuffer.addToQueue(ownTypeElements);
      } catch (WriteBufferFrozenException e) {
        guarder.stopWriting(ownTypeElements);
        completableEmitter.onError(e);
      } catch (WriteBufferFullException e) {
        guarder.stopWriting(ownTypeElements);
        itWouldBeBetterToWrite();
      }
    });
  }


  @Override
  public Completable emitAllElements(FlowableEmitter<OptionalTuple<LT, RT>> emitter) {
    return Completable.fromAction(() -> Flowable.fromIterable(map.keySet())
        .forEach(t -> emitter.onNext(new OnlyLeftTuple<>((LT) t))));
  }

//METHODS FOR TRANSITIONS

  void disableWriteBufferForFill() {
    writeBuffer.fire(WriteBufferAcceptNewInputsTrigger.FREEZE);
  }

  void enableConsumingReadingBuffer() {
    disableConsumingWritingBuffer();
    pollRead(0);
  }

  void disableConsumingReadingBuffer() {
    //looks like it's unnecessary because its action is done by other methods
  }

  void enableConsumingWritingBuffer() {
    disableConsumingReadingBuffer();
  }

  void disableConsumingWritingBuffer() {
    //looks like it's unnecessary because when the write buffer it goes back to a state in which
    // it can't be consumed
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

  void synchronize() {

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
