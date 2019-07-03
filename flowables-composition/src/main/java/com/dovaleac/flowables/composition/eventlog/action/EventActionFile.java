package com.dovaleac.flowables.composition.eventlog.action;

import com.dovaleac.flowables.composition.eventlog.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class EventActionFile implements EventAction {

  private final Path path;

  public EventActionFile(Path path) {
    this.path = path;
  }

  @Override
  public void processEvent(Event event) {
    try {
      Files.write(path, event.asMessage().getBytes(), StandardOpenOption.APPEND);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
