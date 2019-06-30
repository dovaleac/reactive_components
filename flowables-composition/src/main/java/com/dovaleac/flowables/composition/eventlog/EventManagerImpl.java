package com.dovaleac.flowables.composition.eventlog;

import com.dovaleac.flowables.composition.eventlog.action.EventAction;

import java.util.List;

public class EventManagerImpl implements EventManager {

  private final List<EventAction> actions;

  public EventManagerImpl(
      List<EventAction> actions) {
    this.actions = actions;
  }

  @Override
  public void processEvent(Event event) {
    actions.forEach(eventAction -> eventAction.processEvent(event));
  }
}
