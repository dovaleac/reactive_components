package com.dovaleac.flowables.composition;

import io.reactivex.functions.Function;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FlowablesDbJoinFacadeTest {

  @ParameterizedTest
  @MethodSource("provideValues")
  void test(FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep, JoinType expectedJoinType) {
    Class<String> ltClass = String.class;
    Class<Double> rtClass = Double.class;
    Class<Integer> ktClass = Integer.class;
    Function<String, Integer> lkFunction = String::length;
    Function<Double, Integer> rkFunction = Double::intValue;
    PlannerConfig plannerConfig = PlannerConfig.builder()
        .withAreSorted(true).build();

    FlowablesDbJoinFacade.RightKeyFunctionSpecifiedStep<String, Double, Integer, ?> preLastStep1 =
        initialStep
        .withLeftType(ltClass)
        .withRightType(rtClass)
        .withKeyType(ktClass)
        .withLeftKeyFunction(lkFunction)
        .withRightKeyFunction(rkFunction);

    assertFalse(preLastStep1.hasSecondKey());
    PlannerConfig noConfig = PlannerConfig.builder().build();
    assertEquals(noConfig, preLastStep1.getPlannerConfig());

    FlowablesDbJoinFacade.PlannerConfigSpecifiedWith1KeyStep<String, Double, Integer, ?> lastStep1
        = preLastStep1
        .withPlannerConfig(plannerConfig);


    assertEquals(expectedJoinType, lastStep1.getJoinType());
    assertEquals(ltClass, lastStep1.getLtClass());
    assertEquals(rtClass, lastStep1.getRtClass());
    assertEquals(ktClass, lastStep1.getKtClass());
    assertEquals(lkFunction, lastStep1.getLkFunction());
    assertEquals(rkFunction, lastStep1.getRkFunction());
    assertEquals(plannerConfig, lastStep1.getPlannerConfig());
    assertFalse(lastStep1.hasSecondKey());


    Class<Float> kt2Class = Float.class;
    Function<String, Float> lk2Function = s -> 0F;
    Function<Double, Float> rk2Function = Double::floatValue;
    PlannerConfig plannerConfig2 = PlannerConfig.builder().withLeftCardinality(100000).build();

    FlowablesDbJoinFacade.RightKey2FunctionSpecifiedStep<String, Double, Integer, Float> preLastStep2 = preLastStep1
        .withKeyType2(kt2Class)
        .withLeftKey2Function(lk2Function)
        .withRightKey2Function(rk2Function);

    assertTrue(preLastStep2.hasSecondKey());
    assertEquals(noConfig, preLastStep2.getPlannerConfig());

    FlowablesDbJoinFacade.PlannerConfigSpecifiedWith2KeysStep<String, Double, Integer, Float> lastStep2 = preLastStep2
        .withPlannerConfig(plannerConfig2);

    assertEquals(expectedJoinType, lastStep2.getJoinType());
    assertEquals(ltClass, lastStep2.getLtClass());
    assertEquals(rtClass, lastStep2.getRtClass());
    assertEquals(ktClass, lastStep2.getKtClass());
    assertEquals(lkFunction, lastStep2.getLkFunction());
    assertEquals(rkFunction, lastStep2.getRkFunction());
    assertEquals(kt2Class, lastStep2.getK2TClass());
    assertEquals(lk2Function, lastStep2.getLk2Function());
    assertEquals(rk2Function, lastStep2.getRk2Function());
    assertEquals(plannerConfig2, lastStep2.getPlannerConfig());
    assertTrue(lastStep2.hasSecondKey());

  }

  private static Stream<Arguments> provideValues() {
    return Stream.of(
        Arguments.of(FlowablesDbJoinFacade.fullJoin(), JoinType.FULL),
        Arguments.of(FlowablesDbJoinFacade.innerJoin(), JoinType.INNER),
        Arguments.of(FlowablesDbJoinFacade.rightJoin(), JoinType.RIGHT),
        Arguments.of(FlowablesDbJoinFacade.leftJoin(), JoinType.LEFT)
    );
  }
}