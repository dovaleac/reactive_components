package com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder;

import java.util.List;
import java.util.Map;

public interface SubscriberStatusGuarder<T, KT> {

  void stopWriting(Map<KT, T> elementToRetake);
  void stopReading(List<T> elementToRetake);
  void retakeReading();
  void retakeWriting();
  void bothAreDepleted();
}
