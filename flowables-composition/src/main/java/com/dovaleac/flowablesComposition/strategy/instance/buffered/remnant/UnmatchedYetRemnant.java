package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Completable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

public interface UnmatchedYetRemnant<UYRT extends UnmatchedYetRemnant, T, OT, KT, LT, RT> {
  void setOther(UYRT other);
  Completable addToReadBuffer(List<OT> otherTypeElements);
  Completable addToWriteBuffer(Map<KT, T> ownTypeElements);
  Completable emitAllElements();
}
