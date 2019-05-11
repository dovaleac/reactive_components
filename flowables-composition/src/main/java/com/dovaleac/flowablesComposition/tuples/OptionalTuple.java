package com.dovaleac.flowablesComposition.tuples;

public interface OptionalTuple<LT, RT> {

  <T> T acceptVisitor(TupleVisitor<LT, RT, T> visitor);
}
