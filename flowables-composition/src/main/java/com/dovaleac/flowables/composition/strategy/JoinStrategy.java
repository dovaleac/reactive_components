package com.dovaleac.flowables.composition.strategy;

import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.strategy.instance.JoinStrategyInstance;

import java.util.stream.Stream;

public interface JoinStrategy {

  <LT, RT> Stream<JoinStrategyInstance<LT, RT>> proposeCandidates(
      Scenario scenario, Class<LT> ltClass, Class<RT> rtClass);
}
