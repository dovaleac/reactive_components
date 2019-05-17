package com.dovaleac.flowablesComposition.tuples;

import java.util.Optional;

public interface RightJoinTuple<LT, RT> extends OptionalTuple<LT, RT> {

  Optional<LT> getLeftOptional();

  RT getRight();
}
