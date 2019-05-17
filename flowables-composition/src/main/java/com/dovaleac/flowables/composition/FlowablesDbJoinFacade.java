package com.dovaleac.flowables.composition;

import com.dovaleac.flowables.composition.scenario.OneKeyScenario;
import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.scenario.TwoKeysScenario;
import io.reactivex.functions.Function;

public abstract class FlowablesDbJoinFacade {

  public static JoinTypeSpecifiedStep innerJoin() {
    return new JoinTypeSpecifiedStep(JoinType.INNER);
  }

  public static JoinTypeSpecifiedStep leftJoin() {
    return new JoinTypeSpecifiedStep(JoinType.LEFT);
  }

  public static JoinTypeSpecifiedStep rightJoin() {
    return new JoinTypeSpecifiedStep(JoinType.RIGHT);
  }

  public static JoinTypeSpecifiedStep fullJoin() {
    return new JoinTypeSpecifiedStep(JoinType.FULL);
  }

  public static class JoinTypeSpecifiedStep {

    private final JoinType joinType;

    private JoinTypeSpecifiedStep(JoinType joinType) {
      this.joinType = joinType;
    }

    public <LT> LeftTypeSpecifiedStep<LT> withLeftType(Class<LT> ltClass) {
      return new LeftTypeSpecifiedStep<>(joinType, ltClass);
    }
  }

  public static class LeftTypeSpecifiedStep<LT> {

    private final JoinType joinType;
    private final Class<LT> ltClass;

    private LeftTypeSpecifiedStep(JoinType joinType, Class<LT> ltClass) {
      this.joinType = joinType;
      this.ltClass = ltClass;
    }

    public <RT> RightTypeSpecifiedStep<LT, RT> withRightType(Class<RT> rtClass) {
      return new RightTypeSpecifiedStep<>(joinType, ltClass, rtClass);
    }
  }

  public static class RightTypeSpecifiedStep<LT, RT> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;

