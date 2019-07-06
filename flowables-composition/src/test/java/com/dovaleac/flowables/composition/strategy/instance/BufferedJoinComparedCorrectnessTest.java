package com.dovaleac.flowables.composition.strategy.instance;

import com.dovaleac.flowables.composition.PlannerConfig;
import com.dovaleac.flowables.composition.eventlog.BuffersStatus;
import com.dovaleac.flowables.composition.eventlog.EventManagerImpl;
import com.dovaleac.flowables.composition.eventlog.action.EventActionFile;
import com.dovaleac.flowables.composition.eventlog.action.EventActionFileWithBuffersStatus;
import com.dovaleac.flowables.composition.strategy.instance.buffered.BufferedStrategyConfig;
import com.dovaleac.flowables.composition.strategy.instance.buffered.capacity.LeverageBufferCapacitiesStrategyImpl;
import com.dovaleac.flowables.composition.strategy.instance.buffered.remnant.UnmatchedYetRemnantConfig;
import com.dovaleac.flowables.composition.strategy.instance.domain.SmallDomainClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class BufferedJoinComparedCorrectnessTest extends AbstractComparedCorrectnessTest {

  public static final LeverageBufferCapacitiesStrategyImpl LEVERAGE_BUFFER_CAPACITIES_STRATEGY =
      new LeverageBufferCapacitiesStrategyImpl(0.05, 0.5, 0.0);
  public static final UnmatchedYetRemnantConfig<Long, SmallDomainClass>
      UNMATCHED_YET_REMNANT_CONFIG =
          new UnmatchedYetRemnantConfig<>(
              5, 40, 6, new HashMap<>(), LEVERAGE_BUFFER_CAPACITIES_STRATEGY);
  public static final int FLOWABLE_BUFFER = 10;
  public static final Path PATH_TO_LOG_FILE = Paths.get(
      "src", "test", "resources", "BufferedJoinComparedCorrectnessTest.txt");

  public BufferedJoinComparedCorrectnessTest() {
    super(
        scenario ->
            new BufferedJoinStrategyInstance<>(
                scenario,
                new BufferedStrategyConfig<>(
                    UNMATCHED_YET_REMNANT_CONFIG,
                    UNMATCHED_YET_REMNANT_CONFIG,
                    FLOWABLE_BUFFER,
                    FLOWABLE_BUFFER,
                    new HashMap<>(),
                    new HashMap<>())),
        PlannerConfig.NO_CONFIG,
        new EventManagerImpl(
            List.of(
                new EventActionFileWithBuffersStatus<>(
                    PATH_TO_LOG_FILE, new BuffersStatus<>()))));
    try {
      Files.write(PATH_TO_LOG_FILE, "".getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
