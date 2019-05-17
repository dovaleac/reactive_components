package com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer;

import com.github.oxo42.stateless4j.StateMachineConfig;

import static com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsState.*;
import static com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsTrigger.*;

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
        .configure(ACCEPT_NEW)
        .onEntry(writeBuffer::unfreeze)
        .permit(FREEZE, FROZEN)
        .permit(MARK_AS_FULL, FULL);

    config.configure(FROZEN).onEntry(writeBuffer::freeze).permit(UNFREEZE, ACCEPT_NEW);

    config
        .configure(FULL)
        .onEntry(writeBuffer::itWouldBeBetterToWrite)
        .permitDynamic(
            MARK_AS_EMPTY,
            () -> {
              if (writeBuffer.isBufferFrozen()) {
                return FROZEN;
              } else {
                return ACCEPT_NEW;
              }
            });

    return config;
  }
}
