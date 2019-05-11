package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.FlowablesDbJoinFacade;
import com.dovaleac.flowablesComposition.PlannerConfig;
import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.helpers.InputFlowables;
import com.dovaleac.flowablesComposition.tuples.OnlyRightTuple;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.vertx.reactivex.core.AbstractVerticle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class NonBlockTestVerticle extends AbstractVerticle {

  public static final int COUNT = 50000;
  protected final Function<Scenario<SmallTuple, SmallTuple, Long, ?>,
      JoinStrategyInstance<SmallTuple, SmallTuple>> strategyInstanceFunction;
  protected final PlannerConfig plannerConfig;
  private final FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep;

  public NonBlockTestVerticle(
      Function<Scenario<SmallTuple, SmallTuple, Long, ?>, JoinStrategyInstance<SmallTuple, SmallTuple>> strategyInstanceFunction,
      PlannerConfig plannerConfig,
      FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep) {
    this.strategyInstanceFunction = strategyInstanceFunction;
    this.plannerConfig = plannerConfig;
    this.initialStep = initialStep;
  }

  @Override
  public void start() throws Exception {
    super.start();
    Flowable<SmallTuple> leftFlowable = InputFlowables.leftFlowable(COUNT);
    Flowable<SmallTuple> rightFlowable = InputFlowables.rightFlowable(COUNT);

    FlowablesDbJoinFacade.PlannerConfigSpecifiedWith1KeyStep<SmallTuple, SmallTuple, Long, Object> scenario = initialStep
        .withLeftType(SmallTuple.class)
        .withRightType(SmallTuple.class)
        .withKeyType(Long.class)
        .withLeftKeyFunction(SmallTuple::getId)
        .withRightKeyFunction(SmallTuple::getId)
        .withPlannerConfig(plannerConfig);

    System.out.println("pre join");
    strategyInstanceFunction.apply(scenario)
        .join(leftFlowable, rightFlowable)
        .reduce(0, (integer, o) -> integer + 1)
    .subscribe(System.out::println);

    System.out.println("post join");
  }



}
