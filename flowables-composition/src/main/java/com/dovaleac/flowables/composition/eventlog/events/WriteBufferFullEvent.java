package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class WriteBufferFullEvent extends Event {

  public static final String MESSAGE_FORMAT = "";

  public WriteBufferFullEvent(Side side) {
    super(EventType.WRITE_BUFFER_IS_FULL, side, MESSAGE_FORMAT);
  }
}
