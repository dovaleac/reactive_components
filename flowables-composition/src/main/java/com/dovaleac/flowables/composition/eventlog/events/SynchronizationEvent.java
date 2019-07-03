package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SynchronizationEvent extends Event {

  public static final String MESSAGE_FORMAT = "Synchronizing own [%s] with other's [%s]";

  public <KT> SynchronizationEvent(Side side, Set<KT> own, Set<KT> other) {
    super(EventType.SYNCHRONIZATION, side, String.format(MESSAGE_FORMAT,
        own.stream().map(Objects::toString).collect(Collectors.joining(", ")),
        other.stream().map(Objects::toString).collect(Collectors.joining(", "))));
  }
}
