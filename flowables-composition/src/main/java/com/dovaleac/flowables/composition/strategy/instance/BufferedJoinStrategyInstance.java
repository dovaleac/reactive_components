package com.dovaleac.flowables.composition.strategy.instance;

import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.strategy.instance.buffered.BufferedJoinStrategySubscriber;
import com.dovaleac.flowables.composition.strategy.instance.buffered.BufferedStrategyConfig;
import com.dovaleac.flowables.composition.strategy.instance.buffered.remnant.UnmatchedYetRemnantImpl;
import com.dovaleac.flowables.composition.tuples.OptionalTuple;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.util.List;

public class BufferedJoinStrategyInstance<LT, RT, KT, K2T> implements JoinStrategyInstance<LT, RT> {

  private final Scenario<LT, RT, KT, K2T> scenario;
  private final BufferedStrategyConfig config;
  private BackpressureStrategy backPressure;

  public BufferedJoinStrategyInstance(
      Scenario<LT, RT, KT, K2T> scenario, BufferedStrategyConfig config) {
    this.scenario = scenario;
    this.config = config;
  }

  @Override
  public double evalSuitability(Scenario scenario) {
    return 0;
  }

  @Override
  public Flowable<OptionalTuple<LT, RT>> join(
      Flowable<LT> leftFlowable, Flowable<RT> rightFlowable) {

    Flowable<List<LT>> leftBufferedFlowable = leftFlowable.buffer(config.getLeftFlowableBuffer());
    Flowable<List<RT>> rightBufferedFlowable =
        rightFlowable.buffer(config.getRightFlowableBuffer());

    return Flowable.create(
        flowableEmitter -> {
          UnmatchedYetRemnantImpl<LT, RT, KT, LT, RT> leftRemnant;
          UnmatchedYetRemnantImpl<RT, LT, KT, LT, RT> rightRemnant;
          leftRemnant =
              new UnmatchedYetRemnantImpl<>(
                  config.getLeftRemnantInitialMap(),
                  config.getLeftRemnantConfig(),
                  scenario.getLkFunction(),
                  -1,
                  true,
                  flowableEmitter);
          rightRemnant =
              new UnmatchedYetRemnantImpl<>(
                  config.getRightRemnantInitialMap(),
                  config.getRightRemnantConfig(),
                  scenario.getRkFunction(),
                  -1,
                  false,
                  flowableEmitter);
          leftRemnant.setOther(rightRemnant);
          rightRemnant.setOther(leftRemnant);
          BufferedJoinStrategySubscriber<LT, RT, KT, K2T, LT, RT> leftSubscriber =
              new BufferedJoinStrategySubscriber<>(
                  leftRemnant,
                  rightRemnant,
                  this,
                  flowableEmitter,
                  scenario.getJoinType().allowsRightNulls(),
                  scenario.getJoinType().allowsLeftNulls());
          BufferedJoinStrategySubscriber<RT, LT, KT, K2T, LT, RT> rightSubscriber =
              new BufferedJoinStrategySubscriber<>(
                  rightRemnant,
                  leftRemnant,
                  null,
                  flowableEmitter,
                  scenario.getJoinType().allowsRightNulls(),
                  scenario.getJoinType().allowsLeftNulls());
          leftRemnant.setGuarder(leftSubscriber.getGuarder());
          rightRemnant.setGuarder(rightSubscriber.getGuarder());
          leftBufferedFlowable.subscribe(leftSubscriber);
          rightBufferedFlowable.subscribe(rightSubscriber);
        },
        backPressure);
  }
}
