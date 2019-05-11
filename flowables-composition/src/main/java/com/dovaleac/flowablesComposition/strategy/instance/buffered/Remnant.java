package com.dovaleac.flowablesComposition.strategy.instance.buffered;

public class Remnant {

  void disableWriteBufferForFill() {

  }

  void enableConsumingReadingBuffer() {
    disableConsumingWritingBuffer();
  }

  void disableConsumingReadingBuffer() {

  }

  void enableConsumingWritingBuffer() {
    disableConsumingReadingBuffer();
  }

  void disableConsumingWritingBuffer() {

  }

  void requestSync() {

  }

  void rejectSync() {

  }

  void acceptSync() {

  }

  void synchronize() {

  }

  void syncFinished() {

  }

  void notifyWriteIsSafe() {

  }

  void useSecondaryWriteBuffer() {

  }

  void promoteSecondaryWriteBuffer() {

  }
}
