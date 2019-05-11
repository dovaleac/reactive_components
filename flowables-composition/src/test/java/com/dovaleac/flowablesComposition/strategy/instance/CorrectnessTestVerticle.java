package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.FlowablesDbJoinFacade;
import com.dovaleac.flowablesComposition.PlannerConfig;
import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.domain.SmallDomainClass;
import com.dovaleac.flowablesComposition.strategy.instance.helpers.CommonTestUtils;
import com.dovaleac.flowablesComposition.strategy.instance.helpers.CorrectnessCheckingUtils;
import com.dovaleac.flowablesComposition.strategy.instance.helpers.InputFlowables;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.vertx.reactivex.core.AbstractVerticle;

import java.nio.file.Path;

public class CorrectnessTestVerticle extends AbstractVerticle {

  public static final int COUNT = 100;
  protected final Function<Scenario<SmallDomainClass, SmallDomainClass, Long, ?>,
      JoinStrategyInstance<SmallDomainClass, SmallDomainClass>> strategyInstanceFunction;
  protected final PlannerConfig plannerConfig;
  protected final Path pathWithoutJoinType;
  protected final Path righteousPath = Path.of("src", "test", "resources", "right");
  private final FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep;
  private final String pathSuffix;

  public CorrectnessTestVerticle(
      Function<Scenario<SmallDomainClass, SmallDomainClass, Long, ?>, JoinStrategyInstance<SmallDomainClass, SmallDomainClass>> strategyInstanceFunction,
      PlannerConfig plannerConfig, Path pathWithoutJoinType,
      FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep, String pathSuffix) {
    this.strategyInstanceFunction = strategyInstanceFunction;
    this.plannerConfig = plannerConfig;
    this.pathWithoutJoinType = pathWithoutJoinType;
    this.initialStep = initialStep;
    this.pathSuffix = pathSuffix;
  }

  @Override
  public void start() throws Exception {
    super.start();
    Flowable<SmallDomainClass> leftFlowable = InputFlowables.leftFlowable(COUNT);
    Flowable<SmallDomainClass> rightFlowable = InputFlowables.rightFlowable(COUNT);

    Flowable<? extends OptionalTuple<SmallDomainClass, SmallDomainClass>> joinedFlowable =
        CommonTestUtils.joinSmallDomainClassFlowables(leftFlowable, rightFlowable, plannerConfig,
            initialStep, strategyInstanceFunction);

    CorrectnessCheckingUtils.checkCorrectness(pathWithoutJoinType, righteousPath, joinedFlowable,
        pathSuffix);

  }




}
