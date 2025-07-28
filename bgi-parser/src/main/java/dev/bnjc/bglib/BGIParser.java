package dev.bnjc.bglib;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * A parser to parse the custom Blockgame Item (BGI) tag found in the custom data component.
 *
 * <pre>
 *   BGIResult result = BGIParser.parse(itemStack);
 *   result.ifSuccess((data) -> {
 *     String itemId = data.getString(BGIField.ITEM_ID);
 *   });
 * </pre>
 *
 * @author Jack Grzechowiak
 * @since 0.1.2
 */
public final class BGIParser {
  /**
   * The name of the Blockgame Item byte array tag used within an {@linkplain ItemStack} custom data component
   */
  public static final String BGI_TAG = "bgi";

  private final ByteBuffer buffer;
  private final byte[] originalData;

  private BGIParser(byte[] data) {
    this.originalData = data;
    this.buffer = ByteBuffer.wrap(data);
    this.buffer.order(ByteOrder.BIG_ENDIAN);
  }

  /**
   * Parses the specified byte array into a {@link BGIParseResult} object.
   *
   * @param data Byte array from NBT data
   * @return a {@link BGIParseResult} corresponding to the specified byte array
   * @since 0.1.1
   */
  public static BGIParseResult<BGIData> parse(byte[] data) {
    var parser = new BGIParser(data);
    return parser.parse();
  }

  /**
   * Parses the specified item stack's custom data component into a {@link BGIParseResult} object. If the stack
   * does not have the "bgi" tag in its custom data, then an error will be returned.
   *
   * @param itemStack Item stack to find the tag in
   * @return a {@link BGIParseResult} corresponding to the specified item stack
   */
  public static BGIParseResult<BGIData> parse(ItemStack itemStack) {
    NbtCompound stackNbt = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

    if (stackNbt != null && stackNbt.contains(BGI_TAG, NbtElement.BYTE_ARRAY_TYPE)) {
      return BGIParser.parse(stackNbt.getByteArray(BGI_TAG));
    }

    return BGIParseResult.error(ErrorCode.MISSING_TAG);
  }

  /**
   * Attempts to determine the matching {@link BGIField} from the given hash code. This is most useful for debugging
   * purposes as it will loop over all the fields to match the hash.
   *
   * @param hashCode The field name hash code to match
   * @return if present, the matching {@link BGIField}
   */
  public static Optional<BGIField> getFieldFromHash(int hashCode) {
    for (var field : BGIField.values()) {
      if (field.name().hashCode() == hashCode) {
        return Optional.of(field);
      }
    }
    return Optional.empty();
  }

  private BGIParseResult<BGIData> parse() {
    if (originalData.length < 5) {
      return BGIParseResult.error(ErrorCode.DATA_TOO_SHORT);
    }

    if (originalData[0] != 7) {
      return BGIParseResult.error(ErrorCode.GOBLINLESS);
    }

    buffer.position(1); // Skip initial 7

    try {
      int dataVersion = getShort();
      var properties = parseProperties();
      return BGIParseResult.success(new BGIData(dataVersion, properties));
    } catch (BGIParseException e) {
      return BGIParseResult.error(e);
    }
  }

  private Map<Integer, Object> parseProperties() throws BGIParseException {
    int numAttributes = getShort();

    var properties = new HashMap<Integer, Object>();
    for (int i = 0; i < numAttributes; i++) {
      int key = getInt();
      byte typeId = getByte();

      Object value = switch (typeId) {
        case 2 -> getVarInt();
        case 3 -> getString();
        case 4 -> getStringArray();
        case 8 -> getDouble();
        case 9 -> getStream();
        case 10 -> getBoolean();
        case 11 -> getUUID();
        default -> throw new BGIParseException("Could not parse data type [" + typeId + "]", ErrorCode.UNKNOWN_DATA_TYPE);
      };

      properties.put(key, value);
    }

    return properties;
  }

  private byte getByte() {
    return buffer.get();
  }

  private short getShort() {
    return buffer.getShort();
  }

  private double getDouble() {
    return buffer.getDouble();
  }

  private boolean getBoolean() {
    return getByte() != 0;
  }

  private String getString() {
    int length = getVarInt();
    byte[] strBytes = new byte[length];
    buffer.get(strBytes);
    return new String(strBytes, StandardCharsets.UTF_8);
  }

  private String[] getStringArray() {
    int count = getVarInt();
    String[] array = new String[count];
    for (int j = 0; j < count; j++) {
      array[j] = getString();
    }
    return array;
  }

