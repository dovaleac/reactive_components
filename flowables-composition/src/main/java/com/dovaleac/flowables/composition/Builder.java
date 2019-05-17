package com.dovaleac.flowables.composition;

import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.tuples.OptionalTuple;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

interface Builder<LT, RT, KT, K2T> {

  // TEMPLATE METHOD
  default BiFunction<Flowable<LT>, Flowable<RT>, Flowable<OptionalTuple<LT, RT>>> build() {
    return new DbJoiner<>(getScenario());
  }

  Scenario<LT, RT, KT, K2T> getScenario();
}
