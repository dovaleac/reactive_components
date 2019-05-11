package com.dovaleac.flowablesComposition.strategy.instance;


public class SmallTuple {
  private final long id;
  private final String value;

  public SmallTuple(long id, String value) {
    this.id = id;
    this.value = value;
  }

  long getId() {
    return id;
  }

  public String getValue() {
    return value;
  }

  public static SmallTuple left(long id) {
    return new SmallTuple(id, "l" + id);
  }
  public static SmallTuple right(long id) {
    return new SmallTuple(id, "r" + id);
  }
}

