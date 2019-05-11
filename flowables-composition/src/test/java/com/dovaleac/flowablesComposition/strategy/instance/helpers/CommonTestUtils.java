package com.dovaleac.flowablesComposition.strategy.instance.helpers;

import com.dovaleac.flowablesComposition.FlowablesDbJoinFacade;
import com.dovaleac.flowablesComposition.PlannerConfig;
import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.JoinStrategyInstance;
import com.dovaleac.flowablesComposition.strategy.instance.domain.SmallDomainClass;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
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

  public static Flowable<? extends OptionalTuple<SmallDomainClass, SmallDomainClass>> joinCorrectnessFlowables(
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
