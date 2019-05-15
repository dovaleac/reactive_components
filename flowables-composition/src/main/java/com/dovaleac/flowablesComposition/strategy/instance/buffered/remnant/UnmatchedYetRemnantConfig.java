package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import java.util.Map;

public class UnmatchedYetRemnantConfig {
  public int getPollReadsForCheckCapacity() {
    return 0;
  }

  public int getMaxElementsInWriteBuffer() {
    return 0;
  }

  public Map getInitialMapForWriteBuffer() {
    return null;
  }
}
