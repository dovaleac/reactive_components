package com.dovaleac.flowablesComposition.tuples;

import java.util.Optional;

public class OnlyRightTuple<LT, RT> implements FullJoinTuple<LT, RT>, RightJoinTuple<LT, RT> {

  private final RT right;

  public OnlyRightTuple(RT right) {
    this.right = right;
  }

  public static <LT, RT> OnlyRightTuple<LT, RT> of(RT left) {
    return new OnlyRightTuple<>(left);
  }

  @Override
  public RT getRight() {
    return right;
  }

  @Override
  public Optional<LT> getLeftOptional() {
    return Optional.empty();
  }

  @Override
  public Optional<RT> getRightOptional() {
    return Optional.of(right);
  }
}
