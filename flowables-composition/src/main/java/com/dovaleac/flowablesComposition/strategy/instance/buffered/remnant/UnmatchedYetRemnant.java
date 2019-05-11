package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import com.github.oxo42.stateless4j.StateMachine;

public class UnmatchedYetRemnant {

  private final StateMachine<UnmatchedYetRemnantState, UnmatchedYetRemnantTrigger> stateMachine =
      new StateMachine<>(
          UnmatchedYetRemnantState.IDLE,
          new UnmatchedYetRemnantStateMachine(this).getConfig()
      );

  private UnmatchedYetRemnant other;

  public void setOther(UnmatchedYetRemnant other) {
    this.other = other;
  }

  void disableWriteBufferForFill() {

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

  }

  void promoteSecondaryWriteBuffer() {

  }
}
