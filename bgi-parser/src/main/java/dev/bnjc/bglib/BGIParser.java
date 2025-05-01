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
import java.util.HashMap;
import java.util.Map;

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
      int key = getVarInt();
      byte typeId = getByte();

      Object value = switch (typeId) {
        case 2 -> getVarInt();
        case 3 -> getString();
        case 4 -> getStringArray();
        case 8 -> getDouble();
        case 9 -> getStream();
        case 10 -> getBoolean();
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
}
