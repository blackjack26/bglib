package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.utils.ByteParser;

import java.nio.ByteBuffer;

abstract class VersionedStreamObject extends StreamObject {
  protected byte version;

  public VersionedStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);
  }

  public void parse() throws BGIParseException {
    this.version = ByteParser.getByte(this.buffer);
    this.parseBuffer();
  }

  public byte getVersion() {
    return version;
  }

  protected abstract void parseBuffer() throws BGIParseException;

  @Override
  public String toString() {
    return "VersionedStreamObject{" +
        "version=" + version +
        "} " + super.toString();
  }
}
