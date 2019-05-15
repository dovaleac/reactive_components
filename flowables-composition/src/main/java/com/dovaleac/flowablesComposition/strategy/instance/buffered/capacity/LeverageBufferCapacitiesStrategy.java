package com.dovaleac.flowablesComposition.strategy.instance.buffered.capacity;

public interface LeverageBufferCapacitiesStrategy {

  NextAction getNextAction(double readBufferCapacity, double writeBufferCapacity);
}
