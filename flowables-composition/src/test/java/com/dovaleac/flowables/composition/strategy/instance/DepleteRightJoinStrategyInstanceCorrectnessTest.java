package com.dovaleac.flowables.composition.strategy.instance;

import com.dovaleac.flowables.composition.PlannerConfig;
import io.reactivex.BackpressureStrategy;

import java.nio.file.Path;
import java.util.HashMap;

public class DepleteRightJoinStrategyInstanceCorrectnessTest extends AbstractJoinStrategyInstanceCorrectnessTest {
  public DepleteRightJoinStrategyInstanceCorrectnessTest() {
    super(scenario -> new DepleteRightJoinStrategyInstance<>(scenario, new HashMap<>(CorrectnessTestVerticle.COUNT),
            BackpressureStrategy.BUFFER),
        PlannerConfig.NO_CONFIG,
        Path.of("src", "test", "resources", "expected"));
  }
}
