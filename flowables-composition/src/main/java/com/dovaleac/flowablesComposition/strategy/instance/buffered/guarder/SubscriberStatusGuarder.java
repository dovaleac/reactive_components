package com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder;

import java.util.List;
import java.util.Map;

public interface SubscriberStatusGuarder<T, OT, KT> {

  void stopWriting(Map<KT, T> elementToRetake);
  void stopReading(List<OT> elementToRetake);
  void retakeReading(List<OT> elementToRetake);
  void retakeWriting(Map<KT, T> elementToRetake);
  void bothAreDepleted();
}
