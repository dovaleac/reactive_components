package com.dovaleac.flowablesComposition;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

public class DbJoiner<LT, RT> implements BiFunction<Flowable<LT>, Flowable<RT>, Flowable<Object>> {

  private final Situation situation;

  public DbJoiner(Situation situation) {
    this.situation = situation;
  }

  public Flowable<Object> apply(Flowable<LT> ltFlowable, Flowable<RT> rtFlowable)
      throws Exception {
    return null;
  }

}
