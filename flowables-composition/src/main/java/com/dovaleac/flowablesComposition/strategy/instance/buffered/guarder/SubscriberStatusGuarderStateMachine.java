package com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action1;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;

import java.util.List;
import java.util.Map;

import static com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarderState.*;
import static com.dovaleac.flowablesComposition.strategy.instance.buffered.guarder.SubscriberStatusGuarderTrigger.*;

public class SubscriberStatusGuarderStateMachine<T, OT, KT> {

  private final SubscriberStatusGuarderImpl<T, OT, KT> subscriberStatusGuarder;

  public SubscriberStatusGuarderStateMachine(
      SubscriberStatusGuarderImpl subscriberStatusGuarder) {
    this.subscriberStatusGuarder = subscriberStatusGuarder;
  }

  StateMachineConfig<SubscriberStatusGuarderState, SubscriberStatusGuarderTrigger> getConfig() {
    StateMachineConfig<SubscriberStatusGuarderState, SubscriberStatusGuarderTrigger> config = new StateMachineConfig<>();

    config.configure(RUNNING)
        .permit(STOP_ON_READING, STOPPED_ON_READING)
        .permit(STOP_ON_WRITING, STOPPED_ON_WRITING)
        .permit(MARK_AS_DEPLETED, DEPLETED)
        .permit(NOTIFY_OTHER_IS_DEPLETED, OTHER_IS_DEPLETED)
        .onEntryFrom(new TriggerWithParameters1<>(RETAKE_READING, List.class),
            list -> {
              List<OT> otList = (List<OT>) list;
              subscriberStatusGuarder.retakeReading(otList);
            }, List.class)
        .onEntryFrom(new TriggerWithParameters1<>(RETAKE_WRITING, Map.class),
        map -> {
          Map<KT, T> kttMap = (Map<KT, T>) map;
          subscriberStatusGuarder.retakeWriting(kttMap);
        }, Map.class);

    config.configure(STOPPED_ON_READING)
        .permit(RETAKE_READING, RUNNING)
        .onEntryFrom(new TriggerWithParameters1<>(STOP_ON_READING, List.class),
            list -> {
              List<OT> otList = (List<OT>) list;
              subscriberStatusGuarder.stopReading(otList);
            }, List.class);

    config.configure(STOPPED_ON_WRITING)
        .permit(RETAKE_WRITING, RUNNING)
        .onEntryFrom(new TriggerWithParameters1<>(STOP_ON_WRITING, Map.class),
            map -> {
              Map<KT, T> kttMap = (Map<KT, T>) map;
              subscriberStatusGuarder.stopWriting(kttMap);
            }, Map.class);

    config.configure(DEPLETED)
        .permit(NOTIFY_OTHER_IS_DEPLETED, BOTH_ARE_DEPLETED);

    config.configure(OTHER_IS_DEPLETED)
        .permit(MARK_AS_DEPLETED, BOTH_ARE_DEPLETED);

    config.configure(DEPLETED)
        .onEntry(subscriberStatusGuarder::bothAreDepleted);

    return config;
  }
}
