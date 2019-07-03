package com.dovaleac.flowables.composition.eventlog.action;

import com.dovaleac.flowables.composition.eventlog.Event;

public class EventActionSysout implements EventAction {
  @Override
  public void processEvent(Event event) {
    System.out.print(event.asMessage());
  }
}
