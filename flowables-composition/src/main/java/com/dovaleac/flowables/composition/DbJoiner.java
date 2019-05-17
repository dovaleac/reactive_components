package com.dovaleac.flowables.composition;

import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.tuples.OptionalTuple;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

public class DbJoiner<LT, RT, KT, K2T>
    implements BiFunction<Flowable<LT>, Flowable<RT>, Flowable<OptionalTuple<LT, RT>>> {

  private final Scenario<LT, RT, KT, K2T> scenario;

  public DbJoiner(Scenario<LT, RT, KT, K2T> scenario) {
    this.scenario = scenario;
  }

  public Flowable<OptionalTuple<LT, RT>> apply(Flowable<LT> ltFlowable, Flowable<RT> rtFlowable)
      throws Exception {

    return null;
  }
}
