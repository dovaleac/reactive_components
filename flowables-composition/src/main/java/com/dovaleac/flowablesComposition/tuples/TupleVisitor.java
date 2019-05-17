package com.dovaleac.flowablesComposition.tuples;

public interface TupleVisitor<LT, RT, T> {

  T visit(InnerJoinTuple<LT, RT> inner);

  T visit(LeftJoinTuple<LT, RT> left);

  T visit(RightJoinTuple<LT, RT> right);
}
