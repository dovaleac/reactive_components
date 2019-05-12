package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsState;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsTrigger;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferManager;
import com.github.oxo42.stateless4j.StateMachine;

public class UnmatchedYetRemnantImpl implements UnmatchedYetRemnant<UnmatchedYetRemnantImpl> {

  private final StateMachine<UnmatchedYetRemnantState, UnmatchedYetRemnantTrigger> stateMachine =
      new StateMachine<>(
          UnmatchedYetRemnantState.IDLE,
          new UnmatchedYetRemnantStateMachine(this).getConfig()
      );
  private final UnmatchedYetRemnantConfig config;

  private UnmatchedYetRemnantImpl other;
  private WriteBufferManager primaryWriteBuffer = new WriteBufferManager(this,
      WriteBufferAcceptNewInputsState.ACCEPT_NEW);
  private WriteBufferManager secondaryWriteBuffer = new WriteBufferManager(this,
      WriteBufferAcceptNewInputsState.DISABLED);

  private WriteBufferManager writeBufferInUse = primaryWriteBuffer;

  public UnmatchedYetRemnantImpl(
      UnmatchedYetRemnantConfig config) {
    this.config = config;
  }

  @Override
  public void setOther(UnmatchedYetRemnantImpl other) {
    this.other = other;
  }

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
    other.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_REQUESTED);
  }

  void rejectSync() {
    other.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_REJECTED);
  }

  void acceptSync() {
    other.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_ACCEPTED);
  }

  void synchronize() {

  }

  void syncFinished() {
    other.stateMachine.fire(UnmatchedYetRemnantTrigger.SYNC_FINISHED);
  }

  void notifyWriteIsSafe() {
    other.stateMachine.fire(UnmatchedYetRemnantTrigger.WRITE_IS_SAFE_NOW);
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