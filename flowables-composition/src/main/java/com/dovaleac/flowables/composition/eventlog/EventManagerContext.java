package com.dovaleac.flowables.composition.eventlog;

import java.util.List;

public class EventManagerContext {

  private EventManager eventManager = new EventManagerImpl(List.of());
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

  public EventManager getEventManager() {
    return eventManager;
  }

  public static void setEventManager(EventManager eventManager) {
    getInstance().eventManager = eventManager;
  }
}
