package com.dovaleac.flowables.composition.strategy.instance;

import com.dovaleac.flowables.composition.FlowablesDbJoinFacade;
import com.dovaleac.flowables.composition.PlannerConfig;
import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.strategy.instance.helpers.CommonTestUtils;
import com.dovaleac.flowables.composition.strategy.instance.helpers.InputFlowables;
import com.dovaleac.flowables.composition.strategy.instance.helpers.NonBlockCheckingUtils;
import com.dovaleac.flowables.composition.strategy.instance.domain.SmallDomainClass;
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

    NonBlockCheckingUtils.testNonBlock(CommonTestUtils.
        joinSmallDomainClassFlowables(leftFlowable, rightFlowable, plannerConfig,
        initialStep, strategyInstanceFunction));
  }



}
