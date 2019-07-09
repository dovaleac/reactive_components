package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

public class CompletionTriggerEvent extends Event {

  public static final String MESSAGE_FORMAT = "Completion trigger %s moved to state %s";

  public CompletionTriggerEvent(Side side, String trigger, String newState) {
    super(EventType.COMPLETION_TRIGGER, side, String.format(MESSAGE_FORMAT, trigger, newState));
  }
}