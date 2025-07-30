package dev.bnjc.bglib.stream;

import dev.bnjc.bglib.BGIField;
import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;
import dev.bnjc.bglib.stream.object.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BGIStreamParser {
  private static final List<Integer> HSTRY_NAMES = new ArrayList<>();

  static {
    for (var field : BGIField.values()) {
      if (field.name().startsWith("HSTRY")) {
        HSTRY_NAMES.add(field.key());
      }
    }
  }

  public static StreamObject parse(int key, byte[] streamBytes) throws BGIParseException {
    if (streamBytes.length == 0) throw new BGIParseException("Empty stream data");

    ByteBuffer buffer = ByteBuffer.wrap(streamBytes);

    if (BGIField.COMMANDS.key() == key) {
      return new CommandStreamObject(key, buffer);
    }

    if (BGIField.ABILITY.key() == key) {
      return new AbilityStreamObject(key, buffer);
    }

    if (BGIField.ARROW_PARTICLES.key() == key) {
      return new ArrowParticlesStreamObject(key, buffer);
    }

    if (BGIField.CONSUMABLE_BUFFS.key() == key) {
      return new ConsumableBuffStreamObject(key, buffer);
    }

    if (BGIField.GEM_SOCKETS.key() == key) {
      return new GemSocketStreamObject(key, buffer);
    }

    if (HSTRY_NAMES.contains(key)) {
      return new HistoryStreamObject(key, buffer);
    }

    throw new BGIParseException("Unknown stream type for key: " + key + " " + Arrays.toString(streamBytes), ErrorCode.UNKNOWN_STREAM_TYPE);
  }
}
