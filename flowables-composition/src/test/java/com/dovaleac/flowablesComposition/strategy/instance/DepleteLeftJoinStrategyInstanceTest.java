package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.PlannerConfig;
import io.reactivex.BackpressureStrategy;

import java.nio.file.Path;
import java.util.HashMap;

public class DepleteLeftJoinStrategyInstanceTest extends AbstractJoinStrategyInstanceTest {
  public DepleteLeftJoinStrategyInstanceTest() {
    super(scenario -> new DepleteLeftJoinStrategyInstance<>(scenario, new HashMap<>(),
            BackpressureStrategy.BUFFER),
        PlannerConfig.NO_CONFIG,
        Path.of("src", "test", "resources", "expected"));
  }
}
