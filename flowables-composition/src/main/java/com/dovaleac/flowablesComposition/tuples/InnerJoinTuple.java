package com.dovaleac.flowablesComposition.tuples;

import java.util.Optional;

public class InnerJoinTuple<LT, RT>
    implements OptionalTuple<LT, RT>,
        FullJoinTuple<LT, RT>,
        LeftJoinTuple<LT, RT>,
        RightJoinTuple<LT, RT> {

  private final LT left;
  private final RT right;

  private InnerJoinTuple(LT left, RT right) {
    this.left = left;
    this.right = right;
  }

  public static <LT, RT> InnerJoinTuple<LT, RT> of(LT left, RT right) {
    return new InnerJoinTuple<>(left, right);
  }

  public LT getLeft() {
    return left;
  }

  @Override
  public RT getRight() {
    return right;
  }

  @Override
  public Optional<LT> getLeftOptional() {
    return Optional.of(left);
  }

  @Override
  public Optional<RT> getRightOptional() {
    return Optional.of(right);
  }

  @Override
  public <T> T acceptVisitor(TupleVisitor<LT, RT, T> visitor) {
    return visitor.visit(this);
  }
}
