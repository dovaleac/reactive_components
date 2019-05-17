package com.dovaleac.flowables.composition.strategy.instance;

import com.dovaleac.flowables.composition.FlowablesDbJoinFacade;
import com.dovaleac.flowables.composition.PlannerConfig;
import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.strategy.instance.domain.SmallDomainClass;
import io.reactivex.functions.Function;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.core.VertxOptions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.stream.Stream;

@ExtendWith(VertxExtension.class)
abstract class AbstractJoinStrategyInstanceNonBlockTest {

  protected final Function<Scenario<SmallDomainClass, SmallDomainClass, Long, ?>,
      JoinStrategyInstance<SmallDomainClass, SmallDomainClass>> strategyInstanceFunction;
  protected final PlannerConfig plannerConfig;
  protected final Path pathWithoutJoinType;

  public AbstractJoinStrategyInstanceNonBlockTest(
      Function<Scenario<SmallDomainClass, SmallDomainClass, Long, ?>,
          JoinStrategyInstance<SmallDomainClass, SmallDomainClass>> strategyInstanceFunction,
      PlannerConfig plannerConfig, Path pathWithoutJoinType) {
    this.strategyInstanceFunction = strategyInstanceFunction;
    this.plannerConfig = plannerConfig;
    this.pathWithoutJoinType = pathWithoutJoinType;
  }

  @ParameterizedTest
  @MethodSource("provideValues")
  void test(FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep, io.vertx.core.Vertx vertxUgly,
      VertxTestContext testContext) throws Exception {

    Checkpoint finishedDeployment = testContext.checkpoint();
    VertxOptions options = new VertxOptions();
    Vertx vertx = Vertx.newInstance(io.vertx.core.Vertx.vertx(options));

    vertx.deployVerticle(new NonBlockTestVerticle(strategyInstanceFunction, plannerConfig,
         initialStep), result -> {
      System.out.println(result.succeeded());
      finishedDeployment.flag();
    });
  }


  private static Stream<Arguments> provideValues() {
    return Stream.of(
        Arguments.of(FlowablesDbJoinFacade.fullJoin()),
        Arguments.of(FlowablesDbJoinFacade.innerJoin()),
        Arguments.of(FlowablesDbJoinFacade.rightJoin()),
        Arguments.of(FlowablesDbJoinFacade.leftJoin())
    );
  }

}