package com.dovaleac.flowablesComposition.strategy;

import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.JoinStrategyInstance;

import java.util.stream.Stream;

public interface JoinStrategy {

  <LT, RT> Stream<JoinStrategyInstance<LT, RT>> proposeCandidates(Scenario scenario,
      Class<LT> ltClass, Class<RT> rtClass);
}
