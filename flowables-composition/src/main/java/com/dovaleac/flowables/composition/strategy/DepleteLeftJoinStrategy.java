package com.dovaleac.flowables.composition.strategy;

import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.strategy.instance.JoinStrategyInstance;

import java.util.stream.Stream;

public class DepleteLeftJoinStrategy implements JoinStrategy {
  private static volatile DepleteLeftJoinStrategy mInstance;

  private DepleteLeftJoinStrategy() {}

  public static DepleteLeftJoinStrategy getInstance() {
    if (mInstance == null) {
      synchronized (DepleteLeftJoinStrategy.class) {
        if (mInstance == null) {
          mInstance = new DepleteLeftJoinStrategy();
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
