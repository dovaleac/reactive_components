package com.dovaleac.flowables.composition.eventlog.action;

import com.dovaleac.flowables.composition.eventlog.BuffersStatus;
import com.dovaleac.flowables.composition.eventlog.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class EventActionFileWithBuffersStatus<KT> implements EventAction {

  private final Path path;
  private BuffersStatus<KT> buffersStatus;

  public EventActionFileWithBuffersStatus(Path path,
      BuffersStatus<KT> buffersStatus) {
    this.path = path;
    this.buffersStatus = buffersStatus;
  }

  @Override
  public void processEvent(Event event) {
    try {
      Files.write(path, event.asMessageWithBufferStatus(buffersStatus).getBytes(),
          StandardOpenOption.APPEND);
      buffersStatus = event.updateBufferStatus(buffersStatus);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
