package com.dovaleac.flowables.composition.scenario;

import com.dovaleac.flowables.composition.JoinType;
import com.dovaleac.flowables.composition.PlannerConfig;
import io.reactivex.functions.Function;

public interface Scenario<LT, RT, KT, K2T> {

  JoinType getJoinType();

  Class<LT> getLtClass();

  Class<RT> getRtClass();

  Class<KT> getKtClass();

  Function<LT, KT> getLkFunction();

  Function<RT, KT> getRkFunction();

  Class<K2T> getK2TClass();

  Function<LT, K2T> getLk2Function();

  Function<RT, K2T> getRk2Function();

  boolean hasSecondKey();

  PlannerConfig getPlannerConfig();
}
