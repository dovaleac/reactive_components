package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

public enum UnmatchedYetRemnantTrigger {
  PROCESS_READ,
  PROCESS_WRITE,
  IT_WOULD_BE_BETTER_TO_WRITE,
  WRITE_BUFFER_DEPLETED,
  SYNC_REQUESTED,
  SYNC_ACCEPTED,
  SYNC_REJECTED,
  SYNC_FINISHED,
  WRITE_IS_SAFE_NOW;
}
