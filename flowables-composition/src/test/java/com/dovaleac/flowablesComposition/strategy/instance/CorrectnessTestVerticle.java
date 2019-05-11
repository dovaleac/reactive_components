package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.FlowablesDbJoinFacade;
import com.dovaleac.flowablesComposition.PlannerConfig;
import com.dovaleac.flowablesComposition.scenario.Scenario;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.vertx.reactivex.core.AbstractVerticle;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class CorrectnessTestVerticle extends AbstractVerticle {

  public static final int COUNT = 100;
  protected final Function<Scenario<SmallTuple, SmallTuple, Long, ?>,
      JoinStrategyInstance<SmallTuple, SmallTuple>> strategyInstanceFunction;
  protected final PlannerConfig plannerConfig;
  protected final Path pathWithoutJoinType;
  protected final Path righteousPath = Path.of("src", "test", "resources", "right");
  private final FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep;
  private final String pathSuffix;

  public CorrectnessTestVerticle(
      Function<Scenario<SmallTuple, SmallTuple, Long, ?>, JoinStrategyInstance<SmallTuple, SmallTuple>> strategyInstanceFunction,
      PlannerConfig plannerConfig, Path pathWithoutJoinType,
      FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep, String pathSuffix) {
    this.strategyInstanceFunction = strategyInstanceFunction;
    this.plannerConfig = plannerConfig;
    this.pathWithoutJoinType = pathWithoutJoinType;
    this.initialStep = initialStep;
    this.pathSuffix = pathSuffix;
  }

  @Override
  public void start() throws Exception {
    super.start();
    Flowable<SmallTuple> leftFlowable = Flowable
        .range(0, COUNT)
        .map(id -> new SmallTuple(id, "l" + id));
    Flowable<SmallTuple> rightFlowable = Flowable
        .range(1, COUNT)
        .map(id -> new SmallTuple(id, "r" + id));

    FlowablesDbJoinFacade.PlannerConfigSpecifiedWith1KeyStep<SmallTuple, SmallTuple, Long, Object> scenario = initialStep
        .withLeftType(SmallTuple.class)
        .withRightType(SmallTuple.class)
        .withKeyType(Long.class)
        .withLeftKeyFunction(SmallTuple::getId)
        .withRightKeyFunction(SmallTuple::getId)
        .withPlannerConfig(plannerConfig);

    Path realPath = appendSuffixToPath(pathSuffix, pathWithoutJoinType);
    Path realRighteousPath = appendSuffixToPath(pathSuffix, righteousPath);

    Files.write(realPath, "ID,LEFT,RIGHT\n".getBytes(), StandardOpenOption.CREATE,
        StandardOpenOption.WRITE);

    TuplePrinter tuplePrinter = new TuplePrinter();

    Completable.create((CompletableEmitter completableEmitter) ->
        strategyInstanceFunction.apply(scenario)
            .join(leftFlowable, rightFlowable)
            .map(tuple -> tuple.acceptVisitor(tuplePrinter) + "\n")
            .sorted()
            .subscribe(
                s -> Files.write(realPath, s.getBytes(), StandardOpenOption.APPEND),
                Assertions::fail,
                () -> checkFile(realPath, realRighteousPath, completableEmitter)
            )
    ).test()
        .assertNoErrors();

  }


  private Path appendSuffixToPath(String pathSuffix, Path original) {
    return original.getParent()
        .resolve(original.getFileName().toString() + pathSuffix + ".csv");
  }

  private void checkFile(Path path1, Path path2,
      CompletableEmitter completableEmitter) throws IOException {
    System.out.println("starting the checking");
    boolean areEqual = sameContent(path1, path2);
    Files.deleteIfExists(path1);
    System.out.println("ended the checking");
    if (areEqual) {
      completableEmitter.onComplete();
    } else {
      completableEmitter.onError(new AssertionFailedError());
    }
  }

  boolean sameContent(Path path1, Path path2) throws IOException {
    final long size = Files.size(path1);
    if (size != Files.size(path2))
      return false;

    if (size < 4096)
      return Arrays.equals(Files.readAllBytes(path1), Files.readAllBytes(path2));

    try (InputStream is1 = Files.newInputStream(path1);
         InputStream is2 = Files.newInputStream(path2)) {

      int data;
      while ((data = is1.read()) != -1) {
        int rightPathRead = is2.read();
        if (data != rightPathRead) {
          return false;
        }
      }
    }

    return true;
  }

}
