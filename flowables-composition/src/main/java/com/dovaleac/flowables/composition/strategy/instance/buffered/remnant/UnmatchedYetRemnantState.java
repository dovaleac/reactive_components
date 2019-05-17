package com.dovaleac.flowables.composition.strategy.instance.buffered.remnant;

public enum UnmatchedYetRemnantState {
  IDLE(false, false, true),
  READING(false, true, true),
  WAITING_FOR_SYNCHRONIZER(true, false, false),
  SYNCHRONIZER(false, false, false),
  REJECTED_SYNCHRONIZER(false, true, false),
  WRITING(true, false, true),
  WAITING_FOR_SYNCHRONIZEE(false, false, false),
  SYNCHRONIZEE(false, false, false),
  REJECTED_SYNCHRONIZEE(true, false, false);

  private final boolean consumesWritingBuffer;
  private final boolean consumesReadingBuffer;
  private final boolean allowsFillingWritingBuffer;

  UnmatchedYetRemnantState(
      boolean consumesWritingBuffer,
      boolean consumesReadingBuffer,
      boolean allowsFillingWritingBuffer) {
    this.consumesWritingBuffer = consumesWritingBuffer;
    this.consumesReadingBuffer = consumesReadingBuffer;
    this.allowsFillingWritingBuffer = allowsFillingWritingBuffer;
  }

  public boolean consumesWritingBuffer() {
    return consumesWritingBuffer;
  }

  public boolean consumesReadingBuffer() {
    return consumesReadingBuffer;
  }

  public boolean allowsFillingWritingBuffer() {
    return allowsFillingWritingBuffer;
  }
}
