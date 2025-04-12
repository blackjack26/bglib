package dev.bnjc.bglib;

/**
 * A utility enum used to help parse the BGI data. It stores a reference to the signal byte used
 * to distinguish the type as well as the associated class.
 *
 * @since 0.1.0
 * @author Jack Grzechowiak
 */
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
