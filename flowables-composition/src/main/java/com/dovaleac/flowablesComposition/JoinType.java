package com.dovaleac.flowablesComposition;

public enum JoinType {
  INNER(false, false),
  LEFT(false, true),
  RIGHT(true, false),
  FULL(true, true);

  private final boolean allowLeftNulls;
  private final boolean allowRightNulls;

  JoinType(boolean allowLeftNulls, boolean allowRightNulls) {
    this.allowLeftNulls = allowLeftNulls;
    this.allowRightNulls = allowRightNulls;
  }

  public boolean allowsLeftNulls() {
    return allowLeftNulls;
  }

  public boolean allowsRightNulls() {
    return allowRightNulls;
  }
}
