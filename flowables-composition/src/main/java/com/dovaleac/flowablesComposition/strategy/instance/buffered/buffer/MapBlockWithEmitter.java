package com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer;

import io.reactivex.SingleEmitter;

import java.util.Map;

public class MapBlockWithEmitter<KT, T> {
  private final Map<KT, T> otherTypeElements;
  private final SingleEmitter<Map<KT, T>> singleEmitter;

  public MapBlockWithEmitter(Map<KT, T> otherTypeElements,
      SingleEmitter<Map<KT, T>> singleEmitter) {
    this.otherTypeElements = otherTypeElements;
    this.singleEmitter = singleEmitter;
  }

  public Map<KT, T> getOtherTypeElements() {
    return otherTypeElements;
  }

  public SingleEmitter<Map<KT, T>> getSingleEmitter() {
    return singleEmitter;
  }
}
