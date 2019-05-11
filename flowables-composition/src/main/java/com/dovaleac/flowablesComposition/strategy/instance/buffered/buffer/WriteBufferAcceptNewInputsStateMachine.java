package com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer;

import com.github.oxo42.stateless4j.StateMachineConfig;

import static com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsState.*;
import static com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer.WriteBufferAcceptNewInputsTrigger.*;

public class WriteBufferAcceptNewInputsStateMachine {

  private final WriteBufferManager writeBufferManager;

  public WriteBufferAcceptNewInputsStateMachine(
      WriteBufferManager writeBufferManager) {
    this.writeBufferManager = writeBufferManager;
  }

  StateMachineConfig<WriteBufferAcceptNewInputsState, WriteBufferAcceptNewInputsTrigger> getConfig() {
    StateMachineConfig<WriteBufferAcceptNewInputsState, WriteBufferAcceptNewInputsTrigger> config =
        new StateMachineConfig<>();

    config.configure(ACCEPT_NEW)
        .onEntry(writeBufferManager::acceptNew)
        .onExit(writeBufferManager::rejectNew)
        .permit(FREEZE, FROZEN)
        .permit(MARK_AS_FULL, FULL);

    config.configure(FROZEN)
        .permit(UNFREEZE, ACCEPT_NEW);

    config.configure(FULL)
        .onEntry(writeBufferManager::itWouldBeBetterToWrite)
        .permitDynamic(MARK_AS_EMPTY, () -> {
          if (writeBufferManager.isBufferFrozen()) {
            return FROZEN;
          } else {
            return ACCEPT_NEW;
          }
        });

    config.configure(DISABLED)
        .onEntry(writeBufferManager::rejectNew)
        .permitDynamic(ENABLE_FOR_USE, () -> {
          if (writeBufferManager.isFull()) {
            return FULL;
          } else  {
            return ACCEPT_NEW;
          }
        });

    return config;
  }
}
