package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.ReadBufferImpl;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsState;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsTrigger;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferManager;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
import com.dovaleac.flowablesComposition.tuples.OnlyLeftTuple;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import com.github.oxo42.stateless4j.StateMachine;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
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
  private final Map<T, KT> map;
  private final UnmatchedYetRemnantConfig config;

  private UnmatchedYetRemnantImpl<OT, T, KT, LT, RT> otherRemnant;

  private final ReadBufferImpl<OT, KT> readBuffer;
  private WriteBufferManager primaryWriteBuffer = new WriteBufferManager(this,
      WriteBufferAcceptNewInputsState.ACCEPT_NEW);
  private WriteBufferManager secondaryWriteBuffer = new WriteBufferManager(this,
      WriteBufferAcceptNewInputsState.DISABLED);

  private WriteBufferManager writeBufferInUse = primaryWriteBuffer;

  public UnmatchedYetRemnantImpl(
      Map<T, KT> initialMap, UnmatchedYetRemnantConfig config,
      Function<OT, KT> function, int maxBlocks) {
    this.map = initialMap;
    this.config = config;
    this.readBuffer = new ReadBufferImpl<>(function, maxBlocks);
  }

  //OVERRIDEN METHODS

  @Override
  public void setOther(UnmatchedYetRemnantImpl other) {
    this.otherRemnant = other;
  }

  @Override
  public Completable addToReadBuffer(List<OT> otherTypeElements) {
    return Completable.create(singleEmitter -> {
      stateMachine.fire(UnmatchedYetRemnantTrigger.PROCESS_READ);
      try {
        readBuffer.push(otherTypeElements, singleEmitter);
      } catch (ReadBufferNotAvailableForNewElementsException e) {
        singleEmitter.onError(e);
      }

    });
  }

  protected void pollReads() {

  }

  @Override
  public Completable processWrite(Map<KT, T> ownTypeElements) {
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
