package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;
import com.dovaleac.flowables.composition.strategy.instance.buffered.capacity.NextAction;

public class ChooseNextActionEvent extends Event {

  public static final String MESSAGE_FORMAT = "Chose to %s with read=%f and write=%f";

  public ChooseNextActionEvent(Side side, NextAction nextAction, double read, double write) {
    super(EventType.CHECK_CAPACITY, side, String.format(MESSAGE_FORMAT, nextAction, read, write));
  }
}
