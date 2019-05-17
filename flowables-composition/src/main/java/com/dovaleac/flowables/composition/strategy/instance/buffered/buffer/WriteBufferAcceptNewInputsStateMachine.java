package com.dovaleac.flowables.composition.strategy.instance.buffered.buffer;

import com.github.oxo42.stateless4j.StateMachineConfig;

public class WriteBufferAcceptNewInputsStateMachine {

  private final WriteBuffer writeBuffer;

  public WriteBufferAcceptNewInputsStateMachine(WriteBuffer writeBuffer) {
    this.writeBuffer = writeBuffer;
  }

  StateMachineConfig<WriteBufferAcceptNewInputsState, WriteBufferAcceptNewInputsTrigger>
      getConfig() {
    StateMachineConfig<WriteBufferAcceptNewInputsState, WriteBufferAcceptNewInputsTrigger> config =
        new StateMachineConfig<>();

    config
        .configure(WriteBufferAcceptNewInputsState.ACCEPT_NEW)
        .onEntry(writeBuffer::unfreeze)
        .permit(WriteBufferAcceptNewInputsTrigger.FREEZE, WriteBufferAcceptNewInputsState.FROZEN)
        .permit(
            WriteBufferAcceptNewInputsTrigger.MARK_AS_FULL, WriteBufferAcceptNewInputsState.FULL);

    config
        .configure(WriteBufferAcceptNewInputsState.FROZEN)
        .onEntry(writeBuffer::freeze)
        .permit(
            WriteBufferAcceptNewInputsTrigger.UNFREEZE, WriteBufferAcceptNewInputsState.ACCEPT_NEW);

    config
        .configure(WriteBufferAcceptNewInputsState.FULL)
        .onEntry(writeBuffer::itWouldBeBetterToWrite)
        .permitDynamic(
            WriteBufferAcceptNewInputsTrigger.MARK_AS_EMPTY,
            () -> {
              if (writeBuffer.isBufferFrozen()) {
                return WriteBufferAcceptNewInputsState.FROZEN;
              } else {
                return WriteBufferAcceptNewInputsState.ACCEPT_NEW;
              }
            });

    return config;
  }
}
