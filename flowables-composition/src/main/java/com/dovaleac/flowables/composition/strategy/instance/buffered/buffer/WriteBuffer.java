package com.dovaleac.flowables.composition.strategy.instance.buffered.buffer;

import com.dovaleac.flowables.composition.strategy.instance.buffered.exceptions.WriteBufferFrozenException;
import com.dovaleac.flowables.composition.strategy.instance.buffered.exceptions.WriteBufferFullException;
import com.dovaleac.flowables.composition.strategy.instance.buffered.remnant.UnmatchedYetRemnantImpl;
import com.github.oxo42.stateless4j.StateMachine;

import java.util.Map;

public class WriteBuffer<T, OT, KT, LT, RT> {

  private final UnmatchedYetRemnantImpl<T, OT, KT, LT, RT> remnant;
  private final int maxElements;
  private final Map<KT, T> buffer;

  private final StateMachine<WriteBufferAcceptNewInputsState, WriteBufferAcceptNewInputsTrigger>
      stateMachine =
          new StateMachine<>(
              WriteBufferAcceptNewInputsState.ACCEPT_NEW,
              new WriteBufferAcceptNewInputsStateMachine(this).getConfig());
  private final Object freezeLock = new Object();
  private final Object capacityLock = new Object();

  private boolean isFrozen = false;

  public WriteBuffer(
      UnmatchedYetRemnantImpl<T, OT, KT, LT, RT> remnant, int maxElements, Map<KT, T> initialMap) {
    this.remnant = remnant;
    this.maxElements = maxElements;
    this.buffer = initialMap;
  }

  /**
   * Adds a bunch of elements to the buffer. If the resulting elements exceed the expected capacity
   * of the buffer, no problem with it, the next time that anyone intends to put more objects, it
   * will fail
   *
   * @param elementsToAdd elements to be added to the buffer
   * @throws WriteBufferFrozenException if the write buffer is in frozen state, it won't let put
   *     elements
   * @throws WriteBufferFullException if the buffer has more elements than expected, or the same
   *     number, it won't let put elements
   */
  public void addToQueue(Map<KT, T> elementsToAdd)
      throws WriteBufferFrozenException, WriteBufferFullException {

    synchronized (freezeLock) {
      if (isFrozen) {
        throw new WriteBufferFrozenException();
      }
    }

    if (isFull()) {
      throw new WriteBufferFullException();
    }

    synchronized (capacityLock) {
      buffer.putAll(elementsToAdd);
    }
  }

  public void addForciblyToQueue(Map<KT, T> elementsToAdd) {
    buffer.putAll(elementsToAdd);
  }

  // no need to erase them, the WriteBuffer element will be deleted itself
  public Map<KT, T> getAllElements() {
    return buffer;
  }

  public void clear() {
    buffer.clear();
  }

  public void fire(WriteBufferAcceptNewInputsTrigger trigger) {
    stateMachine.fire(trigger);
  }

  void itWouldBeBetterToWrite() {
    remnant.itWouldBeBetterToWrite();
  }

  boolean isBufferFrozen() {
    return remnant.isWriteBufferFrozen();
  }

  void freeze() {
    synchronized (freezeLock) {
      isFrozen = true;
    }
  }

  void unfreeze() {
    synchronized (freezeLock) {
      isFrozen = false;
    }
  }

  boolean isFull() {
    synchronized (capacityLock) {
      return buffer.size() >= maxElements;
    }
  }

  public double getCapacity() {
    synchronized (capacityLock) {
      return (double) (maxElements - buffer.size()) / (double) maxElements;
    }
  }
}
