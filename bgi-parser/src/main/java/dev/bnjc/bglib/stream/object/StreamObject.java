package dev.bnjc.bglib.stream.object;

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class StreamObject {
  protected final int key;
  protected final ByteBuffer buffer;

  public StreamObject(int key, ByteBuffer buffer) {
    this.key = key;
    this.buffer = buffer;
  }

  @Override
  public String toString() {
    return "StreamObject{" +
        "buffer=" + Arrays.toString(buffer.array()) +
        '}';
  }
}
