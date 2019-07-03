package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

public class AnyEvent extends Event {


  public AnyEvent(Side side, String id) {
    super(EventType.ANY, side, id);
  }
}
