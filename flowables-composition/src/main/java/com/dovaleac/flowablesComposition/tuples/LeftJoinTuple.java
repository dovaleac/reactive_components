package com.dovaleac.flowablesComposition.tuples;

import java.util.Optional;

public interface LeftJoinTuple<LT, RT> extends OptionalTuple<LT, RT> {

  LT getLeft();
  Optional<RT> getRightOptional();
}
