package com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer;

public enum WriteBufferAcceptNewInputsTrigger {
  FREEZE,
  UNFREEZE,
  MARK_AS_FULL,
  ENABLE_FOR_USE,
  MARK_AS_EMPTY
}
