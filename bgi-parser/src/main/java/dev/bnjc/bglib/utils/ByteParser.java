package dev.bnjc.bglib.utils;

import dev.bnjc.bglib.BGIType;
import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;
import dev.bnjc.bglib.stream.BGIStreamParser;
import dev.bnjc.bglib.stream.object.StreamObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class ByteParser {
  private ByteParser() {}

  public static Object getByType(int key, ByteBuffer buffer) throws BGIParseException {
    BGIType type = BGIType.fromTypeId(ByteParser.getByte(buffer));

    return switch (type) {
      case BGIType.BYTE -> getByte(buffer);
      case BGIType.INTEGER -> getVarInt(buffer);
      case BGIType.STRING -> getString(buffer);
      case BGIType.STRING_ARRAY -> getStringArray(buffer);
      case BGIType.SHORT -> getShort(buffer);
      case BGIType.LONG -> getLong(buffer);
      case BGIType.FLOAT -> getFloat(buffer);
      case BGIType.DOUBLE -> getDouble(buffer);
      case BGIType.STREAM -> getStream(key, buffer);
      case BGIType.BOOLEAN -> getBoolean(buffer);
      case BGIType.UUID -> getUUID(buffer);
      default -> throw new BGIParseException("Could not parse data type [" + type + "]", ErrorCode.UNKNOWN_DATA_TYPE);
    };
  }

  public static byte getByte(ByteBuffer buffer) {
    return buffer.get();
  }

  public static short getShort(ByteBuffer buffer) {
    return buffer.getShort();
  }

  public static long getLong(ByteBuffer buffer) {
    return buffer.getLong();
  }

  public static float getFloat(ByteBuffer buffer) {
    return buffer.getFloat();
  }

  public static double getDouble(ByteBuffer buffer) {
    return buffer.getDouble();
  }

  public static boolean getBoolean(ByteBuffer buffer) {
    return getByte(buffer) != 0;
  }

  public static String getString(ByteBuffer buffer) {
    int length = getVarInt(buffer);
    byte[] strBytes = new byte[length];
    buffer.get(strBytes);
    return new String(strBytes, StandardCharsets.UTF_8);
  }

  public static String[] getStringArray(ByteBuffer buffer) {
    int count = getVarInt(buffer);
    String[] array = new String[count];
    for (int j = 0; j < count; j++) {
      array[j] = getString(buffer);
    }
    return array;
  }

  public static StreamObject getStream(int key, ByteBuffer buffer) throws BGIParseException {
    int length = getVarInt(buffer);
    byte[] streamBytes = new byte[length];
    buffer.get(streamBytes);
    return BGIStreamParser.parse(key, streamBytes);
  }

  public static int getInt(ByteBuffer buffer) {
    return buffer.getInt();
  }

  public static int getVarInt(ByteBuffer buffer) {
    int value = 0;
    int shift = 0;

    while (buffer.hasRemaining()) {
      byte b = getByte(buffer);

      // Get the 7 data bits
      int temp = b & 0x7F;
      value |= temp << shift;

      // If the continuation bit is not set, we're done
      if ((b & 0x80) == 0) {
        break;
      }

      // Each byte contributes 7 bits
      shift += 7;

      // guard against malformed input
      if (shift > 32) {
        throw new IllegalArgumentException("Malformed variable int");
      }
    }

    return value;
  }

  public static UUID getUUID(ByteBuffer buffer) {
    long mostSigBits = buffer.getLong();
    long leastSigBits = buffer.getLong();
    return new UUID(mostSigBits, leastSigBits);
  }
}
