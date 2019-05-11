package com.dovaleac.flowablesComposition.strategy.instance.helpers;

import com.dovaleac.flowablesComposition.strategy.instance.TuplePrinter;
import com.dovaleac.flowablesComposition.strategy.instance.domain.SmallDomainClass;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class CorrectnessCheckingUtils {

  public static  Path appendSuffixToPath(String pathSuffix, Path original) {
    return original.getParent()
        .resolve(original.getFileName().toString() + pathSuffix + ".csv");
  }

  public static  void checkFile(Path path1, Path path2,
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

  public static boolean sameContent(Path path1, Path path2) throws IOException {
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

  public static void checkCorrectness(Path pathWithoutJoinType, Path righteousPath,
      Flowable<? extends OptionalTuple<SmallDomainClass, SmallDomainClass>> joinedFlowable,
      String pathSuffix) throws IOException {


    Path realPath = CorrectnessCheckingUtils.appendSuffixToPath(pathSuffix, pathWithoutJoinType);
    Path realRighteousPath = CorrectnessCheckingUtils.appendSuffixToPath(pathSuffix, righteousPath);

    Files.write(realPath, "ID,LEFT,RIGHT\n".getBytes(), StandardOpenOption.CREATE,
        StandardOpenOption.WRITE);

    TuplePrinter tuplePrinter = new TuplePrinter();

    Completable.create((CompletableEmitter completableEmitter) ->
        joinedFlowable
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
}
