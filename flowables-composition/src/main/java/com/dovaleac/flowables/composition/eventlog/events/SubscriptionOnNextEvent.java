package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;
import io.reactivex.functions.Function;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubscriptionOnNextEvent extends Event {

  public static final String MESSAGE_FORMAT = "New block with [%s]";

  public <KT, T> SubscriptionOnNextEvent(Side side, List<T> list, Function<T, KT> keyFunction) {
    super(EventType.BUFFER_REQUESTED, side, String.format(MESSAGE_FORMAT, list.stream()
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
