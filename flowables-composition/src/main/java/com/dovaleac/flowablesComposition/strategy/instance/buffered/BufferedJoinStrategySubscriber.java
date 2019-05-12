package com.dovaleac.flowablesComposition.strategy.instance.buffered;

import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.ReadBufferNotAvailableForNewElementsException;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.exceptions.WriteBufferNotAvailableForNewElementsException;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarder;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarderImpl;
import com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant.UnmatchedYetRemnant;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Map;

public class BufferedJoinStrategySubscriber<T, OT, KT> implements Subscriber<List<T>> {

  private final UnmatchedYetRemnant<?,T, OT, KT> ownRemnant;
  private final UnmatchedYetRemnant<?,OT, T, KT> otherRemnant;
  private Subscription subscription;
  private List<T> lastElementToRetake;
  private final SubscriberStatusGuarder<T, KT> guarder =
      new SubscriberStatusGuarderImpl<>(this);

  public BufferedJoinStrategySubscriber(
      UnmatchedYetRemnant<?, T, OT, KT> ownRemnant,
      UnmatchedYetRemnant<?, OT, T, KT> otherRemnant) {
    this.ownRemnant = ownRemnant;
    this.otherRemnant = otherRemnant;
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

}
