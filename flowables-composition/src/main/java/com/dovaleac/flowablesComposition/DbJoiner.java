package com.dovaleac.flowablesComposition;

import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

public class DbJoiner<LT, RT, KT, KT2> implements BiFunction<Flowable<LT>,
    Flowable<RT>, Flowable<OptionalTuple<LT, RT>>> {

  private final Scenario<LT, RT, KT, KT2> scenario;

  public DbJoiner(Scenario<LT, RT, KT, KT2> scenario) {
    this.scenario = scenario;
  }

  public Flowable<OptionalTuple<LT, RT>> apply(Flowable<LT> ltFlowable, Flowable<RT> rtFlowable)
      throws Exception {

    return null;
  }

}
