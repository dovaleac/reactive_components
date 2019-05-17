package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarder;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Completable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

public interface UnmatchedYetRemnant<UYRT extends UnmatchedYetRemnant<?, OT, T, KT, LT, RT>, T, OT,
    KT, LT, RT> {
  void setOther(UYRT other);
  void setGuarder(SubscriberStatusGuarder<T> guarder);
  Completable addToReadBuffer(List<OT> otherTypeElements);
  Completable addToWriteBuffer(Map<KT, T> ownTypeElements);
  Completable emitAllElements();
}
