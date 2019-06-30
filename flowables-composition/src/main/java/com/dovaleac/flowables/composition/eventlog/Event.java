package com.dovaleac.flowables.composition.eventlog;

public abstract class Event {
  public static final String MESSAGE_FORMAT = "%s - %s - %s";
  protected final EventType eventType;
  protected final Side side;

  public Event(EventType eventType, Side side) {
    this.eventType = eventType;
    this.side = side;
  }

  protected abstract String getMessage();

  public String asMessage() {
    return String.format(MESSAGE_FORMAT, eventType, side, getMessage());
  }
}
