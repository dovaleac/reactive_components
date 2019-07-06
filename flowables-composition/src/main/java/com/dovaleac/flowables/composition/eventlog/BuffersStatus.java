package com.dovaleac.flowables.composition.eventlog;

import java.util.HashSet;
import java.util.Set;

public class BuffersStatus<KT> {
  private Set<KT> leftWriteBufferKeys = new HashSet<>();
  private Set<KT> rightWriteBufferKeys = new HashSet<>();
  private Set<KT> leftRemnantKeys = new HashSet<>();
  private Set<KT> rightRemnantKeys = new HashSet<>();
  private Set<KT> leftReadBufferKeys = new HashSet<>();
  private Set<KT> rightReadBufferKeys = new HashSet<>();

  public synchronized Set<KT> getLeftWriteBufferKeys() {
    return leftWriteBufferKeys;
  }

  public synchronized void setLeftWriteBufferKeys(Set<KT> leftWriteBufferKeys) {
    this.leftWriteBufferKeys = leftWriteBufferKeys;
  }

  public synchronized Set<KT> getRightWriteBufferKeys() {
    return rightWriteBufferKeys;
  }

  public synchronized void setRightWriteBufferKeys(Set<KT> rightWriteBufferKeys) {
    this.rightWriteBufferKeys = rightWriteBufferKeys;
  }

  public synchronized Set<KT> getLeftRemnantKeys() {
    return leftRemnantKeys;
  }

  public synchronized void setLeftRemnantKeys(Set<KT> leftRemnantKeys) {
    this.leftRemnantKeys = leftRemnantKeys;
  }

  public synchronized Set<KT> getRightRemnantKeys() {
    return rightRemnantKeys;
  }

  public synchronized void setRightRemnantKeys(Set<KT> rightRemnantKeys) {
    this.rightRemnantKeys = rightRemnantKeys;
  }

  public synchronized Set<KT> getLeftReadBufferKeys() {
    return leftReadBufferKeys;
  }

  public synchronized void setLeftReadBufferKeys(Set<KT> leftReadBufferKeys) {
    this.leftReadBufferKeys = leftReadBufferKeys;
  }

  public synchronized Set<KT> getRightReadBufferKeys() {
    return rightReadBufferKeys;
  }

  public synchronized void setRightReadBufferKeys(Set<KT> rightReadBufferKeys) {
    this.rightReadBufferKeys = rightReadBufferKeys;
  }

  @Override
  public synchronized String toString() {
    return "LWB=" + leftWriteBufferKeys.size()
        + " - RWB=" + rightWriteBufferKeys.size()
        + " - LRB=" + leftReadBufferKeys.size()
        + " - RRB=" + rightReadBufferKeys.size()
        + " - LR=" + leftRemnantKeys.size()
        + " - RR=" + rightRemnantKeys.size();
  }
}
