package com.dovaleac.flowablesComposition;

import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

public class DbJoiner<LT, RT> implements BiFunction<Flowable<LT>,
    Flowable<RT>, Flowable<OptionalTuple<LT, RT>>> {

  private final Situation situation;

  public DbJoiner(Situation situation) {
    this.situation = situation;
  }

  public Flowable<OptionalTuple<LT, RT>> apply(Flowable<LT> ltFlowable, Flowable<RT> rtFlowable)
      throws Exception {

    return null;
  }

}
