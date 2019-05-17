package com.dovaleac.flowablesComposition.planner;

import java.util.Objects;

public class Priority {

  private final PriorityOption priorityOption;
  private final short weight;

  private Priority(PriorityOption priorityOption, short weight) {
    this.priorityOption = priorityOption;
    this.weight = weight;
  }

  public static Priority memory(short weight) {
    return new Priority(PriorityOption.MEMORY, weight);
  }

  public static Priority time(short weight) {
    return new Priority(PriorityOption.TIME, weight);
  }

  public PriorityOption getPriorityOption() {
    return priorityOption;
  }

  public short getWeight() {
    return weight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Priority priority = (Priority) o;
    return weight == priority.weight && priorityOption == priority.priorityOption;
  }

  @Override
  public int hashCode() {
    return Objects.hash(priorityOption, weight);
  }

  public enum PriorityOption {
    TIME,
    MEMORY
  }
}
