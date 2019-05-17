package com.dovaleac.flowables.composition.strategy.instance.buffered;

import com.dovaleac.flowables.composition.strategy.instance.BufferedJoinStrategyInstance;
import com.dovaleac.flowables.composition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
import com.dovaleac.flowables.composition.strategy.instance.buffered.guarder.SubscriberStatusGuarder;
import com.dovaleac.flowables.composition.strategy.instance.buffered.guarder.SubscriberStatusGuarderImpl;
import com.dovaleac.flowables.composition.strategy.instance.buffered.remnant.UnmatchedYetRemnant;
import com.dovaleac.flowables.composition.tuples.OptionalTuple;
import io.reactivex.Completable;
import io.reactivex.FlowableEmitter;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

public class BufferedJoinStrategySubscriber<T, OT, KT, K2T, LT, RT> implements Subscriber<List<T>> {

  private final UnmatchedYetRemnant<?, T, OT, KT, LT, RT> ownRemnant;
  private final UnmatchedYetRemnant<?, OT, T, KT, LT, RT> otherRemnant;
  private final BufferedJoinStrategyInstance<T, OT, KT, K2T> strategy;
  private final FlowableEmitter<OptionalTuple<LT, RT>> emitter;
  private Subscription subscription;
  private final SubscriberStatusGuarder<T> guarder = new SubscriberStatusGuarderImpl<>(this);
  private final boolean emitLeft;
  private final boolean emitRight;

  public BufferedJoinStrategySubscriber(
      UnmatchedYetRemnant<?, T, OT, KT, LT, RT> ownRemnant,
      UnmatchedYetRemnant<?, OT, T, KT, LT, RT> otherRemnant,
      BufferedJoinStrategyInstance<T, OT, KT, K2T> strategy,
      FlowableEmitter<OptionalTuple<LT, RT>> emitter,
      boolean emitLeft,
      boolean emitRight) {
    this.ownRemnant = ownRemnant;
    this.otherRemnant = otherRemnant;
    this.strategy = strategy;
    this.emitter = emitter;
    this.emitLeft = emitLeft;
    this.emitRight = emitRight;
  }

  @Override
  public void onSubscribe(Subscription subscription) {
    subscription.request(1);
    this.subscription = subscription;
  }

  @Override
  public void onNext(List<T> list) {
    otherRemnant
        .addToReadBuffer(list)
        .subscribe(
            this::requestNext,
            throwable -> {
              if (throwable instanceof ReadBufferNotAvailableForNewElementsException) {
                guarder.stopReading(list);
              }
            });
  }

  @Override
  public void onError(Throwable throwable) {
    emitter.onError(throwable);
  }

  @Override
  public void onComplete() {
    guarder.markAsDepleted();
  }

  private void requestNext() {
    subscription.request(1);
  }

  public void bothAreDepleted() {

    if (strategy == null) {
      return;
    }

    List<Completable> completables = new ArrayList<>(2);

    if (emitLeft) {
      completables.add(ownRemnant.emitAllElements());
    }
    if (emitRight) {
      completables.add(otherRemnant.emitAllElements());
    }

    Completable.merge(completables).subscribe(emitter::onComplete);
  }

  public SubscriberStatusGuarder<T> getGuarder() {
    return guarder;
  }
}
