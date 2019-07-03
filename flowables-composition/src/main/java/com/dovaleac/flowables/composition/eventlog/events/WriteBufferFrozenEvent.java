package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

public class WriteBufferFrozenEvent extends Event {

  public static final String MESSAGE_FORMAT = "";

  public WriteBufferFrozenEvent(Side side) {
    super(EventType.WRITE_BUFFER_IS_FROZEN, side, MESSAGE_FORMAT);
  }
}
