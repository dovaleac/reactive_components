package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;
import io.reactivex.functions.Function;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class WriteBufferNewBlockEvent extends Event {

  public static final String MESSAGE_FORMAT = "Write buffer received [%s]";

  public <KT, T> WriteBufferNewBlockEvent(Side side, Map<T, KT> elements) {
    super(EventType.WRITE_BUFFER_RECEIVED, side, String.format(MESSAGE_FORMAT, elements.keySet()
        .stream()
        .filter(Objects::nonNull)
        .map(Objects::toString)
        .collect(Collectors.joining(", "))
    ));
  }
}
