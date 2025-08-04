package dev.bnjc.bglib;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A utility enum used to help parse the BGI data. It stores a reference to the signal byte used
 * to distinguish the type as well as the associated class.
 *
 * @since 0.1.0
 * @author Jack Grzechowiak
 */
public enum BGIType {
  BYTE((byte)1, Byte.class),
  INTEGER((byte)2, Integer.class),
  STRING((byte)3, String.class),
  STRING_ARRAY((byte)4, String[].class),
  SHORT((byte)5, Short.class),
  LONG((byte)6, Long.class),
  FLOAT((byte)7, Float.class),
  DOUBLE((byte)8, Double.class),
  STREAM((byte)9, Byte[].class),
  BOOLEAN((byte)10, Boolean.class),
  UUID((byte)11, UUID.class);

  public final byte typeId;
  public final Class<?> typeClass;

  BGIType(byte typeId, Class<?> typeClass) {
    this.typeId = typeId;
    this.typeClass = typeClass;
  }

  private static final Map<Byte, BGIType> BY_ID = new HashMap<>();
  static {
    for (var type : values()) {
      BY_ID.put(type.typeId, type);
    }
  }

  public static BGIType fromTypeId(byte id) {
    return BY_ID.get(id);
  }
}
