package com.dovaleac.flowables.composition.strategy.instance.helpers;

import com.dovaleac.flowables.composition.FlowablesDbJoinFacade;
import com.dovaleac.flowables.composition.PlannerConfig;
import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.strategy.instance.JoinStrategyInstance;
import com.dovaleac.flowables.composition.strategy.instance.domain.SmallDomainClass;
import com.dovaleac.flowables.composition.tuples.OptionalTuple;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class CommonTestUtils {

  private static <LT, RT, KT> Flowable<? extends OptionalTuple<LT, RT>> joinFlowables(
      Flowable<LT> leftFlowable,
      Flowable<RT> rightFlowable,
      PlannerConfig plannerConfig,
      FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep,
      Function<Scenario<LT, RT, KT, ?>,
          JoinStrategyInstance<LT, RT>> strategyInstanceFunction,
      Class<LT> ltClass,
      Class<RT> rtClass,
      Class<KT> ktClass,
      Function<LT, KT> lkFunction,
      Function<RT, KT> rkFunction) throws Exception {
    FlowablesDbJoinFacade.PlannerConfigSpecifiedWith1KeyStep<LT, RT, KT, Object>
        scenario = initialStep
        .withLeftType(ltClass)
        .withRightType(rtClass)
        .withKeyType(ktClass)
        .withLeftKeyFunction(lkFunction)
        .withRightKeyFunction(rkFunction)
        .withPlannerConfig(plannerConfig);

    return strategyInstanceFunction.apply(scenario)
        .join(leftFlowable, rightFlowable);
  }

  public static Flowable<? extends OptionalTuple<SmallDomainClass, SmallDomainClass>> joinSmallDomainClassFlowables(
      Flowable<SmallDomainClass> leftFlowable,
      Flowable<SmallDomainClass> rightFlowable,
      PlannerConfig plannerConfig,
      FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep,
      Function<Scenario<SmallDomainClass, SmallDomainClass, Long, ?>,
            JoinStrategyInstance<SmallDomainClass, SmallDomainClass>> strategyInstanceFunction) throws Exception {
    return joinFlowables(leftFlowable, rightFlowable, plannerConfig, initialStep,
        strategyInstanceFunction, SmallDomainClass.class, SmallDomainClass.class, Long.class,
        SmallDomainClass::getId, SmallDomainClass::getId);
  }
}
