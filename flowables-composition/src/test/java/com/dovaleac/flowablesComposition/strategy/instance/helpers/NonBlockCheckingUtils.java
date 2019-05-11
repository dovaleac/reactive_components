package com.dovaleac.flowablesComposition.strategy.instance.helpers;

import com.dovaleac.flowablesComposition.strategy.instance.domain.SmallDomainClass;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Flowable;

public class NonBlockCheckingUtils {

  public static void testNonBlock(
      Flowable<? extends OptionalTuple<SmallDomainClass, SmallDomainClass>> joinedFlowable) {
    joinedFlowable
        .reduce(0, (integer, o) -> integer + 1)
        .subscribe(System.out::println);
  }
}
