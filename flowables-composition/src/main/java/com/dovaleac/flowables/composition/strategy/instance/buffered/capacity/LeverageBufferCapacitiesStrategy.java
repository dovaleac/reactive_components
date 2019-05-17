package com.dovaleac.flowables.composition.strategy.instance.buffered.capacity;

public interface LeverageBufferCapacitiesStrategy {

  NextAction getNextAction(double readBufferCapacity, double writeBufferCapacity);
}
