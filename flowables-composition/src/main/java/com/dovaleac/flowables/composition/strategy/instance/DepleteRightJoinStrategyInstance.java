package com.dovaleac.flowables.composition.strategy.instance;

import com.dovaleac.flowables.composition.scenario.Scenario;
import com.dovaleac.flowables.composition.tuples.InnerJoinTuple;
import com.dovaleac.flowables.composition.tuples.OnlyLeftTuple;
import com.dovaleac.flowables.composition.tuples.OnlyRightTuple;
import com.dovaleac.flowables.composition.tuples.OptionalTuple;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

import java.util.Map;

public class DepleteRightJoinStrategyInstance<LT, RT, KT, K2T>
    implements JoinStrategyInstance<LT, RT> {

  private final Scenario<LT, RT, KT, K2T> scenario;
  private final Map<KT, RT> initialMap;
  private final BackpressureStrategy backpressureStrategy;

  public DepleteRightJoinStrategyInstance(
      Scenario<LT, RT, KT, K2T> scenario,
      Map<KT, RT> initialMap,
      BackpressureStrategy backpressureStrategy) {
    this.scenario = scenario;
    this.initialMap = initialMap;
    this.backpressureStrategy = backpressureStrategy;
    if (!initialMap.isEmpty()) {
      throw new IllegalArgumentException("Inner map should be empty");
    }
  }

  @Override
  public double evalSuitability(Scenario scenario) {
    return 0;
  }

  @Override
  public Flowable<OptionalTuple<LT, RT>> join(
      Flowable<LT> leftFlowable, Flowable<RT> rightFlowable) {

    return rightFlowable
        .toMap(scenario.getRkFunction(), rt -> rt, () -> initialMap)
        .flatMapPublisher(
            innerMap ->
                Flowable.create(
                    (FlowableEmitter<OptionalTuple<LT, RT>> flowableEmitter) -> {
                      leftFlowable.subscribe(
                          lt -> {
                            KT kt = scenario.getLkFunction().apply(lt);
                            if (innerMap.containsKey(kt)) {
                              flowableEmitter.onNext(InnerJoinTuple.of(lt, innerMap.get(kt)));
                              innerMap.remove(kt);
                            } else {
                              if (scenario.getJoinType().allowsRightNulls()) {
                                flowableEmitter.onNext(OnlyLeftTuple.of(lt));
                              }
                            }
                          },
                          flowableEmitter::onError,
                          () -> {
                            if (scenario.getJoinType().allowsLeftNulls()) {
                              innerMap
                                  .values()
                                  .forEach(
                                      right -> flowableEmitter.onNext(OnlyRightTuple.of(right)));
                            }
                            flowableEmitter.onComplete();
                          });
                    },
                    backpressureStrategy));
  }
}
