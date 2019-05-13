package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.ReadBufferImpl;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsState;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsTrigger;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferManager;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
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

  private UnmatchedYetRemnantImpl<OT, T, KT, LT, RT> otherRemnant;

  private WriteBufferManager primaryWriteBuffer = new WriteBufferManager(this,
      WriteBufferAcceptNewInputsState.ACCEPT_NEW);
  private WriteBufferManager secondaryWriteBuffer = new WriteBufferManager(this,
      WriteBufferAcceptNewInputsState.DISABLED);

  private WriteBufferManager writeBufferInUse = primaryWriteBuffer;

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

  protected void pollRead(int acc) {
    readBuffer.pull()
        .doOnComplete(() -> stateMachine.fire(UnmatchedYetRemnantTrigger.IT_WOULD_BE_BETTER_TO_WRITE))
        .map(Map::entrySet)
        .flatMapPublisher(Flowable::fromIterable)
        .filter(key -> tryToEmit(key.getKey(), key.getValue()))
        .toMap(Map.Entry::getKey, Map.Entry::getValue)
        .subscribe(
            ownTypeElements -> {
              otherRemnant.addToWriteBuffer(ownTypeElements);
              if (acc >= config.getPollReadsForCheckCapacity()) {
                if (checkCapacity()){
                  pollRead(acc + 1);
                } else {
                  stateMachine.fire(UnmatchedYetRemnantTrigger.IT_WOULD_BE_BETTER_TO_WRITE);
                }
              }
            }
        );
  }

  private boolean checkCapacity() {
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
    //use writeBufferInUse
    return null;
  }


  @Override
  public Completable emitAllElements(FlowableEmitter<OptionalTuple<LT, RT>> emitter) {
    return Completable.fromAction(() -> Flowable.fromIterable(map.keySet())
        .forEach(t -> emitter.onNext(new OnlyLeftTuple<>((LT) t))));
  }

//METHODS FOR TRANSITIONS

  void disableWriteBufferForFill() {
    primaryWriteBuffer.fire(WriteBufferAcceptNewInputsTrigger.FREEZE);
  }

  void enableConsumingReadingBuffer() {
    disableConsumingWritingBuffer();
    pollRead(0);
  }

  void disableConsumingReadingBuffer() {

  }

  void enableConsumingWritingBuffer() {
    disableConsumingReadingBuffer();
  }

  void disableConsumingWritingBuffer() {

  }

  void requestSync() {
    otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_REQUESTED);
  }

  void rejectSync() {
    otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_REJECTED);
  }

  void acceptSync() {
    otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_ACCEPTED);
  }

  void synchronize() {

  }

  void syncFinished() {
    otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_FINISHED);
  }

  void notifyWriteIsSafe() {
    otherRemnant.stateMachine.fire(UnmatchedYetRemnantTrigger.WRITE_IS_SAFE_NOW);
  }

  void useSecondaryWriteBuffer() {
    secondaryWriteBuffer.fire(WriteBufferAcceptNewInputsTrigger.ENABLE_FOR_USE);
  }

  void promoteSecondaryWriteBuffer() {
    writeBufferInUse = secondaryWriteBuffer;
    primaryWriteBuffer = secondaryWriteBuffer;
    secondaryWriteBuffer = new WriteBufferManager(this,
        WriteBufferAcceptNewInputsState.DISABLED);
  }

  void itWouldBeBetterToWrite() {
    stateMachine.fire(UnmatchedYetRemnantTrigger.IT_WOULD_BE_BETTER_TO_WRITE);
  }
}
