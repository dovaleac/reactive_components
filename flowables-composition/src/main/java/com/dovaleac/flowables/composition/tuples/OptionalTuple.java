package com.dovaleac.flowables.composition.tuples;

public interface OptionalTuple<LT, RT> {

  <T> T acceptVisitor(TupleVisitor<LT, RT, T> visitor);
}
