package com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder;

public enum SubscriberStatusGuarderState {
  STOPPED_ON_READING,
  RUNNING,
  DEPLETED,
  OTHER_IS_DEPLETED,
  BOTH_ARE_DEPLETED
}
