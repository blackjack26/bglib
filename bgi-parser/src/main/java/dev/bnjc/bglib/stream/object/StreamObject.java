package dev.bnjc.bglib.stream.object;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class StreamObject {
  protected final int key;
  protected final ByteBuffer buffer;

  public StreamObject(int key, ByteBuffer buffer) {
    this.key = key;
    this.buffer = buffer;
  }

  protected String readIdentifier() {
    int start = buffer.position();

    while (buffer.hasRemaining()) {
      byte b = buffer.get(buffer.position()); // peek
      if (!isLetterOrUnderscore(b)) {
        break;
      }

      buffer.get(); // consume
    }

    int end = buffer.position();
    byte[] bytes = new byte[end - start];
    buffer.position(start); // reset to start
    buffer.get(bytes); // consume all at once

    return new String(bytes, StandardCharsets.UTF_8);
  }

  private boolean isLetterOrUnderscore(byte b) {
    return (b >= 'A' && b <= 'Z') ||
        (b >= 'a' && b <= 'z') ||
        b == '_';
  }

  @Override
  public String toString() {
    return "StreamObject{" +
        "buffer=" + Arrays.toString(buffer.array()) +
        '}';
  }
}
