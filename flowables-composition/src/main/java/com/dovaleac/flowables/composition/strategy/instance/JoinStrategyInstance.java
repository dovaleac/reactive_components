package com.dovaleac.flowables.composition.strategy.instance;

import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.tuples.OptionalTuple;
import io.reactivex.Flowable;

public interface JoinStrategyInstance<LT, RT> {

  double evalSuitability(Scenario scenario);

  Flowable<? extends OptionalTuple<LT, RT>> join(
      Flowable<LT> leftFlowable, Flowable<RT> rightFlowable);
}
