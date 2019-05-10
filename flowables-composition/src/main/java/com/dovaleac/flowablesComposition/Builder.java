package com.dovaleac.flowablesComposition;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

abstract class Builder<LT, RT> {

  //TEMPLATE METHOD
  public BiFunction<Flowable<LT>, Flowable<RT>, Flowable<Object>> build() {
    return new DbJoiner<LT, RT>(generateSituation());
  }

  //HOOK METHOD
  protected abstract Situation generateSituation();
}
