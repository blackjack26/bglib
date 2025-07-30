package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.utils.ByteParser;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GemSocketStreamObject extends StreamObject {
  private final List<String> emptySlots;
  private final List<GemstoneEntry> gemstones;

  public GemSocketStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);

    this.emptySlots = new ArrayList<>();
    this.gemstones = new ArrayList<>();

    this.parseBuffer();
  }

  public List<String> getEmptySlots() {
    return emptySlots;
  }

  public List<GemstoneEntry> getGemstones() {
    return gemstones;
  }

  private void parseBuffer() {
    boolean hasEmptySlots = ByteParser.getBoolean(this.buffer);
    if (hasEmptySlots) {
      this.emptySlots.addAll(Arrays.stream(ByteParser.getStringArray(this.buffer)).toList());
    }

    int entryCount = ByteParser.getVarInt(this.buffer);
    for (int i = 0; i < entryCount; i++) {
      String name = ByteParser.getString(this.buffer);
      UUID history = ByteParser.getUUID(this.buffer);

      String id = null;
      if (ByteParser.getBoolean(this.buffer)) {
        id = ByteParser.getString(this.buffer);
      }

      String type = null;
      if (ByteParser.getBoolean(this.buffer)) {
        type = ByteParser.getString(this.buffer);
      }

      String color = null;
      if (ByteParser.getBoolean(this.buffer)) {
        color = ByteParser.getString(this.buffer);
      }

      this.gemstones.add(new GemstoneEntry(name, history, id, type, color));
    }
  }

  @Override
  public String toString() {
    return "GemSocketStreamObject{" +
        "emptySlots=" + emptySlots +
        ", gemstones=" + gemstones +
        '}';
  }

  public record GemstoneEntry(String name, UUID history, @Nullable String id, @Nullable String type, @Nullable String color) {}
}
