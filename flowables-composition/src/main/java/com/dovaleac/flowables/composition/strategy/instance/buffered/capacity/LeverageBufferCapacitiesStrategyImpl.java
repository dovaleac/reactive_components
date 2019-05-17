package com.dovaleac.flowables.composition.strategy.instance.buffered.capacity;

public class LeverageBufferCapacitiesStrategyImpl implements LeverageBufferCapacitiesStrategy {

  private final double readThresholdToChange;
  private final double writeThresholdToChange;
  private final double minWriteToAcceptChange;

  public LeverageBufferCapacitiesStrategyImpl(
      double readThresholdToChange, double writeThresholdToChange, double minWriteToAcceptChange) {
    this.readThresholdToChange = readThresholdToChange;
    this.writeThresholdToChange = writeThresholdToChange;
    this.minWriteToAcceptChange = minWriteToAcceptChange;
    validate();
  }

  private void validate() throws IllegalArgumentException {
    validateOne(readThresholdToChange);
    validateOne(writeThresholdToChange);
    validateOne(minWriteToAcceptChange);
  }

  private void validateOne(double actualValue) throws IllegalArgumentException {
    if (actualValue > 1.0 || actualValue < 0.0) {
      throw new IllegalArgumentException(
          "Value " + actualValue + " is not in the [0.0, 1.0] " + "range");
    }
  }

  @Override
  public NextAction getNextAction(double readBufferCapacity, double writeBufferCapacity) {
    if (writeBufferCapacity >= writeThresholdToChange) {
      return NextAction.WRITE;
    }

    if (readBufferCapacity >= readThresholdToChange) {
      return NextAction.READ;
    }

    return writeBufferCapacity > minWriteToAcceptChange ? NextAction.WRITE : NextAction.READ;
  }
}
