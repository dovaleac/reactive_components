package com.dovaleac.flowablesComposition.strategy.instance.buffered;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnantConfig;

public class BufferedStrategyConfig {

  private final UnmatchedYetRemnantConfig leftConfig;
  private final UnmatchedYetRemnantConfig rightConfig;

  public BufferedStrategyConfig(
      UnmatchedYetRemnantConfig leftConfig,
      UnmatchedYetRemnantConfig rightConfig) {
    this.leftConfig = leftConfig;
    this.rightConfig = rightConfig;
  }

  public UnmatchedYetRemnantConfig getLeftConfig() {
    return leftConfig;
  }

  public UnmatchedYetRemnantConfig getRightConfig() {
    return rightConfig;
  }
}
