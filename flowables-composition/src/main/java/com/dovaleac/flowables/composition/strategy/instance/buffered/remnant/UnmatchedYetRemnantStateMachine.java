package com.dovaleac.flowables.composition.strategy.instance.buffered.remnant;

import static com.dovaleac.flowables.composition.strategy.instance.buffered.remnant.UnmatchedYetRemnantState.*;

import com.github.oxo42.stateless4j.StateMachineConfig;

public class UnmatchedYetRemnantStateMachine {

  private final UnmatchedYetRemnantImpl unmatchedYetRemnant;

  public UnmatchedYetRemnantStateMachine(UnmatchedYetRemnantImpl unmatchedYetRemnant) {
    this.unmatchedYetRemnant = unmatchedYetRemnant;
  }

  StateMachineConfig<UnmatchedYetRemnantState, UnmatchedYetRemnantTrigger> getConfig() {
    StateMachineConfig<UnmatchedYetRemnantState, UnmatchedYetRemnantTrigger> config =
        new StateMachineConfig<>();

    config
        .configure(IDLE)
        .permit(UnmatchedYetRemnantTrigger.PROCESS_READ, READING)
        .permit(UnmatchedYetRemnantTrigger.PROCESS_WRITE, WAITING_FOR_SYNCHRONIZER)
        .permit(UnmatchedYetRemnantTrigger.SYNC_REQUESTED, SYNCHRONIZEE);

    config
        .configure(READING)
        .onEntry(unmatchedYetRemnant::enableConsumingReadingBuffer)
        .permit(UnmatchedYetRemnantTrigger.IT_WOULD_BE_BETTER_TO_WRITE, WAITING_FOR_SYNCHRONIZER)
        .permit(UnmatchedYetRemnantTrigger.SYNC_REQUESTED, WAITING_FOR_SYNCHRONIZEE);

    config
        .configure(WAITING_FOR_SYNCHRONIZER)
        .onEntry(unmatchedYetRemnant::enableConsumingReadingBuffer)
        .onEntry(unmatchedYetRemnant::requestSync)
        .permit(UnmatchedYetRemnantTrigger.SYNC_ACCEPTED, SYNCHRONIZER)
        .permit(UnmatchedYetRemnantTrigger.SYNC_REJECTED, REJECTED_SYNCHRONIZER);

    config
        .configure(REJECTED_SYNCHRONIZER)
        .permit(UnmatchedYetRemnantTrigger.WRITE_IS_SAFE_NOW, WRITING);

    config
        .configure(SYNCHRONIZER)
        .onEntry(unmatchedYetRemnant::disableWriteBufferForFill)
        .onEntry(
            () -> unmatchedYetRemnant.synchronize().subscribe(unmatchedYetRemnant::syncFinished))
        .onExit(unmatchedYetRemnant::syncFinished)
        .permit(UnmatchedYetRemnantTrigger.SYNC_FINISHED, WRITING);

    config
        .configure(WRITING)
        .onEntry(unmatchedYetRemnant::enableConsumingWritingBuffer)
        .permit(UnmatchedYetRemnantTrigger.WRITE_BUFFER_DEPLETED, READING)
        .permit(UnmatchedYetRemnantTrigger.SYNC_REQUESTED, REJECTED_SYNCHRONIZEE);

    config
        .configure(REJECTED_SYNCHRONIZEE)
        .onEntry(unmatchedYetRemnant::rejectSync)
        .onExit(unmatchedYetRemnant::notifyWriteIsSafe)
        .permit(UnmatchedYetRemnantTrigger.WRITE_BUFFER_DEPLETED, READING);

    config
        .configure(WAITING_FOR_SYNCHRONIZEE)
        .permit(
            UnmatchedYetRemnantTrigger.LAST_POLL_BEFORE_BEING_SYNCHRONIZED_IS_OVER, SYNCHRONIZEE);

    config
        .configure(SYNCHRONIZEE)
        .onEntry(unmatchedYetRemnant::disableWriteBufferForFill)
        .onEntry(unmatchedYetRemnant::acceptSync)
        .permit(UnmatchedYetRemnantTrigger.SYNC_FINISHED, WRITING);

    return config;
  }
}
