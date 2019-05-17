package com.dovaleac.flowablesComposition.strategy.instance.buffered.buffer;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.functions.Function;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ReadBufferImpl<T, KT> implements ReadBuffer<T, KT> {

  private final Function<T, KT> keyFunction;
  private final BlockingQueue<List<T>> blockQueue;
  private final double maxBlocks;

  public ReadBufferImpl(Function<T, KT> keyFunction, int maxBlocks) {
    this.keyFunction = keyFunction;
    this.blockQueue = new LinkedBlockingDeque<>(maxBlocks);
    this.maxBlocks = (double) maxBlocks;
  }

  @Override
  public boolean push(List<T> otherTypeElements) {
    return blockQueue.offer(otherTypeElements);
  }

  @Override
  public double getCapacity() {
    return (double) blockQueue.remainingCapacity() / maxBlocks;
  }

  @Override
  public Maybe<Map<KT, T>> pull() {
    List<T> polled = blockQueue.poll();
    if (polled == null) {
      return Maybe.empty();
    }
    return Flowable.fromIterable(polled).toMap(keyFunction).toMaybe();
  }
}
