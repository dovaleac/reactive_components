package com.dovaleac.flowablesComposition;

import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

interface Builder<LT, RT, KT, KT2> {

  //TEMPLATE METHOD
  default BiFunction<Flowable<LT>, Flowable<RT>, Flowable<OptionalTuple<LT, RT>>> build() {
    return new DbJoiner<>(getScenario());
  }

  Scenario<LT, RT, KT, KT2> getScenario();

}
