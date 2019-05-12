package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.BufferedJoinStrategySubscriber;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.BufferedStrategyConfig;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnant;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnantImpl;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.util.List;

public class BufferedJoinStrategyInstance<LT, RT, KT, KT2> implements JoinStrategyInstance<LT,
    RT> {

  private final Scenario<LT, RT, KT, KT2> scenario;
  private final BufferedStrategyConfig config;
  private final UnmatchedYetRemnant leftRemnant;
  private final UnmatchedYetRemnant rightRemnant;
  private BackpressureStrategy backPressure;

  public BufferedJoinStrategyInstance(
      Scenario<LT, RT, KT, KT2> scenario,
      BufferedStrategyConfig config) {
    this.scenario = scenario;
    this.config = config;
    leftRemnant = new UnmatchedYetRemnantImpl(config.getLeftRemnantConfig());
    rightRemnant = new UnmatchedYetRemnantImpl(config.getRightRemnantConfig());
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

    Flowable<List<LT>> leftBufferedFlowable = leftFlowable.buffer(config.getLeftFlowableBuffer());
    Flowable<List<RT>> rightBufferedFlowable = rightFlowable.buffer(config.getRightFlowableBuffer());

    return Flowable.create(flowableEmitter -> {
      BufferedJoinStrategySubscriber<LT, RT, KT, KT2, LT, RT> leftSubscriber =
          new BufferedJoinStrategySubscriber<>(leftRemnant, rightRemnant, this, flowableEmitter, emitLeft, emitRight);
      BufferedJoinStrategySubscriber<RT, LT, KT, KT2, LT, RT> rightSubscriber =
          new BufferedJoinStrategySubscriber<>(rightRemnant, leftRemnant, null, flowableEmitter, emitLeft, emitRight);

    }, backPressure);


  }

  private <T> void stopEmittingUntilNewOrder(Flowable<List<T>> flowable) {

  }

  public void complete() {

  }
}
