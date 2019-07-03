package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;
import com.dovaleac.flowables.composition.strategy.instance.buffered.capacity.NextAction;

public class PollReadEvent extends Event {

  public static final String MESSAGE_FORMAT = "Polled read number %d";

  public PollReadEvent(Side side, int acc) {
    super(EventType.POLL_READ, side, String.format(MESSAGE_FORMAT, acc));
  }
}
