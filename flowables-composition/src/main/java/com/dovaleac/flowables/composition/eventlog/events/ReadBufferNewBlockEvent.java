package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;
import io.reactivex.functions.Function;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReadBufferNewBlockEvent extends Event {

  public static final String MESSAGE_FORMAT = "Read buffer received [%s]";

  public <KT, T> ReadBufferNewBlockEvent(Side side, List<T> list, Function<T, KT> keyFunction) {
    super(EventType.READ_BUFFER_RECEIVED, side, String.format(MESSAGE_FORMAT, list.stream()
        .map(t -> {
          try {
            return keyFunction.apply(t);
          } catch (Exception e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .map(Objects::toString)
        .collect(Collectors.joining(", "))
    ));
  }
}
