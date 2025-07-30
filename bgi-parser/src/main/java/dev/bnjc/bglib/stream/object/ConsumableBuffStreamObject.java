package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.utils.ByteParser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumableBuffStreamObject extends StreamObject {
  private final Map<String, List<Buff>> buffTypes;

  public ConsumableBuffStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);

    this.buffTypes = new HashMap<>();

    this.parseBuffer();
  }

  public Map<String, List<Buff>> getBuffTypes() {
    return buffTypes;
  }

  private void parseBuffer() {
    int buffTypeCount = ByteParser.getVarInt(this.buffer);

    for (int b = 0; b < buffTypeCount; b++) {
      String type = ByteParser.getString(this.buffer);
      buffTypes.put(type, new ArrayList<>());

      int buffCount = ByteParser.getVarInt(this.buffer);
      for (int i = 0; i < buffCount; i++) {
        buffTypes.computeIfPresent(type, (s, buffs) -> {
          buffs.add(new Buff(
              ByteParser.getDouble(this.buffer),
              ByteParser.getDouble(this.buffer),
              ByteParser.getString(this.buffer),
              ByteParser.getString(this.buffer),
              ByteParser.getString(this.buffer),
              ByteParser.getDouble(this.buffer)
          ));
          return buffs;
        });
      }
    }
  }

  @Override
  public String toString() {
    return "ConsumableBuffStreamObject{" +
        "buffTypes=" + buffTypes +
        '}';
  }

  public record Buff(double amountNew, double time, String stat, String key, String type, double loreMultiplier) {}
}
