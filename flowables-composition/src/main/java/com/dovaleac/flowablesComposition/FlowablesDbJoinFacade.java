package com.dovaleac.flowablesComposition;

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
      return new LeftTypeSpecifiedStep<LT>(joinType, ltClass);
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
      return new RightTypeSpecifiedStep<LT, RT>(joinType, ltClass, rtClass);
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
      return new KeyTypeSpecifiedStep<LT, RT, KT>(joinType, ltClass, rtClass, ktClass);
    }
  }

  public static class KeyTypeSpecifiedStep<LT, RT, KT> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;

    private KeyTypeSpecifiedStep(JoinType joinType, Class<LT> ltClass, Class<RT> rtClass,
        Class<KT> ktClass) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
    }

    public LeftKeyFunctionSpecifiedStep<LT, RT, KT> withLeftKeyFunction(
        Function<LT, KT> lkFunction) {
      return new LeftKeyFunctionSpecifiedStep<LT, RT, KT>(joinType, ltClass, rtClass, ktClass,
          lkFunction);
    }
  }

  public static class LeftKeyFunctionSpecifiedStep<LT, RT, KT> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;

    private LeftKeyFunctionSpecifiedStep(JoinType joinType, Class<LT> ltClass, Class<RT> rtClass,
        Class<KT> ktClass, Function<LT, KT> lkFunction) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
    }

    public RightKeyFunctionSpecifiedStep<LT, RT, KT> withRightKeyFunction(
        Function<RT, KT> rkFunction) {
      return new RightKeyFunctionSpecifiedStep<LT, RT, KT>(joinType, ltClass, rtClass, ktClass,
          lkFunction, rkFunction);
    }
  }

  public static class RightKeyFunctionSpecifiedStep<LT, RT, KT> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;
    private final Function<RT, KT> rkFunction;

    private RightKeyFunctionSpecifiedStep(JoinType joinType, Class<LT> ltClass, Class<RT> rtClass,
        Class<KT> ktClass, Function<LT, KT> lkFunction, Function<RT, KT> rkFunction) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
      this.rkFunction = rkFunction;
    }

    public <KT2> KeyType2SpecifiedStep<LT, RT, KT, KT2> withKeyType2(
        Class<KT2> kt2Class) {
      return new KeyType2SpecifiedStep<LT, RT, KT, KT2>(joinType, ltClass, rtClass, ktClass,
          lkFunction, rkFunction, kt2Class);
    }

    public PlannerConfigSpecifiedWith1KeyStep<LT, RT, KT> withPlannerConfig(
        PlannerConfig plannerConfig) {
      return new PlannerConfigSpecifiedWith1KeyStep<LT, RT, KT>(joinType, ltClass, rtClass, ktClass,
          lkFunction, rkFunction, plannerConfig);
    }
  }

  public static class PlannerConfigSpecifiedWith1KeyStep<LT, RT, KT> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;
    private final Function<RT, KT> rkFunction;
    private final PlannerConfig plannerConfig;

    private PlannerConfigSpecifiedWith1KeyStep(JoinType joinType, Class<LT> ltClass,
        Class<RT> rtClass, Class<KT> ktClass, Function<LT, KT> lkFunction,
        Function<RT, KT> rkFunction, PlannerConfig plannerConfig) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
      this.rkFunction = rkFunction;
      this.plannerConfig = plannerConfig;
    }
  }



  public static class KeyType2SpecifiedStep<LT, RT, KT, KT2> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;
    private final Function<RT, KT> rkFunction;
    private final Class<KT2> kt2Class;

    private KeyType2SpecifiedStep(JoinType joinType, Class<LT> ltClass, Class<RT> rtClass, Class<KT> ktClass,
        Function<LT, KT> lkFunction, Function<RT, KT> rkFunction, Class<KT2> kt2Class) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
      this.rkFunction = rkFunction;
      this.kt2Class = kt2Class;
    }

    public LeftKey2FunctionSpecifiedStep<LT, RT, KT, KT2> withLeftKey2Function(
        Function<LT, KT2> lk2Function) {
      return new LeftKey2FunctionSpecifiedStep<LT, RT, KT, KT2>(joinType, ltClass, rtClass, ktClass,
          lkFunction, rkFunction, kt2Class, lk2Function);
    }
  }

  public static class LeftKey2FunctionSpecifiedStep<LT, RT, KT, KT2> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;
    private final Function<RT, KT> rkFunction;
    private final Class<KT2> kt2Class;
    private final Function<LT, KT2> lk2Function;

    private LeftKey2FunctionSpecifiedStep(JoinType joinType, Class<LT> ltClass, Class<RT> rtClass,
        Class<KT> ktClass, Function<LT, KT> lkFunction, Function<RT, KT> rkFunction,
        Class<KT2> kt2Class, Function<LT, KT2> lk2Function) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
      this.rkFunction = rkFunction;
      this.kt2Class = kt2Class;
      this.lk2Function = lk2Function;
    }

    public RightKey2FunctionSpecifiedStep<LT, RT, KT, KT2> withLeftKey2Function(
        Function<RT, KT2> rk2Function) {
      return new RightKey2FunctionSpecifiedStep<LT, RT, KT, KT2>(joinType, ltClass, rtClass,
          ktClass, lkFunction, rkFunction, kt2Class, lk2Function, rk2Function);
    }
  }

  public static class RightKey2FunctionSpecifiedStep<LT, RT, KT, KT2> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;
    private final Function<RT, KT> rkFunction;
    private final Class<KT2> kt2Class;
    private final Function<LT, KT2> lk2Function;
    private final Function<RT, KT2> rk2Function;

    private RightKey2FunctionSpecifiedStep(JoinType joinType, Class<LT> ltClass, Class<RT> rtClass,
        Class<KT> ktClass, Function<LT, KT> lkFunction, Function<RT, KT> rkFunction,
        Class<KT2> kt2Class, Function<LT, KT2> lk2Function, Function<RT, KT2> rk2Function) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
      this.rkFunction = rkFunction;
      this.kt2Class = kt2Class;
      this.lk2Function = lk2Function;
      this.rk2Function = rk2Function;
    }

    public PlannerConfigSpecifiedWith2KeysStep<LT, RT, KT, KT2> withLeftKey2Function(
        PlannerConfig plannerConfig) {
      return new PlannerConfigSpecifiedWith2KeysStep<LT, RT, KT, KT2>(joinType, ltClass, rtClass,
          ktClass, lkFunction, rkFunction, kt2Class, lk2Function, rk2Function, plannerConfig);
    }
  }

  public static class PlannerConfigSpecifiedWith2KeysStep<LT, RT, KT, KT2> {

    private final JoinType joinType;
    private final Class<LT> ltClass;
    private final Class<RT> rtClass;
    private final Class<KT> ktClass;
    private final Function<LT, KT> lkFunction;
    private final Function<RT, KT> rkFunction;
    private final Class<KT2> kt2Class;
    private final Function<LT, KT2> lk2Function;
    private final Function<RT, KT2> rk2Function;
    private final PlannerConfig plannerConfig;

    private PlannerConfigSpecifiedWith2KeysStep(JoinType joinType, Class<LT> ltClass,
        Class<RT> rtClass, Class<KT> ktClass, Function<LT, KT> lkFunction,
        Function<RT, KT> rkFunction, Class<KT2> kt2Class, Function<LT, KT2> lk2Function,
        Function<RT, KT2> rk2Function, PlannerConfig plannerConfig) {
      this.joinType = joinType;
      this.ltClass = ltClass;
      this.rtClass = rtClass;
      this.ktClass = ktClass;
      this.lkFunction = lkFunction;
      this.rkFunction = rkFunction;
      this.kt2Class = kt2Class;
      this.lk2Function = lk2Function;
      this.rk2Function = rk2Function;
      this.plannerConfig = plannerConfig;
    }
  }


}
