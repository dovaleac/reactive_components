package com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.BufferedJoinStrategySubscriber;

import java.util.List;
import java.util.Map;

public class SubscriberStatusGuarderImpl<T, OT, KT> implements SubscriberStatusGuarder<T, KT> {

  private final BufferedJoinStrategySubscriber<T, OT, KT> subscriber;

  public SubscriberStatusGuarderImpl(
      BufferedJoinStrategySubscriber<T, OT, KT> subscriber) {
    this.subscriber = subscriber;
  }

  private Map<KT, T> mapToRetake;
  private List<T> listToRetake;

  @Override
  public void stopWriting(Map<KT, T> elementToRetake) {
    this.mapToRetake = elementToRetake;
  }

  @Override
  public void stopReading(List<T> elementToRetake) {
    listToRetake = elementToRetake;
  }

  @Override
  public void retakeReading() {
    subscriber.onNext(listToRetake);
  }

  @Override
  public void retakeWriting() {
    subscriber.processWriting(mapToRetake);
  }

  @Override
  public void bothAreDepleted() {

  }
}
