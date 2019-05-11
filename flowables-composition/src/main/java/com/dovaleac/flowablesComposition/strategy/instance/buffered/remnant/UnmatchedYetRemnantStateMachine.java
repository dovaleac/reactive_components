package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import com.github.oxo42.stateless4j.StateMachineConfig;

import static com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnantState.*;
import static com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnantTrigger.*;

public class UnmatchedYetRemnantStateMachine {

  private final UnmatchedYetRemnant unmatchedYetRemnant;

  public UnmatchedYetRemnantStateMachine(
      UnmatchedYetRemnant unmatchedYetRemnant) {
    this.unmatchedYetRemnant = unmatchedYetRemnant;
  }

  StateMachineConfig<UnmatchedYetRemnantState, UnmatchedYetRemnantTrigger> getConfig() {
    StateMachineConfig<UnmatchedYetRemnantState, UnmatchedYetRemnantTrigger> config = new StateMachineConfig<>();

    config.configure(IDLE)
        .permit(PROCESS_READ, READING)
        .permit(PROCESS_WRITE, WAITING_FOR_SYNCHRONIZER)
        .permit(SYNC_REQUESTED, SYNCHRONIZEE);

    config.configure(READING)
        .onEntry(unmatchedYetRemnant::enableConsumingReadingBuffer)
        .onEntryFrom(WRITE_BUFFER_DEPLETED, unmatchedYetRemnant::promoteSecondaryWriteBuffer)
        .permit(IT_WOULD_BE_BETTER_TO_WRITE, WAITING_FOR_SYNCHRONIZER)
        .permit(SYNC_REQUESTED, SYNCHRONIZEE);

    config.configure(WAITING_FOR_SYNCHRONIZER)
        .onEntry(unmatchedYetRemnant::enableConsumingReadingBuffer)
        .onEntry(unmatchedYetRemnant::requestSync)
        .permit(SYNC_ACCEPTED, SYNCHRONIZER)
        .permit(SYNC_REJECTED, REJECTED_SYNCHRONIZER);

    config.configure(REJECTED_SYNCHRONIZER)
        .permit(WRITE_IS_SAFE_NOW, WRITING);

    config.configure(SYNCHRONIZER)
        .onEntry(unmatchedYetRemnant::disableConsumingReadingBuffer)
        .onEntry(unmatchedYetRemnant::disableWriteBufferForFill)
        .onEntry(unmatchedYetRemnant::synchronize)
        .onExit(unmatchedYetRemnant::syncFinished)
        .permit(SYNC_FINISHED, WRITING);

    config.configure(WRITING)
        .onEntry(unmatchedYetRemnant::enableConsumingWritingBuffer)
        .onEntry(unmatchedYetRemnant::useSecondaryWriteBuffer)
        .permit(WRITE_BUFFER_DEPLETED, READING)
        .permit(SYNC_REQUESTED, REJECTED_SYNCHRONIZEE);

    config.configure(REJECTED_SYNCHRONIZEE)
        .onEntry(unmatchedYetRemnant::rejectSync)
        .onEntry(unmatchedYetRemnant::useSecondaryWriteBuffer)
        .onExit(unmatchedYetRemnant::notifyWriteIsSafe)
        .permit(WRITE_BUFFER_DEPLETED, READING);

    config.configure(SYNCHRONIZEE)
        .onEntry(unmatchedYetRemnant::disableConsumingReadingBuffer)
        .onEntry(unmatchedYetRemnant::disableWriteBufferForFill)
        .onEntry(unmatchedYetRemnant::acceptSync)
        .permit(SYNC_FINISHED, WRITING);


    return config;
  }
}
