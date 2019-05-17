package com.dovaleac.flowables.composition.strategy;

import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.strategy.instance.JoinStrategyInstance;

import java.util.stream.Stream;

public class MinifiedBufferedJoinStrategy implements JoinStrategy {
  private static volatile MinifiedBufferedJoinStrategy mInstance;

  private MinifiedBufferedJoinStrategy() {}

  public static MinifiedBufferedJoinStrategy getInstance() {
    if (mInstance == null) {
      synchronized (MinifiedBufferedJoinStrategy.class) {
        if (mInstance == null) {
          mInstance = new MinifiedBufferedJoinStrategy();
        }
      }
    }
    return mInstance;
  }

  @Override
  public <LT, RT> Stream<JoinStrategyInstance<LT, RT>> proposeCandidates(
      Scenario scenario, Class<LT> ltClass, Class<RT> rtClass) {
    return null;
  }
}
