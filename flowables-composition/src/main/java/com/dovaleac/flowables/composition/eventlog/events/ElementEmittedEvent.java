package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

public class ElementEmittedEvent extends Event {

  public static final String MESSAGE_FORMAT = "Couple emitted with id %s";

  public ElementEmittedEvent(Side side, String id) {
    super(EventType.ELEMENT_EMITTED, side, String.format(MESSAGE_FORMAT, id));
  }
}
