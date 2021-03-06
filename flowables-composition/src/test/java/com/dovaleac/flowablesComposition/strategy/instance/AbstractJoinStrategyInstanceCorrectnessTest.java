package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.FlowablesDbJoinFacade;
import com.dovaleac.flowablesComposition.PlannerConfig;
import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.domain.SmallDomainClass;
import io.reactivex.functions.Function;
import io.vertx.core.VertxOptions;
import io.vertx.reactivex.core.Vertx;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.stream.Stream;

abstract class AbstractJoinStrategyInstanceCorrectnessTest {

  protected final Function<Scenario<SmallDomainClass, SmallDomainClass, Long, ?>,
      JoinStrategyInstance<SmallDomainClass, SmallDomainClass>> strategyInstanceFunction;
  protected final PlannerConfig plannerConfig;
  protected final Path pathWithoutJoinType;

  public AbstractJoinStrategyInstanceCorrectnessTest(
      Function<Scenario<SmallDomainClass, SmallDomainClass, Long, ?>,
          JoinStrategyInstance<SmallDomainClass, SmallDomainClass>> strategyInstanceFunction,
      PlannerConfig plannerConfig, Path pathWithoutJoinType) {
    this.strategyInstanceFunction = strategyInstanceFunction;
    this.plannerConfig = plannerConfig;
    this.pathWithoutJoinType = pathWithoutJoinType;
  }

  @ParameterizedTest
  @MethodSource("provideValues")
  void test(FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep, String pathSuffix) throws Exception {

    VertxOptions options = new VertxOptions();
    options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
    Vertx vertx = Vertx.newInstance(io.vertx.core.Vertx.vertx(options));

    vertx.rxDeployVerticle(new CorrectnessTestVerticle(strategyInstanceFunction, plannerConfig,
        pathWithoutJoinType, initialStep, pathSuffix))
        .blockingGet();
  }


  private static Stream<Arguments> provideValues() {
    return Stream.of(
        Arguments.of(FlowablesDbJoinFacade.fullJoin(), "f"),
        Arguments.of(FlowablesDbJoinFacade.innerJoin(), "i"),
        Arguments.of(FlowablesDbJoinFacade.rightJoin(), "r"),
        Arguments.of(FlowablesDbJoinFacade.leftJoin(), "l")
    );
  }
}