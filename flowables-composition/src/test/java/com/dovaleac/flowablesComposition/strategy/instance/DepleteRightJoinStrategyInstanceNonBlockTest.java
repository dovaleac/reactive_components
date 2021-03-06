package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.PlannerConfig;
import io.reactivex.BackpressureStrategy;

import java.nio.file.Path;
import java.util.HashMap;

public class DepleteRightJoinStrategyInstanceNonBlockTest extends AbstractJoinStrategyInstanceNonBlockTest {
  public DepleteRightJoinStrategyInstanceNonBlockTest() {
    super(scenario -> new DepleteRightJoinStrategyInstance<>(scenario, new HashMap<>(CorrectnessTestVerticle.COUNT),
            BackpressureStrategy.BUFFER),
        PlannerConfig.NO_CONFIG,
        Path.of("src", "test", "resources", "expected"));
  }
}
