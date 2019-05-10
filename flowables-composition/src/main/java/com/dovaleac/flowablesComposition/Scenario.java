package com.dovaleac.flowablesComposition;

public interface Scenario<LT, RT, KT, KT2> {

  boolean hasSecondKey();
  PlannerConfig getPlannerConfig();
}
