package com.dovaleac.flowablesComposition.tuples;

import java.util.Optional;

public class OnlyLeftTuple<LT, RT> implements FullJoinTuple<LT, RT>, LeftJoinTuple<LT, RT> {

  private final LT left;

  public OnlyLeftTuple(LT left) {
    this.left = left;
  }

  public static <LT, RT> OnlyLeftTuple<LT, RT> of(LT left) {
    return new OnlyLeftTuple<>(left);
  }

  @Override
  public LT getLeft() {
    return left;
  }

  @Override
  public Optional<LT> getLeftOptional() {
    return Optional.of(left);
  }

  @Override
  public Optional<RT> getRightOptional() {
    return Optional.empty();
  }
}
