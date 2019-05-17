package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Flowable;

public interface JoinStrategyInstance<LT, RT> {

  double evalSuitability(Scenario scenario);

  Flowable<? extends OptionalTuple<LT, RT>> join(
      Flowable<LT> leftFlowable, Flowable<RT> rightFlowable);
}
