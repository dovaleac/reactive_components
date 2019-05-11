package com.dovaleac.flowablesComposition;

import com.dovaleac.flowablesComposition.planner.Priority;

import java.time.Duration;
import java.util.Objects;

public class PlannerConfig {

  public static PlannerConfig NO_CONFIG = PlannerConfig.builder().build();

  private final long leftCardinality;
  private final long rightCardinality;
  private final double matchChance;
  private final boolean areSorted;
  private final boolean canBeSorted;
  private final Duration leftTimeToComplete;
  private final Duration rightTimeToComplete;
  private final Priority priority;

  private PlannerConfig(long leftCardinality, long rightCardinality, double matchChance,
      boolean areSorted, boolean canBeSorted, Duration leftTimeToComplete,
      Duration rightTimeToComplete, Priority priority) {
    this.leftCardinality = leftCardinality;
    this.rightCardinality = rightCardinality;
    this.matchChance = matchChance;
    this.areSorted = areSorted;
    this.canBeSorted = canBeSorted;
    this.leftTimeToComplete = leftTimeToComplete;
    this.rightTimeToComplete = rightTimeToComplete;
    this.priority = priority;
  }

  public long getLeftCardinality() {
    return leftCardinality;
  }

  public long getRightCardinality() {
    return rightCardinality;
  }

  public double getMatchChance() {
    return matchChance;
  }

  public boolean isAreSorted() {
    return areSorted;
  }

  public boolean isCanBeSorted() {
    return canBeSorted;
  }

  public Duration getLeftTimeToComplete() {
    return leftTimeToComplete;
  }

  public Duration getRightTimeToComplete() {
    return rightTimeToComplete;
  }

  public Priority getPriority() {
    return priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlannerConfig that = (PlannerConfig) o;
    return leftCardinality == that.leftCardinality &&
        rightCardinality == that.rightCardinality &&
        Double.compare(that.matchChance, matchChance) == 0 &&
        areSorted == that.areSorted &&
        canBeSorted == that.canBeSorted &&
        Objects.equals(leftTimeToComplete, that.leftTimeToComplete) &&
        Objects.equals(rightTimeToComplete, that.rightTimeToComplete) &&
        Objects.equals(priority, that.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leftCardinality, rightCardinality, matchChance, areSorted, canBeSorted, leftTimeToComplete, rightTimeToComplete, priority);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private long leftCardinality = -1;
    private long rightCardinality = -1;
    private double matchChance = 0.2;
    private boolean areSorted = false;
    private boolean canBeSorted = false;
    private Duration leftTimeToComplete = Duration.ofMinutes(5);
    private Duration rightTimeToComplete = Duration.ofMinutes(5);
    private Priority priority = Priority.memory((short) 0);

    private Builder() {
    }

    public Builder withLeftCardinality(long leftCardinality) {
      this.leftCardinality = leftCardinality;
      return this;
    }

    public Builder withRightCardinality(long rightCardinality) {
      this.rightCardinality = rightCardinality;
      return this;
    }

    public Builder withMatchChance(double matchChance) {
      this.matchChance = matchChance;
      return this;
    }

    public Builder withAreSorted(boolean areSorted) {
      this.areSorted = areSorted;
      return this;
    }

    public Builder withCanBeSorted(boolean canBeSorted) {
      this.canBeSorted = canBeSorted;
      return this;
    }

    public Builder withLeftTimeToComplete(Duration leftTimeToComplete) {
      this.leftTimeToComplete = leftTimeToComplete;
      return this;
    }

    public Builder withRightTimeToComplete(Duration rightTimeToComplete) {
      this.rightTimeToComplete = rightTimeToComplete;
      return this;
    }

    public Builder withPriority(Priority priority) {
      this.priority = priority;
      return this;
    }

    public PlannerConfig build() {
      return new PlannerConfig(
          leftCardinality, rightCardinality, matchChance, areSorted, canBeSorted,
          leftTimeToComplete, rightTimeToComplete, priority
      );
    }
  }
}
