package com.dovaleac.flowables.composition.strategy.instance.buffered.buffer;

import io.reactivex.Maybe;

import java.util.List;
import java.util.Map;

public interface ReadBuffer<T, KT> {
  boolean push(List<T> otherTypeElements);

  Maybe<Map<KT, T>> pull();

  double getCapacity();
}
