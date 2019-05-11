package com.dovaleac.flowablesComposition.strategy.instance.domain;


public class SmallDomainClass {
  private final long id;
  private final String value;

  public SmallDomainClass(long id, String value) {
    this.id = id;
    this.value = value;
  }

  public long getId() {
    return id;
  }

  public String getValue() {
    return value;
  }

  public static SmallDomainClass left(long id) {
    return new SmallDomainClass(id, "l" + id);
  }
  public static SmallDomainClass right(long id) {
    return new SmallDomainClass(id, "r" + id);
  }
}

