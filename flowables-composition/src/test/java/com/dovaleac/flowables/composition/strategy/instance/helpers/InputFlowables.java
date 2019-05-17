package com.dovaleac.flowables.composition.strategy.instance.helpers;

import com.dovaleac.flowables.composition.strategy.instance.domain.SmallDomainClass;
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

  public static Flowable<SmallDomainClass> leftFlowable(int count) {
    return getInputFlowable(SmallDomainClass.class, count, SmallDomainClass::left, 0);
  }

  public static Flowable<SmallDomainClass> rightFlowable(int count) {
    return getInputFlowable(SmallDomainClass.class, count, SmallDomainClass::right, 1);
  }
}