    private RightTypeSpecifiedStep(JoinType joinType, Class<LT> ltClass, Class<RT> rtClass) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
    }

    public <KT> KeyTypeSpecifiedStep<LT, RT, KT> withKeyType(Class<KT> ktClass) {
      return new KeyTypeSpecifiedStep<>(joinType, ltClass, rtClass, ktClass);
    }
  }

  public static class KeyTypeSpecifiedStep<LT, RT, KT> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;

    private KeyTypeSpecifiedStep(
        JoinType joinType, Class<LT> ltClass, Class<RT> rtClass, Class<KT> ktClass) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
    }

    public LeftKeyFunctionSpecifiedStep<LT, RT, KT> withLeftKeyFunction(
        Function<LT, KT> lkFunction) {
      return new LeftKeyFunctionSpecifiedStep<>(joinType, ltClass, rtClass, ktClass, lkFunction);
    }
  }

  public static class LeftKeyFunctionSpecifiedStep<LT, RT, KT> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;

    private LeftKeyFunctionSpecifiedStep(
        JoinType joinType,
        Class<LT> ltClass,
        Class<RT> rtClass,
        Class<KT> ktClass,
        Function<LT, KT> lkFunction) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
    }

    public <K2T> RightKeyFunctionSpecifiedStep<LT, RT, KT, K2T> withRightKeyFunction(
        Function<RT, KT> rkFunction) {
      return new RightKeyFunctionSpecifiedStep<>(
          joinType, ltClass, rtClass, ktClass, lkFunction, rkFunction);
    }
  }

  public static class RightKeyFunctionSpecifiedStep<LT, RT, KT, K2T>
      extends OneKeyScenario<LT, RT, KT, K2T> implements Builder<LT, RT, KT, K2T> {

    private RightKeyFunctionSpecifiedStep(
        JoinType joinType,
        Class<LT> ltClass,
        Class<RT> rtClass,
        Class<KT> ktClass,
        Function<LT, KT> lkFunction,
        Function<RT, KT> rkFunction) {
      super(joinType, ltClass, rtClass, ktClass, lkFunction, rkFunction, PlannerConfig.NO_CONFIG);
    }

    public <K2T> KeyType2SpecifiedStep<LT, RT, KT, K2T> withKeyType2(Class<K2T> k2TClass) {
      return new KeyType2SpecifiedStep<>(
          joinType, ltClass, rtClass, ktClass, lkFunction, rkFunction, k2TClass);
    }

    public PlannerConfigSpecifiedWith1KeyStep<LT, RT, KT, K2T> withPlannerConfig(
        PlannerConfig plannerConfig) {
      return new PlannerConfigSpecifiedWith1KeyStep<>(
          joinType, ltClass, rtClass, ktClass, lkFunction, rkFunction, plannerConfig);
    }

    @Override
    public Scenario<LT, RT, KT, K2T> getScenario() {
      return this;
    }
  }

  public static class PlannerConfigSpecifiedWith1KeyStep<LT, RT, KT, K2T>
      extends OneKeyScenario<LT, RT, KT, K2T> implements Builder<LT, RT, KT, K2T> {

    private PlannerConfigSpecifiedWith1KeyStep(
        JoinType joinType,
        Class<LT> ltClass,
        Class<RT> rtClass,
        Class<KT> ktClass,
        Function<LT, KT> lkFunction,
        Function<RT, KT> rkFunction,
        PlannerConfig plannerConfig) {
      super(joinType, ltClass, rtClass, ktClass, lkFunction, rkFunction, plannerConfig);
    }

    @Override
    public Scenario<LT, RT, KT, K2T> getScenario() {
      return this;
    }
  }

  public static class KeyType2SpecifiedStep<LT, RT, KT, K2T> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;
    private final Function<RT, KT> rkFunction;
    private final Class<K2T> k2TClass;

    private KeyType2SpecifiedStep(
        JoinType joinType,
        Class<LT> ltClass,
        Class<RT> rtClass,
        Class<KT> ktClass,
        Function<LT, KT> lkFunction,
        Function<RT, KT> rkFunction,
        Class<K2T> k2TClass) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
      this.rkFunction = rkFunction;
      this.k2TClass = k2TClass;
    }

    public LeftKey2FunctionSpecifiedStep<LT, RT, KT, K2T> withLeftKey2Function(
        Function<LT, K2T> lk2Function) {
      return new LeftKey2FunctionSpecifiedStep<>(
          joinType, ltClass, rtClass, ktClass, lkFunction, rkFunction, k2TClass, lk2Function);
    }
  }

  public static class LeftKey2FunctionSpecifiedStep<LT, RT, KT, K2T> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;
    private final Function<RT, KT> rkFunction;
    private final Class<K2T> k2TClass;
    private final Function<LT, K2T> lk2Function;

    private LeftKey2FunctionSpecifiedStep(
        JoinType joinType,
        Class<LT> ltClass,
        Class<RT> rtClass,
        Class<KT> ktClass,
        Function<LT, KT> lkFunction,
        Function<RT, KT> rkFunction,
        Class<K2T> k2TClass,
        Function<LT, K2T> lk2Function) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
      this.rkFunction = rkFunction;
      this.k2TClass = k2TClass;
      this.lk2Function = lk2Function;
    }

    public RightKey2FunctionSpecifiedStep<LT, RT, KT, K2T> withRightKey2Function(
        Function<RT, K2T> rk2Function) {
      return new RightKey2FunctionSpecifiedStep<>(
          joinType,
          ltClass,
          rtClass,
          ktClass,
          lkFunction,
          rkFunction,
          k2TClass,
          lk2Function,
          rk2Function);
    }
  }

  public static class RightKey2FunctionSpecifiedStep<LT, RT, KT, K2T>
      extends TwoKeysScenario<LT, RT, KT, K2T> implements Builder<LT, RT, KT, K2T> {

    private RightKey2FunctionSpecifiedStep(
        JoinType joinType,
        Class<LT> ltClass,
        Class<RT> rtClass,
        Class<KT> ktClass,
        Function<LT, KT> lkFunction,
        Function<RT, KT> rkFunction,
        Class<K2T> k2TClass,
        Function<LT, K2T> lk2Function,
        Function<RT, K2T> rk2Function) {
      super(
          joinType,
          ltClass,
          rtClass,
          ktClass,
          lkFunction,
          rkFunction,
          k2TClass,
          lk2Function,
          rk2Function,
          PlannerConfig.NO_CONFIG);
    }

    public PlannerConfigSpecifiedWith2KeysStep<LT, RT, KT, K2T> withPlannerConfig(
        PlannerConfig plannerConfig) {
      return new PlannerConfigSpecifiedWith2KeysStep<>(
          joinType,
          ltClass,
          rtClass,
          ktClass,
          lkFunction,
          rkFunction,
          k2TClass,
          lk2Function,
          rk2Function,
          plannerConfig);
    }

    @Override
    public Scenario<LT, RT, KT, K2T> getScenario() {
      return this;
    }
  }

  public static class PlannerConfigSpecifiedWith2KeysStep<LT, RT, KT, K2T>
      extends TwoKeysScenario<LT, RT, KT, K2T> implements Builder<LT, RT, KT, K2T> {

    private PlannerConfigSpecifiedWith2KeysStep(
        JoinType joinType,
        Class<LT> ltClass,
        Class<RT> rtClass,
        Class<KT> ktClass,
        Function<LT, KT> lkFunction,
        Function<RT, KT> rkFunction,
        Class<K2T> k2TClass,
        Function<LT, K2T> lk2Function,
        Function<RT, K2T> rk2Function,
        PlannerConfig plannerConfig) {
      super(
          joinType,
          ltClass,
          rtClass,
          ktClass,
          lkFunction,
          rkFunction,
          k2TClass,
          lk2Function,
          rk2Function,
          plannerConfig);
    }

    @Override
    public Scenario<LT, RT, KT, K2T> getScenario() {
      return this;
    }
  }
}
