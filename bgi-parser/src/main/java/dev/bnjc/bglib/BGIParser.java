package dev.bnjc.bglib;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;
import dev.bnjc.bglib.utils.ByteParser;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
      int dataVersion = ByteParser.getShort(buffer);
      var properties = parseProperties();
      return BGIParseResult.success(new BGIData(dataVersion, properties));
    } catch (BGIParseException e) {
      return BGIParseResult.error(e);
    }
  }

  private Map<Integer, Object> parseProperties() throws BGIParseException {
    int numAttributes = ByteParser.getShort(buffer);

    var properties = new HashMap<Integer, Object>();
    for (int i = 0; i < numAttributes; i++) {
      int key = ByteParser.getInt(buffer);
      try {
        Object value = ByteParser.getByType(key, buffer);
        properties.put(key, value);
      } catch (Exception e) {
        properties.put(key, null);
      }
    }

    return properties;
  }
}
