package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

//ET= element's type; OET = other element's type
public interface UnmatchedYetRemnant<T extends UnmatchedYetRemnant, ET, OET, KT> {
  void setOther(T other);
  Single<Map<KT, OET>> processRead(List<OET> otherTypeElements);
  Completable processWrite(Map<KT, ET> ownTypeElements);
  void notifyFlowableIsDepleted();
  void notifyOtherFlowableIsDepleted();
  boolean isInFlowableDepletedStatus();
}
