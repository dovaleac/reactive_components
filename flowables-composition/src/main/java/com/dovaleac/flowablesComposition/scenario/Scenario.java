package com.dovaleac.flowablesComposition.scenario;

import com.dovaleac.flowablesComposition.JoinType;
import com.dovaleac.flowablesComposition.PlannerConfig;
import io.reactivex.functions.Function;

public interface Scenario<LT, RT, KT, KT2> {

  JoinType getJoinType();
  Class<LT> getLtClass();
  Class<RT> getRtClass();
  Class<KT> getKtClass();
  Function<LT, KT> getLkFunction();
  Function<RT, KT> getRkFunction();
  Class<KT2> getKt2Class();
  Function<LT, KT2> getLk2Function();
  Function<RT, KT2> getRk2Function();
  boolean hasSecondKey();
  PlannerConfig getPlannerConfig();
}
