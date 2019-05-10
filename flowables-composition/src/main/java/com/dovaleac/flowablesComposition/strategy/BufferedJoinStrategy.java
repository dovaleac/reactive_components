package com.dovaleac.flowablesComposition.strategy;

import com.dovaleac.flowablesComposition.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.JoinStrategyInstance;

import java.util.stream.Stream;

public class BufferedJoinStrategy implements JoinStrategy {
  private static volatile BufferedJoinStrategy mInstance;

  private BufferedJoinStrategy() {
  }

  public static BufferedJoinStrategy getInstance() {
    if (mInstance == null) {
      synchronized (BufferedJoinStrategy.class) {
        if (mInstance == null) {
          mInstance = new BufferedJoinStrategy();
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
