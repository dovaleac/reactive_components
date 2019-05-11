package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.FlowablesDbJoinFacade;
import com.dovaleac.flowablesComposition.JoinType;
import com.dovaleac.flowablesComposition.PlannerConfig;
import com.dovaleac.flowablesComposition.scenario.Scenario;
import com.dovaleac.flowablesComposition.tuples.InnerJoinTuple;
import com.dovaleac.flowablesComposition.tuples.LeftJoinTuple;
import com.dovaleac.flowablesComposition.tuples.RightJoinTuple;
import com.dovaleac.flowablesComposition.tuples.TupleVisitor;
import io.reactivex.*;
import io.reactivex.functions.Function;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractJoinStrategyInstanceTest {

  public static final int COUNT = 1000;
  protected final Function<Scenario<SmallTuple, SmallTuple, Long, ?>,
      JoinStrategyInstance<SmallTuple, SmallTuple>> strategyInstanceFunction;
  protected final PlannerConfig plannerConfig;
  protected final Path pathWithoutJoinType;
  protected final Path righteousPath = Path.of("src", "test", "resources", "right");

  public AbstractJoinStrategyInstanceTest(
      Function<Scenario<SmallTuple, SmallTuple, Long, ?>,
          JoinStrategyInstance<SmallTuple, SmallTuple>> strategyInstanceFunction,
      PlannerConfig plannerConfig, Path pathWithoutJoinType) {
    this.strategyInstanceFunction = strategyInstanceFunction;
    this.plannerConfig = plannerConfig;
    this.pathWithoutJoinType = pathWithoutJoinType;
  }

  @ParameterizedTest
  @MethodSource("provideValues")
  void test(FlowablesDbJoinFacade.JoinTypeSpecifiedStep initialStep, String pathSuffix) throws Exception {
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
      // Compare byte-by-byte.
      // Note that this can be sped up drastically by reading large chunks
      // (e.g. 16 KBs) but care must be taken as InputStream.read(byte[])
      // does not neccessarily read a whole array!
      int data;
      while ((data = is1.read()) != -1)
        if (data != is2.read())
          return false;
    }

    return true;
  }

  private static Stream<Arguments> provideValues() {
    return Stream.of(
        Arguments.of(FlowablesDbJoinFacade.fullJoin(), "f"),
        Arguments.of(FlowablesDbJoinFacade.innerJoin(), "i"),
        Arguments.of(FlowablesDbJoinFacade.rightJoin(), "r"),
        Arguments.of(FlowablesDbJoinFacade.leftJoin(), "l")
    );
  }

  public class SmallTuple {
    private final long id;
    private final String value;

    public SmallTuple(long id, String value) {
      this.id = id;
      this.value = value;
    }

    public long getId() {
      return id;
    }

    public String getValue() {
      return value;
    }
  }

  public class TuplePrinter implements TupleVisitor<SmallTuple, SmallTuple, String> {


    public static final String DELIMITER = ",";

    @Override
    public String visit(InnerJoinTuple<SmallTuple, SmallTuple> inner) {
      return inner.getLeft().id + DELIMITER + inner.getLeft().value + DELIMITER + inner.getRight().value;
    }

    @Override
    public String visit(LeftJoinTuple<SmallTuple, SmallTuple> left) {
      return left.getLeft().id + DELIMITER + left.getLeft().value + DELIMITER ;
    }

    @Override
    public String visit(RightJoinTuple<SmallTuple, SmallTuple> right) {
      return right.getRight().id + DELIMITER + DELIMITER + right.getRight().value;
    }
  }
}