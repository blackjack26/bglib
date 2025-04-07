package dev.bnjc.bglib;

public enum BGIType {
  INTEGER((byte)2, Integer.class),
  STRING((byte)3, String.class),
  STRING_ARRAY((byte)4, String[].class),
  DOUBLE((byte)8, Double.class),
  STREAM((byte)9, Byte[].class),
  BOOLEAN((byte)10, Boolean.class);

  public final byte typeId;
  public final Class<?> typeClass;

  BGIType(byte typeId, Class<?> typeClass) {
    this.typeId = typeId;
    this.typeClass = typeClass;
  }
}
