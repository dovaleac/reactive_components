package com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action1;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;

import java.util.List;
import java.util.Map;

import static com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarderState.*;
import static com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarderTrigger.*;

public class SubscriberStatusGuarderStateMachine<T, OT, KT> {

  private final SubscriberStatusGuarderImpl<T, OT, KT, ?, ?, ?> subscriberStatusGuarder;

  public SubscriberStatusGuarderStateMachine(SubscriberStatusGuarderImpl subscriberStatusGuarder) {
    this.subscriberStatusGuarder = subscriberStatusGuarder;
  }

  StateMachineConfig<SubscriberStatusGuarderState, SubscriberStatusGuarderTrigger> getConfig() {
    StateMachineConfig<SubscriberStatusGuarderState, SubscriberStatusGuarderTrigger> config =
        new StateMachineConfig<>();

    config
        .configure(RUNNING)
        .permit(STOP_ON_READING, STOPPED_ON_READING)
        .permit(MARK_AS_DEPLETED, DEPLETED)
        .permit(NOTIFY_OTHER_IS_DEPLETED, OTHER_IS_DEPLETED)
        .onEntryFrom(RETAKE_READING, subscriberStatusGuarder::retakeReading);

    config
        .configure(STOPPED_ON_READING)
        .permit(RETAKE_READING, RUNNING)
        .onEntryFrom(
            new TriggerWithParameters1<>(STOP_ON_READING, List.class),
            list -> {
              List<T> tList = (List<T>) list;
              subscriberStatusGuarder.stopReading(tList);
            },
            List.class);

    config.configure(DEPLETED).permit(NOTIFY_OTHER_IS_DEPLETED, BOTH_ARE_DEPLETED);

    config.configure(OTHER_IS_DEPLETED).permit(MARK_AS_DEPLETED, BOTH_ARE_DEPLETED);

    config.configure(DEPLETED).onEntry(subscriberStatusGuarder::bothAreDepleted);

    return config;
  }
}
