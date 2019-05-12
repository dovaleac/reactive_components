package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.BufferedStrategyConfig;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnant;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnantImpl;
import com.dovaleac.flowablesComposition.tuples.InnerJoinTuple;
import com.dovaleac.flowablesComposition.tuples.OnlyLeftTuple;
import com.dovaleac.flowablesComposition.tuples.OnlyRightTuple;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

import java.util.Map;

public class BufferedJoinStrategyInstance<LT, RT, KT, KT2> implements JoinStrategyInstance<LT,
    RT> {

  private final Scenario<LT, RT, KT, KT2> scenario;
  private final BufferedStrategyConfig config;
  private final UnmatchedYetRemnant leftRemnant;
  private final UnmatchedYetRemnant rightRemnant;

  public BufferedJoinStrategyInstance(
      Scenario<LT, RT, KT, KT2> scenario,
      BufferedStrategyConfig config) {
    this.scenario = scenario;
    this.config = config;
    leftRemnant = new UnmatchedYetRemnantImpl(config.getLeftConfig());
    rightRemnant = new UnmatchedYetRemnantImpl(config.getRightConfig());
    leftRemnant.setOther(rightRemnant);
    rightRemnant.setOther(leftRemnant);
  }

  @Override
  public double evalSuitability(Scenario scenario) {
    return 0;
  }

  @Override
  public Flowable<OptionalTuple<LT, RT>> join(Flowable<LT> leftFlowable,
      Flowable<RT> rightFlowable) {


  }
}
