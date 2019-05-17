package com.dovaleac.flowables.composition.strategy.instance.buffered.guarder;

import com.dovaleac.flowables.composition.strategy.instance.buffered.BufferedJoinStrategySubscriber;
import com.github.oxo42.stateless4j.StateMachine;

import java.util.List;

public class SubscriberStatusGuarderImpl<T, OT, KT, K2T, LT, RT>
    implements SubscriberStatusGuarder<T> {

  private final BufferedJoinStrategySubscriber<T, OT, KT, K2T, LT, RT> subscriber;
  private final StateMachine<SubscriberStatusGuarderState, SubscriberStatusGuarderTrigger>
      stateMachine =
          new StateMachine<>(
              SubscriberStatusGuarderState.RUNNING,
              new SubscriberStatusGuarderStateMachine<>(this).getConfig());

  public SubscriberStatusGuarderImpl(
      BufferedJoinStrategySubscriber<T, OT, KT, K2T, LT, RT> subscriber) {
    this.subscriber = subscriber;
  }

  private List<T> listToRetake;

  @Override
  public void stopReading(List<T> elementToRetake) {
    listToRetake = elementToRetake;
  }

  @Override
  public void retakeReading() {
    subscriber.onNext(listToRetake);
  }

  @Override
  public void bothAreDepleted() {
    subscriber.bothAreDepleted();
  }

  @Override
  public void markAsDepleted() {
    stateMachine.fire(SubscriberStatusGuarderTrigger.MARK_AS_DEPLETED);
  }

  @Override
  public void notifyOtherIsDepleted() {
    stateMachine.fire(SubscriberStatusGuarderTrigger.NOTIFY_OTHER_IS_DEPLETED);
  }
}
