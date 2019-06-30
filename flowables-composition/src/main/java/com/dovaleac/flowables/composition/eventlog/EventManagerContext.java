package com.dovaleac.flowables.composition.eventlog;

import javax.naming.event.EventContext;

public class EventManagerContext {

  private EventContext eventContext;
  private static volatile EventManagerContext mInstance;

  private EventManagerContext() {
  }

  public static EventManagerContext getInstance() {
    if (mInstance == null) {
      synchronized (EventManagerContext.class) {
        if (mInstance == null) {
          mInstance = new EventManagerContext();
        }
      }
    }
    return mInstance;
  }

  public EventContext getEventContext() {
    return eventContext;
  }

  public static void setEventContext(EventContext eventContext) {
    getInstance().eventContext = eventContext;
  }
}
