package com.dovaleac.flowablesComposition.strategy;

import com.dovaleac.flowablesComposition.Situation;
import com.dovaleac.flowablesComposition.strategy.instance.JoinStrategyInstance;

import java.util.stream.Stream;

public interface JoinStrategy {

  <LT, RT> Stream<JoinStrategyInstance<LT, RT>> proposeCandidates(Situation situation,
      Class<LT> ltClass, Class<RT> rtClass);
}
