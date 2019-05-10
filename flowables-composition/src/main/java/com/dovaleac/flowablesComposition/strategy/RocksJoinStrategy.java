package com.dovaleac.flowablesComposition.strategy;

import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.JoinStrategyInstance;

import java.util.stream.Stream;

public class RocksJoinStrategy implements JoinStrategy {
  private static volatile RocksJoinStrategy mInstance;

  private RocksJoinStrategy() {
  }

  public static RocksJoinStrategy getInstance() {
    if (mInstance == null) {
      synchronized (RocksJoinStrategy.class) {
        if (mInstance == null) {
          mInstance = new RocksJoinStrategy();
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
