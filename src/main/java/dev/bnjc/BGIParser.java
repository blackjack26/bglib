package dev.bnjc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class BGIParser {
  private final ByteBuffer buffer;
  private final byte[] originalData;

  private BGIParser(byte[] data) {
    this.originalData = data;
    this.buffer = ByteBuffer.wrap(data);
    this.buffer.order(ByteOrder.BIG_ENDIAN);
  }

  public static BGIData parse(byte[] data) throws IllegalArgumentException {
    BGIParser parser = new BGIParser(data);
    return parser.parse();
  }

  private BGIData parse() throws IllegalArgumentException {
    if (originalData.length < 9) {
      throw new IllegalArgumentException("Data is too short to contain header.");
    }

    if (originalData[0] != 7) {
      throw new IllegalArgumentException("Not goblin enough to be BGI data");
    }

    buffer.position(1); // Skip initial 7

    int dataVersion = buffer.getInt();
    var attributes = parseAttributes();

    return new BGIData(dataVersion, attributes);
  }

  private Map<Integer, Object> parseAttributes() {
    int numAttributes = buffer.getInt();

    var attributes = new HashMap<Integer, Object>();
    for (int i = 0; i < numAttributes; i++) {
      int key = buffer.getInt();
      byte typeId = buffer.get();

      Object value = switch (typeId) {
        // Integer
        case 2 -> buffer.getInt();
        // String
        case 3 -> {
          int length = buffer.getInt();
          byte[] strBytes = new byte[length];
          buffer.get(strBytes);
          yield new String(strBytes, StandardCharsets.UTF_8);
        }
        case 4 -> {
          int count = buffer.getInt();
          String[] array = new String[count];
          for (int j = 0; j < count; j++) {
            int sLen = buffer.getInt();
            byte[] sBytes = new byte[sLen];
            buffer.get(sBytes);
            array[j] = new String(sBytes, StandardCharsets.UTF_8);
          }
          yield array;
        }
        // Double
        case 8 -> buffer.getDouble();
        // TODO: Stream
        case 9 -> {
          int length = buffer.getInt();
          byte[] streamBytes = new byte[length];
          buffer.get(streamBytes);
          yield streamBytes;
        }
        // Boolean
        case 10 -> buffer.get() != 0;
        default -> throw new IllegalArgumentException("Unknown type ID: " + typeId);
      };

      attributes.put(key, value);
    }

    return attributes;
  }
}
