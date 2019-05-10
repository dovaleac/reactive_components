package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.Situation;
import io.reactivex.Flowable;

public interface JoinStrategyInstance<LT, RT> {

  double evalSuitability(Situation situation);
  Flowable<Object> join(Flowable<LT> leftFlowable, Flowable<RT> rightFlowable);
}
