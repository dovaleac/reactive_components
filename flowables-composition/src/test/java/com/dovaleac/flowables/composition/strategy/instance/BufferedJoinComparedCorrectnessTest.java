package com.dovaleac.flowables.composition.strategy.instance;

import com.dovaleac.flowables.composition.PlannerConfig;
import com.dovaleac.flowables.composition.strategy.instance.buffered.BufferedStrategyConfig;
import com.dovaleac.flowables.composition.strategy.instance.buffered.capacity.LeverageBufferCapacitiesStrategyImpl;
import com.dovaleac.flowables.composition.strategy.instance.buffered.remnant.UnmatchedYetRemnantConfig;
import com.dovaleac.flowables.composition.strategy.instance.domain.SmallDomainClass;

import java.util.HashMap;

public class BufferedJoinComparedCorrectnessTest extends AbstractComparedCorrectnessTest {

  public static final LeverageBufferCapacitiesStrategyImpl LEVERAGE_BUFFER_CAPACITIES_STRATEGY = new LeverageBufferCapacitiesStrategyImpl(0.95, 0.5, 1.0);
  public static final UnmatchedYetRemnantConfig<Long, SmallDomainClass> UNMATCHED_YET_REMNANT_CONFIG =
    new UnmatchedYetRemnantConfig<>(
      5,
      40,
        6, new HashMap<>(),
  LEVERAGE_BUFFER_CAPACITIES_STRATEGY);
  public static final int FLOWABLE_BUFFER = 10;


  public BufferedJoinComparedCorrectnessTest() {
    super(scenario -> new BufferedJoinStrategyInstance<>(scenario, new BufferedStrategyConfig<>(
        UNMATCHED_YET_REMNANT_CONFIG,
        UNMATCHED_YET_REMNANT_CONFIG,
            FLOWABLE_BUFFER,
            FLOWABLE_BUFFER,
            new HashMap<>(),
            new HashMap<>()
        )), PlannerConfig.NO_CONFIG
        );
  }
}
