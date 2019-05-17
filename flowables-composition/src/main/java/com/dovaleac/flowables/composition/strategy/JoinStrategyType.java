package com.dovaleac.flowables.composition.strategy;

public enum JoinStrategyType {
  DEPLETE_LEFT(DepleteLeftJoinStrategy.getInstance()),
  DEPLETE_RIGHT(DepleteRightJoinStrategy.getInstance()),
  BUFFERED(BufferedJoinStrategy.getInstance()),
  MINIFIED_BUFFERED(MinifiedBufferedJoinStrategy.getInstance()),
  ROCKS(RocksJoinStrategy.getInstance());

  private final JoinStrategy strategy;

  JoinStrategyType(JoinStrategy strategy) {
    this.strategy = strategy;
  }

  public JoinStrategy getStrategy() {
    return strategy;
  }
}
