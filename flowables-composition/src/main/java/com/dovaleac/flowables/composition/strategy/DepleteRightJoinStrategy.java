package com.dovaleac.flowables.composition.strategy;

import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.strategy.instance.JoinStrategyInstance;

import java.util.stream.Stream;

public class DepleteRightJoinStrategy implements JoinStrategy {
  private static volatile DepleteRightJoinStrategy mInstance;

  private DepleteRightJoinStrategy() {}

  public static DepleteRightJoinStrategy getInstance() {
    if (mInstance == null) {
      synchronized (DepleteRightJoinStrategy.class) {
        if (mInstance == null) {
          mInstance = new DepleteRightJoinStrategy();
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
