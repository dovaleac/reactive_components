package com.dovaleac.flowables.composition.eventlog;

import com.dovaleac.flowables.composition.eventlog.events.*;
import com.dovaleac.flowables.composition.strategy.instance.buffered.capacity.NextAction;
import io.reactivex.functions.Function;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Event {
  public static final String MESSAGE_FORMAT = "%s - %s - %s - %s\n";
  protected final EventType eventType;
  protected final Side side;
  protected final String message;
  protected final Date date = new Date();

  public Event(EventType eventType, Side side, String message) {
    this.eventType = eventType;
    this.side = side;
    this.message = message;
  }

  public String asMessage() {
    return String.format(MESSAGE_FORMAT, new SimpleDateFormat("HH:mm:ss.SSS").format(date),
        eventType.getPrintableMessage(),
        side.getRepresentation(),
        message);
  }

  public static Event trigger(Side side, String trigger, String newState) {
    return new TriggerEvent(side, trigger, newState);
  }

  public static Event writeBufferTrigger(Side side, String trigger, String newState) {
    return new WriteBufferTriggerEvent(side, trigger, newState);
  }

  public static Event subscriberTrigger(Side side, String trigger, String newState) {
    return new SubscriberTriggerEvent(side, trigger, newState);
  }

  public static <T, KT> Event subscriberOnNext(Side side, List<T> list,
      Function<T, KT> keyFunction) {
    return new SubscriptionOnNextEvent(side, list, keyFunction);
  }

  public static <T, KT> Event pushToReadBuffer(Side side, List<T> list,
      Function<T, KT> keyFunction) {
    return new ReadBufferNewBlockEvent(side, list, keyFunction);
  }

  public static <T, KT> Event pushToWriteBuffer(Side side, Map<T, KT> elements) {
    return new WriteBufferNewBlockEvent(side, elements);
  }

  public static <KT> Event synchronizing(Side side, Set<KT> own, Set<KT> other) {
    return new SynchronizationEvent(side, own, other);
  }

  public static Event chooseNextAction(Side side, NextAction nextAction, double read, double write) {
    return new ChooseNextActionEvent(side, nextAction, read, write);
  }

  public static Event coupleEmitted(Side side, String id) {
    return new ElementEmittedEvent(side, id);
  }

  public static Event any(Side side, String id) {
    return new AnyEvent(side, id);
  }

  public static Event pollRead(Side side, int acc) {
    return new PollReadEvent(side, acc);
  }

  public static Event writeBufferFull(Side side) {
    return new WriteBufferFullEvent(side);
  }

  public static Event writeBufferFrozen(Side side) {
    return new WriteBufferFrozenEvent(side);
  }

}
