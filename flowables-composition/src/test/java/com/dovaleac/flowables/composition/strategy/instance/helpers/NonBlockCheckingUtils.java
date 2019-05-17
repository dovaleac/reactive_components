package com.dovaleac.flowables.composition.strategy.instance.helpers;

import com.dovaleac.flowables.composition.strategy.instance.domain.SmallDomainClass;
import com.dovaleac.flowables.composition.tuples.OptionalTuple;
import io.reactivex.Flowable;

public class NonBlockCheckingUtils {

  public static void testNonBlock(
      Flowable<? extends OptionalTuple<SmallDomainClass, SmallDomainClass>> joinedFlowable) {
    joinedFlowable
        .reduce(0, (integer, o) -> integer + 1)
        .subscribe(System.out::println);
  }
}
