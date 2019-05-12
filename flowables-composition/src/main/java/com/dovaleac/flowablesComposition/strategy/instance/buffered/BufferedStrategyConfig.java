package com.dovaleac.flowablesComposition.strategy.instance.buffered;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnantConfig;

public class BufferedStrategyConfig {

  private final UnmatchedYetRemnantConfig leftRemnantConfig;
  private final UnmatchedYetRemnantConfig rightRemnantConfig;
  private final int leftFlowableBuffer;
  private final int rightFlowableBuffer;

  public BufferedStrategyConfig(
      UnmatchedYetRemnantConfig leftRemnantConfig,
      UnmatchedYetRemnantConfig rightRemnantConfig, int leftFlowableBuffer, int rightFlowableBuffer) {
    this.leftRemnantConfig = leftRemnantConfig;
    this.rightRemnantConfig = rightRemnantConfig;
    this.leftFlowableBuffer = leftFlowableBuffer;
    this.rightFlowableBuffer = rightFlowableBuffer;
  }

  public UnmatchedYetRemnantConfig getLeftRemnantConfig() {
    return leftRemnantConfig;
  }

  public UnmatchedYetRemnantConfig getRightRemnantConfig() {
    return rightRemnantConfig;
  }

  public int getLeftFlowableBuffer() {
    return leftFlowableBuffer;
  }

  public int getRightFlowableBuffer() {
    return rightFlowableBuffer;
  }
}
