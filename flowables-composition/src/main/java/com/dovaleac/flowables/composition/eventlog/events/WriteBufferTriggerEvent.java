package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

public class WriteBufferTriggerEvent extends Event {

  public static final String MESSAGE_FORMAT = "WB Trigger %s moved to state %s";

  public WriteBufferTriggerEvent(Side side, String trigger, String newState) {
    super(EventType.WRITE_BUFFER_TRIGGER, side, String.format(MESSAGE_FORMAT, trigger, newState));
  }
}
