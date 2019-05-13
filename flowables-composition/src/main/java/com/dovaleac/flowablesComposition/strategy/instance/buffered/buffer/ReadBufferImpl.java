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
  private final BlockingQueue<BlockWithEmitter<KT, T>> blockQueue;
  private final double maxBlocks;

  public ReadBufferImpl(Function<T, KT> keyFunction, int maxBlocks) {
    this.keyFunction = keyFunction;
    this.blockQueue = new LinkedBlockingDeque<>(maxBlocks);
    this.maxBlocks = (double) maxBlocks;
  }

  @Override
  public void push(List<T> otherTypeElements,
      SingleEmitter<Map<KT, T>> singleEmitter) throws ReadBufferNotAvailableForNewElementsException {
    if (!blockQueue.offer(new BlockWithEmitter<>(otherTypeElements, singleEmitter, keyFunction))) {
      throw new ReadBufferNotAvailableForNewElementsException();
    }
  }

  @Override
  public double getCapacity() throws ReadBufferNotAvailableForNewElementsException {
    return (double) blockQueue.remainingCapacity() / maxBlocks;
  }

  @Override
  public Maybe<MapBlockWithEmitter<KT, T>> pull() {
    BlockWithEmitter<KT, T> polled = blockQueue.poll();
    if (polled == null) {
      return Maybe.empty();
    }
    return polled.toMap().toMaybe();
  }



  public class BlockWithEmitter<KT, T> {
    private final List<T> otherTypeElements;
    private final SingleEmitter<Map<KT, T>> singleEmitter;
    private final Function<T, KT> function;

    public BlockWithEmitter(List<T> otherTypeElements,
        SingleEmitter<Map<KT, T>> singleEmitter, Function<T, KT> function) {
      this.otherTypeElements = otherTypeElements;
      this.singleEmitter = singleEmitter;
      this.function = function;
    }

    public Single<MapBlockWithEmitter<KT, T>> toMap() {
      return Flowable.fromIterable(otherTypeElements)
          .toMap(function)
          .map(kttMap -> new MapBlockWithEmitter<>(kttMap, singleEmitter));

    }
  }
}
