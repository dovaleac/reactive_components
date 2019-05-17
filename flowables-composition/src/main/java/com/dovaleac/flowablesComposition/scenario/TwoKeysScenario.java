package com.dovaleac.flowablesComposition.scenario;

import com.dovaleac.flowablesComposition.JoinType;
import com.dovaleac.flowablesComposition.PlannerConfig;
import io.reactivex.functions.Function;

public class TwoKeysScenario<LT, RT, KT, KT2> implements Scenario<LT, RT, KT, KT2> {

  protected final JoinType joinType;
  protected final Class<LT> ltClass;
  protected final Class<RT> rtClass;
  protected final Class<KT> ktClass;
  protected final Function<LT, KT> lkFunction;
  protected final Function<RT, KT> rkFunction;
  protected final Class<KT2> kt2Class;
  protected final Function<LT, KT2> lk2Function;
  protected final Function<RT, KT2> rk2Function;
  protected final PlannerConfig plannerConfig;

  protected TwoKeysScenario(
      JoinType joinType,
      Class<LT> ltClass,
      Class<RT> rtClass,
      Class<KT> ktClass,
      Function<LT, KT> lkFunction,
      Function<RT, KT> rkFunction,
      Class<KT2> kt2Class,
      Function<LT, KT2> lk2Function,
      Function<RT, KT2> rk2Function,
      PlannerConfig plannerConfig) {
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

  @Override
  public JoinType getJoinType() {
    return joinType;
  }

  @Override
  public Class<LT> getLtClass() {
    return ltClass;
  }

  @Override
  public Class<RT> getRtClass() {
    return rtClass;
  }

  @Override
  public Class<KT> getKtClass() {
    return ktClass;
  }

  @Override
  public Function<LT, KT> getLkFunction() {
    return lkFunction;
  }

  @Override
  public Function<RT, KT> getRkFunction() {
    return rkFunction;
  }

  @Override
  public Class<KT2> getKt2Class() {
    return kt2Class;
  }

  @Override
  public Function<LT, KT2> getLk2Function() {
    return lk2Function;
  }

  @Override
  public Function<RT, KT2> getRk2Function() {
    return rk2Function;
  }

  @Override
  public boolean hasSecondKey() {
    return true;
  }

  @Override
  public PlannerConfig getPlannerConfig() {
    return plannerConfig;
  }
}
