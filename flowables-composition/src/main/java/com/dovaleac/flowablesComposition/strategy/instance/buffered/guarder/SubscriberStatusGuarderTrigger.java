package com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder;

public enum SubscriberStatusGuarderTrigger {
  STOP_ON_READING,
  RETAKE_READING,
  MARK_AS_DEPLETED,
  NOTIFY_OTHER_IS_DEPLETED
}
