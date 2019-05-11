package com.dovaleac.flowablesComposition.strategy.instance.buffered;

import com.github.oxo42.stateless4j.StateMachineConfig;

import static com.dovaleac.flowablesComposition.strategy.instance.buffered.RemnantState.*;
import static com.dovaleac.flowablesComposition.strategy.instance.buffered.RemnantTrigger.*;

public class RemnantStateMachine {

  private final Remnant remnant;

  public RemnantStateMachine(
      Remnant remnant) {
    this.remnant = remnant;
  }

  StateMachineConfig<RemnantState, RemnantTrigger> getConfig() {
    StateMachineConfig<RemnantState, RemnantTrigger> config = new StateMachineConfig<>();

    config.configure(IDLE)
        .permit(PROCESS_READ, READING)
        .permit(PROCESS_WRITE, WAITING_FOR_SYNCHRONIZER)
        .permit(SYNC_REQUESTED, SYNCHRONIZEE);

    config.configure(READING)
        .onEntry(remnant::enableConsumingReadingBuffer)
        .onEntryFrom(WRITE_BUFFER_DEPLETED, remnant::promoteSecondaryWriteBuffer)
        .permit(READ_BUFFER_LOW, WAITING_FOR_SYNCHRONIZER)
        .permit(SYNC_REQUESTED, SYNCHRONIZEE);

    config.configure(WAITING_FOR_SYNCHRONIZER)
        .onEntry(remnant::enableConsumingReadingBuffer)
        .onEntry(remnant::requestSync)
        .permit(SYNC_ACCEPTED, SYNCHRONIZER)
        .permit(SYNC_REJECTED, REJECTED_SYNCHRONIZER);

    config.configure(REJECTED_SYNCHRONIZER)
        .permit(WRITE_IS_SAFE_NOW, WRITING);

    config.configure(SYNCHRONIZER)
        .onEntry(remnant::disableConsumingReadingBuffer)
        .onEntry(remnant::disableWriteBufferForFill)
        .onEntry(remnant::synchronize)
        .onExit(remnant::syncFinished)
        .permit(SYNC_FINISHED, WRITING);

    config.configure(WRITING)
        .onEntry(remnant::enableConsumingWritingBuffer)
        .onEntry(remnant::useSecondaryWriteBuffer)
        .permit(WRITE_BUFFER_DEPLETED, READING)
        .permit(SYNC_REQUESTED, REJECTED_SYNCHRONIZEE);

    config.configure(REJECTED_SYNCHRONIZEE)
        .onEntry(remnant::rejectSync)
        .onEntry(remnant::useSecondaryWriteBuffer)
        .onExit(remnant::notifyWriteIsSafe)
        .permit(WRITE_BUFFER_DEPLETED, READING);

    config.configure(SYNCHRONIZEE)
        .onEntry(remnant::disableConsumingReadingBuffer)
        .onEntry(remnant::disableWriteBufferForFill)
        .onEntry(remnant::acceptSync)
        .permit(SYNC_FINISHED, WRITING);


    return config;
  }
}
