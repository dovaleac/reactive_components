package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

public enum UnmatchedYetRemnantTrigger {
  PROCESS_READ,
  PROCESS_WRITE,
  READ_BUFFER_LOW,
  WRITE_BUFFER_DEPLETED,
  SYNC_REQUESTED,
  SYNC_ACCEPTED,
  SYNC_REJECTED,
  SYNC_FINISHED,
  WRITE_IS_SAFE_NOW;
}
