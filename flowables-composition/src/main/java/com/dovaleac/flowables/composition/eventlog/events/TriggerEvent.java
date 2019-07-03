package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

public class TriggerEvent extends Event {

  public static final String MESSAGE_FORMAT = "Trigger %s moved to state %s";

  public TriggerEvent(Side side, String trigger, String newState) {
    super(EventType.TRIGGER, side, String.format(MESSAGE_FORMAT, trigger, newState));
  }
}
