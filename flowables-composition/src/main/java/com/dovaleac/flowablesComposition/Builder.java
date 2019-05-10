package com.dovaleac.flowablesComposition;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

abstract class Builder<LT, RT> implements Situation {

  //TEMPLATE METHOD
  public BiFunction<Flowable<LT>, Flowable<RT>, Flowable<Object>> build() {
    return new DbJoiner<>(this);
  }

}
