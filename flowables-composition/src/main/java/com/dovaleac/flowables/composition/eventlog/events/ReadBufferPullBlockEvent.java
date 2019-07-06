package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.BuffersStatus;
import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;
import io.reactivex.functions.Function;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ReadBufferPullBlockEvent<InnerKT> extends Event {

  public static final String MESSAGE_FORMAT = "Read buffer consumed [%s]";
  private final Set<InnerKT> keys;

  public <KT, T> ReadBufferPullBlockEvent(Side side, List<T> list,
      Function<T, InnerKT> keyFunction) {
    super(EventType.READ_BUFFER_CONSUMED, side, String.format(MESSAGE_FORMAT, list.stream()
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

    keys = list.stream()
        .map(t -> {
          try {
            return keyFunction.apply(t);
          } catch (Exception e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }


  @Override
  public <KT> BuffersStatus<KT> updateBufferStatus(BuffersStatus<KT> buffersStatus) {
    if (side == Side.LEFT) {
      buffersStatus.getLeftReadBufferKeys().removeAll(keys);
    } else {
      buffersStatus.getRightReadBufferKeys().removeAll(keys);
    }
    return buffersStatus;
  }

}
