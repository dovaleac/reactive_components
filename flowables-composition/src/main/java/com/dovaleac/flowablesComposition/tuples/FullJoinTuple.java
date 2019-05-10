package com.dovaleac.flowablesComposition.tuples;

import java.util.Optional;

public interface FullJoinTuple<LT, RT> extends OptionalTuple<LT, RT> {

  Optional<LT> getLeftOptional();
  Optional<RT> getRightOptional();
}
