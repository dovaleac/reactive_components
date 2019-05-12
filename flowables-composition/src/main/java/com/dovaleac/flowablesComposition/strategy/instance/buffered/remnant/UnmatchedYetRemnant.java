package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

public interface UnmatchedYetRemnant<T extends UnmatchedYetRemnant> {
  void setOther(T other);
}
