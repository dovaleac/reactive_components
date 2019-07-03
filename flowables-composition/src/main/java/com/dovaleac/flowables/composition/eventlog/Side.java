package com.dovaleac.flowables.composition.eventlog;

public enum Side {
  LEFT("LEFT "), RIGHT("RIGHT");

  private final String representation;

  Side(String representation) {
    this.representation = representation;
  }

  public String getRepresentation() {
    return representation;
  }
}
