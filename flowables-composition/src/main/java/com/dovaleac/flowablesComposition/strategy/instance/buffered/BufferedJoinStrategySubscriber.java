package com.dovaleac.flowablesComposition.strategy.instance.buffered;

import com.dovaleac.flowablesComposition.strategy.instance.BufferedJoinStrategyInstance;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.WriteBufferNotAvailableForNewElementsException;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarder;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarderImpl;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnant;
import com.dovaleac.flowablesComposition.tuples.OptionalTuple;
import io.reactivex.Completable;
import io.reactivex.FlowableEmitter;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BufferedJoinStrategySubscriber<T, OT, KT, KT2, LT, RT> implements Subscriber<List<T>> {

  private final UnmatchedYetRemnant<?,T, OT, KT, LT, RT> ownRemnant;
  private final UnmatchedYetRemnant<?,OT, T, KT, LT, RT> otherRemnant;
  private final BufferedJoinStrategyInstance<T, OT, KT, KT2> strategy;
  private final FlowableEmitter<OptionalTuple<LT, RT>> emitter;
  private Subscription subscription;
  private List<T> lastElementToRetake;
  private final SubscriberStatusGuarder<T, KT> guarder =
      new SubscriberStatusGuarderImpl<>(this);
  private final boolean emitLeft;
  private final boolean emitRight;

  public BufferedJoinStrategySubscriber(
      UnmatchedYetRemnant<?, T, OT, KT, LT, RT> ownRemnant,
      UnmatchedYetRemnant<?, OT, T, KT, LT, RT> otherRemnant,
      BufferedJoinStrategyInstance<T, OT, KT, KT2> strategy,
      FlowableEmitter<OptionalTuple<LT, RT>> emitter, boolean emitLeft, boolean emitRight) {
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
    otherRemnant.processRead(list)
        .subscribe(
            this::processWriting,
            throwable -> {
              if (throwable instanceof ReadBufferNotAvailableForNewElementsException) {
                guarder.stopReading(list);
              }
            }
        );
  }

  public void processWriting(Map<KT, T> unMatched) {
    ownRemnant.processWrite(unMatched).subscribe(
        this::requestNext,
        throwable -> {
          if (throwable instanceof WriteBufferNotAvailableForNewElementsException) {
            guarder.stopWriting(unMatched);
          }
        }
    );
  }

  //TODO: all this retaking has to be performed differently if it stopped on writing or on reading
  void restartEmitting() {
    List<T> elementToRetake = lastElementToRetake;
    if (elementToRetake == null) {
      requestNext();
    } else {
      lastElementToRetake = null;
      onNext(elementToRetake);
    }
  }

  @Override
  public void onError(Throwable throwable) {

  }

  @Override
  public void onComplete() {
    ownRemnant.notifyFlowableIsDepleted();
    otherRemnant.notifyOtherFlowableIsDepleted();
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
      completables.add(ownRemnant.emitAllElements(emitter));
    }
    if (emitRight) {
      completables.add(ownRemnant.emitAllElements(emitter));
    }

    Completable.merge(completables).subscribe(emitter::onComplete);
  }
}
