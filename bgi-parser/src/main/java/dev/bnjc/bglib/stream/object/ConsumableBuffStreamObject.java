package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;
import dev.bnjc.bglib.utils.ByteParser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumableBuffStreamObject extends VersionedStreamObject {
  private final Map<String, List<Buff>> buffTypes;

  private ConsumableBuffStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);

    this.buffTypes = new HashMap<>();
  }

  public static ConsumableBuffStreamObject from(int key, ByteBuffer buffer) throws BGIParseException {
    var cbso = new ConsumableBuffStreamObject(key, buffer);
    cbso.parse();
    return cbso;
  }

  public Map<String, List<Buff>> getBuffTypes() {
    return buffTypes;
  }

  @Override
  protected void parseBuffer() throws BGIParseException {
    if (this.version > 1) {
      throw new BGIParseException("Unsupported CONSUMABLE_BUFFS stream version [" + this.version + "]", ErrorCode.UNSUPPORTED_STREAM_VERSION);
    }

    while (this.buffer.hasRemaining()) {
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
        ", version=" + version +
        '}';
  }

  public record Buff(double amountNew, double time, String stat, String key, String type, double loreMultiplier) {}
}
