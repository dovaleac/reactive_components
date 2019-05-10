package com.dovaleac.flowablesComposition.strategy;

import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.JoinStrategyInstance;

import java.util.stream.Stream;

public class MinifiedBufferedJoinStrategy implements JoinStrategy {
  private static volatile MinifiedBufferedJoinStrategy mInstance;

  private MinifiedBufferedJoinStrategy() {
  }

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
  public <LT, RT> Stream<JoinStrategyInstance<LT, RT>> proposeCandidates(Scenario scenario,
      Class<LT> ltClass, Class<RT> rtClass) {
    return null;
  }

}
