package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.FlowablesDbJoinFacade;
import com.dovaleac.flowablesComposition.PlannerConfig;
import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.domain.SmallDomainClass;
import com.dovaleac.flowablesComposition.strategy.instance.helpers.InputFlowables;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.vertx.reactivex.core.AbstractVerticle;

public class NonBlockTestVerticle extends AbstractVerticle {

  public static final int COUNT = 50000;
  protected final Function<Scenario<SmallDomainClass, SmallDomainClass, Long, ?>,
      JoinStrategyInstance<SmallDomainClass, SmallDomainClass>> strategyInstanceFunction;
  protected final PlannerConfig plannerConfig;
  private final FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep;

  public NonBlockTestVerticle(
      Function<Scenario<SmallDomainClass, SmallDomainClass, Long, ?>, JoinStrategyInstance<SmallDomainClass, SmallDomainClass>> strategyInstanceFunction,
      PlannerConfig plannerConfig,
      FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep) {
    this.strategyInstanceFunction = strategyInstanceFunction;
    this.plannerConfig = plannerConfig;
    this.initialStep = initialStep;
  }

  @Override
  public void start() throws Exception {
    super.start();
    Flowable<SmallDomainClass> leftFlowable = InputFlowables.leftFlowable(COUNT);
    Flowable<SmallDomainClass> rightFlowable = InputFlowables.rightFlowable(COUNT);

    FlowablesDbJoinFacade.PlannerConfigSpecifiedWith1KeyStep<SmallDomainClass, SmallDomainClass, Long, Object> scenario = initialStep
        .withLeftType(SmallDomainClass.class)
        .withRightType(SmallDomainClass.class)
        .withKeyType(Long.class)
        .withLeftKeyFunction(SmallDomainClass::getId)
        .withRightKeyFunction(SmallDomainClass::getId)
        .withPlannerConfig(plannerConfig);

    System.out.println("pre join");
    strategyInstanceFunction.apply(scenario)
        .join(leftFlowable, rightFlowable)
        .reduce(0, (integer, o) -> integer + 1)
    .subscribe(System.out::println);

    System.out.println("post join");
  }



}
