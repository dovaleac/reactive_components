package com.dovaleac.flowablesComposition.strategy.instance.helpers;

import com.dovaleac.flowablesComposition.strategy.instance.SmallTuple;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;


public class InputFlowables {

  private static <TT> Flowable<TT> getInputFlowable(Class<TT> ttClass, int count, Function<Long,
        TT> function, int start) {
    return Flowable
        .range(start, count)
        .map(Long::valueOf)
        .map(function);
  }

  public static Flowable<SmallTuple> leftFlowable(int count) {
    return getInputFlowable(SmallTuple.class, count, SmallTuple::left, 0);
  }

  public static Flowable<SmallTuple> rightFlowable(int count) {
    return getInputFlowable(SmallTuple.class, count, SmallTuple::right, 1);
  }
}
