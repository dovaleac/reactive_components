package com.dovaleac.flowables.composition.strategy.instance.buffered.guarder;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;

import java.util.List;

public class SubscriberStatusGuarderStateMachine<T, OT, KT> {

  private final SubscriberStatusGuarderImpl<T, OT, KT, ?, ?, ?> subscriberStatusGuarder;

  public SubscriberStatusGuarderStateMachine(SubscriberStatusGuarderImpl subscriberStatusGuarder) {
    this.subscriberStatusGuarder = subscriberStatusGuarder;
  }

  StateMachineConfig<SubscriberStatusGuarderState, SubscriberStatusGuarderTrigger> getConfig() {
    StateMachineConfig<SubscriberStatusGuarderState, SubscriberStatusGuarderTrigger> config =
        new StateMachineConfig<>();

    config
        .configure(SubscriberStatusGuarderState.RUNNING)
        .permit(
            SubscriberStatusGuarderTrigger.STOP_ON_READING,
            SubscriberStatusGuarderState.STOPPED_ON_READING)
        .permit(
            SubscriberStatusGuarderTrigger.MARK_AS_DEPLETED, SubscriberStatusGuarderState.DEPLETED)
        .permit(
            SubscriberStatusGuarderTrigger.NOTIFY_OTHER_IS_DEPLETED,
            SubscriberStatusGuarderState.OTHER_IS_DEPLETED)
        .onEntryFrom(
            SubscriberStatusGuarderTrigger.RETAKE_READING, subscriberStatusGuarder::retakeReading);

    config
        .configure(SubscriberStatusGuarderState.STOPPED_ON_READING)
        .permit(SubscriberStatusGuarderTrigger.RETAKE_READING, SubscriberStatusGuarderState.RUNNING)
        .onEntryFrom(
            new TriggerWithParameters1<>(
                SubscriberStatusGuarderTrigger.STOP_ON_READING, List.class),
            list -> {
              List<T> ts = (List<T>) list;
              subscriberStatusGuarder.stopReading(ts);
            },
            List.class);

    config
        .configure(SubscriberStatusGuarderState.DEPLETED)
        .permit(
            SubscriberStatusGuarderTrigger.NOTIFY_OTHER_IS_DEPLETED,
            SubscriberStatusGuarderState.BOTH_ARE_DEPLETED);

    config
        .configure(SubscriberStatusGuarderState.OTHER_IS_DEPLETED)
        .permit(
            SubscriberStatusGuarderTrigger.MARK_AS_DEPLETED,
            SubscriberStatusGuarderState.BOTH_ARE_DEPLETED);

    config
        .configure(SubscriberStatusGuarderState.DEPLETED)
        .onEntry(subscriberStatusGuarder::bothAreDepleted);

    return config;
  }
}
