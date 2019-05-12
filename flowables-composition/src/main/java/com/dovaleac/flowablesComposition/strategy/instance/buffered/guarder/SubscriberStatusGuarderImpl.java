package com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.BufferedJoinStrategySubscriber;

import java.util.List;
import java.util.Map;

public class SubscriberStatusGuarderImpl<T, OT, KT> implements SubscriberStatusGuarder<T, OT, KT> {

  private final BufferedJoinStrategySubscriber<T, OT, KT> subscriber;

  public SubscriberStatusGuarderImpl(
      BufferedJoinStrategySubscriber<T, OT, KT> subscriber) {
    this.subscriber = subscriber;
  }

  @Override
  public void stopWriting(Map<KT, T> elementToRetake) {

  }

  @Override
  public void stopReading(List<OT> elementToRetake) {

  }

  @Override
  public void retakeReading(List<OT> elementToRetake) {

  }

  @Override
  public void retakeWriting(Map<KT, T> elementToRetake) {

  }

  @Override
  public void bothAreDepleted() {

  }
}
