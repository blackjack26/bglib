package dev.bnjc;

public enum BGIType {
  INTEGER((byte)2),
  STRING((byte)3),
  STRING_ARRAY((byte)4),
  DOUBLE((byte)8),
  STREAM((byte)9),
  BOOLEAN((byte)10);

  public final byte typeId;

  BGIType(byte typeId) {
    this.typeId = typeId;
  }
}
