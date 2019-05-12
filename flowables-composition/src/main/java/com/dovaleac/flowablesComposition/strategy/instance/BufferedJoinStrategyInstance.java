package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.BufferedStrategyConfig;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.WriteBufferNotAvailableForNewElementsException;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnant;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnantImpl;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;

import java.util.List;

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


  }

  //T = own type, OT = other type, KT = key type
   void subscribe(Flowable<List<T>> flowable,
      UnmatchedYetRemnant<?,T, OT, KT> ownRemnant,
      UnmatchedYetRemnant<?,OT, T, KT> otherRemnant) {
    flowable.subscribe(
        list -> otherRemnant.processRead(list)
            .subscribe(
                unMatched -> ownRemnant.processWrite(unMatched).subscribe(
                    () -> {},
                    throwable -> {
                      if (throwable instanceof WriteBufferNotAvailableForNewElementsException) {
                        stopEmittingUntilNewOrder(flowable);
                      }
                    }
                ),
                throwable -> {
                  if (throwable instanceof ReadBufferNotAvailableForNewElementsException) {
                    stopEmittingUntilNewOrder(flowable);
                  }
                }
            ),

    );
  }

  private <T> void stopEmittingUntilNewOrder(Flowable<List<T>> flowable) {

  }
}
