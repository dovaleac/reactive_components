package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.BuffersStatus;
import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

import java.util.Set;

public class TriggerEvent extends Event {

  public static final String MESSAGE_FORMAT = "Trigger %s moved to state %s";

  public TriggerEvent(Side side, String trigger, String newState) {
    super(EventType.TRIGGER, side, String.format(MESSAGE_FORMAT, trigger, newState));
  }

}
