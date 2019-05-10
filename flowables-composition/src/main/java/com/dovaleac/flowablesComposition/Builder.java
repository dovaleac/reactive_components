package com.dovaleac.flowablesComposition;

import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

abstract class Builder<LT, RT, KT, KT2> {

  //TEMPLATE METHOD
  public BiFunction<Flowable<LT>, Flowable<RT>, Flowable<OptionalTuple<LT, RT>>> build() {
    return new DbJoiner<>(getScenario());
  }

  protected abstract Scenario<LT, RT, KT, KT2> getScenario();

}
