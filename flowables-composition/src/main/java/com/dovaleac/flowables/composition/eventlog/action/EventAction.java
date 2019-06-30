package com.dovaleac.flowables.composition.eventlog.action;

import com.dovaleac.flowables.composition.eventlog.Event;

public interface EventAction {
  void processEvent(Event event);
}
