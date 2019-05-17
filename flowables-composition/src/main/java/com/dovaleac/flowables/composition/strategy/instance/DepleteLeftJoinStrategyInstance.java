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

public class DepleteLeftJoinStrategyInstance<LT, RT, KT, K2T>
    implements JoinStrategyInstance<LT, RT> {

  private final Scenario<LT, RT, KT, K2T> scenario;
  private final Map<KT, LT> initialMap;
  private final BackpressureStrategy backpressureStrategy;

  public DepleteLeftJoinStrategyInstance(
      Scenario<LT, RT, KT, K2T> scenario,
      Map<KT, LT> initialMap,
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

    return leftFlowable
        .toMap(scenario.getLkFunction(), lt -> lt, () -> initialMap)
        .flatMapPublisher(
            innerMap ->
                Flowable.create(
                    (FlowableEmitter<OptionalTuple<LT, RT>> flowableEmitter) -> {
                      rightFlowable.subscribe(
                          rt -> {
                            KT kt = scenario.getRkFunction().apply(rt);
                            if (innerMap.containsKey(kt)) {
                              flowableEmitter.onNext(InnerJoinTuple.of(innerMap.get(kt), rt));
                              innerMap.remove(kt);
                            } else {
                              if (scenario.getJoinType().allowsLeftNulls()) {
                                flowableEmitter.onNext(OnlyRightTuple.of(rt));
                              }
                            }
                          },
                          flowableEmitter::onError,
                          () -> {
                            if (scenario.getJoinType().allowsRightNulls()) {
                              innerMap
                                  .values()
                                  .forEach(left -> flowableEmitter.onNext(OnlyLeftTuple.of(left)));
                            }
                            flowableEmitter.onComplete();
                          });
                    },
                    backpressureStrategy));
  }
}
