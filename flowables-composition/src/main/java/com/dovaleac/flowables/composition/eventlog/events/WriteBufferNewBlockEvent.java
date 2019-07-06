package com.dovaleac.flowables.composition.eventlog.events;

import com.dovaleac.flowables.composition.eventlog.BuffersStatus;
import com.dovaleac.flowables.composition.eventlog.Event;
import com.dovaleac.flowables.composition.eventlog.EventType;
import com.dovaleac.flowables.composition.eventlog.Side;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WriteBufferNewBlockEvent<InnerKT> extends Event {

  public static final String MESSAGE_FORMAT = "Write buffer received [%s]";
  private final Set<InnerKT> keys;

  public <T> WriteBufferNewBlockEvent(Side side, Map<InnerKT, T> elements) {
    super(EventType.WRITE_BUFFER_RECEIVED, side, String.format(MESSAGE_FORMAT, elements.keySet()
        .stream()
        .filter(Objects::nonNull)
        .map(Objects::toString)
        .collect(Collectors.joining(", "))
    ));
    this.keys = elements.keySet();
  }

  @Override
  public <KT> BuffersStatus<KT> updateBufferStatus(BuffersStatus<KT> buffersStatus) {
    if (side == Side.LEFT) {
      buffersStatus.getLeftWriteBufferKeys().addAll((Set<KT>) keys);
    } else {
      buffersStatus.getRightWriteBufferKeys().addAll((Set<KT>) keys);
    }
    return buffersStatus;
  }
}
