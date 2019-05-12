package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Completable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

//ET= element's type; OET = other element's type
public interface UnmatchedYetRemnant<T extends UnmatchedYetRemnant, ET, OET, KT, LT, RT> {
  void setOther(T other);
  Single<Map<KT, OET>> processRead(List<OET> otherTypeElements);
  Completable processWrite(Map<KT, ET> ownTypeElements);
  void notifyFlowableIsDepleted();
  void notifyOtherFlowableIsDepleted();
  Completable emitAllElements(FlowableEmitter<OptionalTuple<LT,RT>> emitter);
}
