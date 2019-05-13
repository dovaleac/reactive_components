package com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
import io.reactivex.Maybe;
import io.reactivex.SingleEmitter;

import java.util.List;
import java.util.Map;

public interface ReadBuffer<T, KT> {

  void push(List<T> otherTypeElements,
      SingleEmitter<Map<KT, T>> singleEmitter) throws ReadBufferNotAvailableForNewElementsException;


  double getCapacity() throws ReadBufferNotAvailableForNewElementsException;

  Maybe<MapBlockWithEmitter<KT, T>> pull();
}
