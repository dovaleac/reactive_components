package com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnantImpl;
import com.github.oxo42.stateless4j.StateMachine;

public class WriteBufferManager {

  private final UnmatchedYetRemnantImpl remnant;
  private final StateMachine<WriteBufferAcceptNewInputsState, WriteBufferAcceptNewInputsTrigger> stateMachine;

  public WriteBufferManager(
      UnmatchedYetRemnantImpl remnant, WriteBufferAcceptNewInputsState initialState) {
    this.remnant = remnant;
    stateMachine = new StateMachine<>(initialState,
        new WriteBufferAcceptNewInputsStateMachine(this).getConfig());
  }

  public void fire(WriteBufferAcceptNewInputsTrigger trigger) {
    stateMachine.fire(trigger);
  }

  void acceptNew() {
  }

  void rejectNew() {
  }

  void itWouldBeBetterToWrite() {
  }

  boolean isBufferFrozen() {
    return true;
  }

  boolean isFull() {
    return true;
  }
}
