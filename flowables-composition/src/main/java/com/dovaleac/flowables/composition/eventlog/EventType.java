package com.dovaleac.flowables.composition.eventlog;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum EventType {
  TRIGGER,
  CHECK_CAPACITY,
  BUFFER_REQUESTED,
  READ_BUFFER_RECEIVED,
  WRITE_BUFFER_RECEIVED,
  ELEMENT_EMITTED;


  static int maxEventSize;
  static {
    maxEventSize = Stream.of(values())
        .map(EventType::name)
        .map(String::length)
        .max(Integer::compareTo)
        .orElse(-1);
  }

  public String getPrintableMessage() {
    String spaces = IntStream.range(name().length(), maxEventSize)
        .boxed()
        .map(i -> " ")
        .collect(Collectors.joining());

    return name() + spaces;
  }
}