  private byte[] getStream() {
    int length = getVarInt();
    byte[] streamBytes = new byte[length];
    buffer.get(streamBytes);
    return streamBytes;
  }

  private int getInt() {
    return buffer.getInt();
  }

  private int getVarInt() {
    int value = 0;
    int shift = 0;

    while (buffer.hasRemaining()) {
      byte b = getByte();

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

  private UUID getUUID() {
    long mostSigBits = buffer.getLong();
    long leastSigBits = buffer.getLong();
    return new UUID(mostSigBits, leastSigBits);
  }

  public static void main(String[] args) {
    byte[] item1 = {
        7, 0, 1, 0, 20, -122, -31, 26, 32, 8, 64, 57, 50, -83, 74, 64, 57, -9, -113, -89, -67, -85, 3, 9, 82, 65, 82, 69, 95, 71, 69, 65, 82, -108, 65, 47, 58, 4, 0, -103, -106, 9, -23, 9, 25, 8, 69, 78, 67, 72,
        65, 78, 84, 83, 1, 1, 8, 69, 78, 67, 72, 65, 78, 84, 83, 4, 0, 0, 0, 0, -98, -12, -13, -1, 2, 1, -88, -18, -67, -57, 3, 17, 67, 72, 69, 70, 95, 67, 72, 69, 83, 84, 80, 76, 65, 84, 69, 95, 51, -86,
        -14, 60, -67, 8, 64, 77, -98, -23, -63, 106, -93, -45, -84, -126, 1, -28, 2, -24, 7, -48, 7, 51, 73, 3, 17, 67, 72, 69, 70, 95, 67, 72, 69, 83, 84, 80, 76, 65, 84, 69, 95, 51, -27, -110, 92, -84, 3, 14, 80, 79,
        87, 68, 69, 82, 124, 67, 79, 82, 69, 95, 84, 51, -17, 17, -96, -92, 3, 9, 110, 101, 116, 104, 101, 114, 105, 116, 101, -7, -91, 118, 48, 9, 27, 1, 2, 11, 82, 117, 110, 101, 99, 97, 114, 118, 105, 110, 103, 11, 82, 117,
        110, 101, 99, 97, 114, 118, 105, 110, 103, 0, 0, 1, 64, 66, 3, 8, 83, 69, 84, 95, 67, 72, 69, 70, 0, 35, -66, -10, 4, 1, 35, 38, 55, 77, 97, 100, 101, 32, 102, 114, 111, 109, 32, 111, 110, 108, 121, 32, 116, 104,
        101, 32, 102, 105, 110, 101, 115, 116, 32, 112, 111, 116, 97, 116, 111, 46, 0, 36, 114, -117, 3, 38, 60, 116, 105, 101, 114, 45, 99, 111, 108, 111, 114, 62, 74, 111, 117, 114, 110, 101, 121, 109, 97, 110, 32, 67, 104, 101, 102, 115,
        32, 84, 97, 116, 101, 114, 32, 84, 111, 112, 0, 39, 73, -30, 8, 64, 8, 0, 0, 0, 0, 0, 0, 1, 70, 72, 83, 3, 3, 118, 101, 120, 12, 71, 3, -8, 3, 19, -62, -85, 32, 67, 111, 111, 107, 105, 110, 103, 32,
        65, 114, 109, 111, 114, 32, -62, -69, 34, -107, 32, -1, 8, 64, 52, 0, 0, 0, 0, 0, 0, 40, 59, -64, -26, 3, 5, 65, 82, 77, 79, 82
    };
    BGIParseResult<BGIData> result = BGIParser.parse(item1);
    if (result.isSuccess()) {
      var data = result.result().get();

      System.out.println(data.getString("ItemId"));

      System.out.println("Num Attributes: " + data.getNumAttributes());

      var attrs = data.getAttributes();
      System.out.println(attrs.get(-804834487));
      for (var key : attrs.keySet()) {
        boolean found = false;
        for (BGIField field : BGIField.values()) {
          if (field.name().hashCode() == key) {
            var value = attrs.get(key);

            if (value instanceof String[]) {
              System.out.println(field + " = " + Arrays.toString((String[])value));
            } else {
              System.out.println(field + " = " + value);
            }
            found = true;
            break;
          }
        }

        if (!found) {
          System.out.println(key + " = " + attrs.get(key));
        }
      }
    } else {
      System.out.println(result.error().get().getMessage());
    }
  }
}
