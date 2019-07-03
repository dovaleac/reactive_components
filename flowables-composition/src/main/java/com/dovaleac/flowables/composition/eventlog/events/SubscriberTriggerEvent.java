package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

public class SubscriberTriggerEvent extends Event {

  public static final String MESSAGE_FORMAT = "Subscriber trigger %s moved to state %s";

  public SubscriberTriggerEvent(Side side, String trigger, String newState) {
    super(EventType.SUBSCRIBER_TRIGGER, side, String.format(MESSAGE_FORMAT, trigger, newState));
  }
}
