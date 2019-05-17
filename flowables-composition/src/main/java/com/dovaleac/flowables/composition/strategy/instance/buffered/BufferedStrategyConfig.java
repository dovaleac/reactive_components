package com.dovaleac.flowables.composition.strategy.instance.buffered;

import com.dovaleac.flowables.composition.strategy.instance.buffered.remnant.UnmatchedYetRemnantConfig;

import java.util.Map;

public class BufferedStrategyConfig<LT, RT, KT> {

  private final UnmatchedYetRemnantConfig leftRemnantConfig;
  private final UnmatchedYetRemnantConfig rightRemnantConfig;
  private final int leftFlowableBuffer;
  private final int rightFlowableBuffer;
  private final Map<KT, LT> leftRemnantInitialMap;
  private final Map<KT, RT> rightRemnantInitialMap;

  public BufferedStrategyConfig(
      UnmatchedYetRemnantConfig leftRemnantConfig,
      UnmatchedYetRemnantConfig rightRemnantConfig,
      int leftFlowableBuffer,
      int rightFlowableBuffer,
      Map<KT, LT> leftRemnantInitialMap,
      Map<KT, RT> rightRemnantInitialMap) {
    this.leftRemnantConfig = leftRemnantConfig;
    this.rightRemnantConfig = rightRemnantConfig;
    this.leftFlowableBuffer = leftFlowableBuffer;
    this.rightFlowableBuffer = rightFlowableBuffer;
    this.leftRemnantInitialMap = leftRemnantInitialMap;
    this.rightRemnantInitialMap = rightRemnantInitialMap;
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

  public Map<KT, LT> getLeftRemnantInitialMap() {
    return leftRemnantInitialMap;
  }

  public Map<KT, RT> getRightRemnantInitialMap() {
    return rightRemnantInitialMap;
  }
}
