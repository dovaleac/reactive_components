package com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder;

import java.util.List;
import java.util.Map;

public interface SubscriberStatusGuarder<T> {

  void stopReading(List<T> elementToRetake);

  void retakeReading();

  void bothAreDepleted();

  void markAsDepleted();

  void notifyOtherIsDepleted();
}
