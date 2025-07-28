package dev.bnjc.bglib;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * A helper class used to simulate how the server would convert the old 
 */
public final class BGIWriter {
  private final Deque<Byte> bytes;
  private final short dataVersion;
  private short propertyCount;

  public BGIWriter(short dataVersion) {
    this.bytes = new LinkedList<>();
    this.dataVersion = dataVersion;
    this.propertyCount = 0;
  }

  public BGIWriter addInt(String key, int val) {
    putPropertyHeader(key, BGIType.INTEGER);
    putVarInt(val);
    return this;
  }

  public BGIWriter addString(String key, String val) {
    putPropertyHeader(key, BGIType.STRING);
    putString(val);
    return this;
  }

  public BGIWriter addStringArray(String key, String[] val) {
    putPropertyHeader(key, BGIType.STRING_ARRAY);
    putVarInt(val.length);
    for (String s : val) {
      putString(s);
    }
    return this;
  }

  public BGIWriter addDouble(String key, double val) {
    putPropertyHeader(key, BGIType.DOUBLE);
    putDouble(val);
    return this;
  }

  public BGIWriter addBoolean(String key, boolean val) {
    putPropertyHeader(key, BGIType.BOOLEAN);
    putBoolean(val);
    return this;
  }

  public BGIWriter addStream(String key, byte[] byteArray) {
    putPropertyHeader(key, BGIType.STREAM);
    putVarInt(byteArray.length);
    putBytes(byteArray);
    return this;
  }

  public byte[] write() {
    byte[] result = new byte[bytes.size() + 5];

    int i = 0;

    // Add gobliness
    result[i++] = 7;

    // Add data version
    for (byte b : ByteBuffer.allocate(2).putShort(dataVersion).array()) {
      result[i++] = b;
    }

    // Add number of properties
    for (byte b : ByteBuffer.allocate(2).putShort(propertyCount).array()) {
      result[i++] = b;
    }

    // Insert the rest of the data
    for (byte b : bytes) {
      result[i++] = b;
    }

    return result;
  }

  public void writeToCustomData(ItemStack itemStack) {
    var bytes = write();

    var component = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
    component = component.apply((stackNbt) -> {
      stackNbt.putByteArray(BGIParser.BGI_TAG, bytes);
    });
    itemStack.set(DataComponentTypes.CUSTOM_DATA, component);
  }

  private void putPropertyHeader(String key, BGIType type) {
    putInt(key.hashCode());
    putByte(type.typeId);

    propertyCount++;
  }

  private void putByte(byte val) {
    bytes.addLast(val);
  }

  private void putBytes(byte[] byteArray) {
    for (byte b : byteArray) {
      bytes.addLast(b);
    }
  }

  private void putShort(short val) {
    putBytes(ByteBuffer.allocate(2).putShort(val).array());
  }

  private void putInt(int val) {
    putBytes(ByteBuffer.allocate(4).putInt(val).array());
  }

  private void putVarInt(int val) {
    while ((val & 0xFFFFFF80) != 0) {
      bytes.addLast((byte) ((val & 0x7F) | 0x80));
      val >>>= 7;
    }
    bytes.addLast((byte) (val & 0x7F));
  }

  private void putDouble(double val) {
    putBytes(ByteBuffer.allocate(8).putDouble(val).array());
  }

  private void putString(String val) {
    byte[] strBytes = val.getBytes(StandardCharsets.UTF_8);
    putVarInt(strBytes.length);
    putBytes(strBytes);
  }

  private void putBoolean(boolean val) {
    putByte((byte) (val ? 1 : 0));
  }
}
