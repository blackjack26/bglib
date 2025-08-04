package dev.bnjc.bglib.stream;

import dev.bnjc.bglib.BGIField;
import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.stream.object.*;

import java.nio.ByteBuffer;

public final class BGIStreamParser {
  public static StreamObject parse(int key, byte[] streamBytes) throws BGIParseException {
    if (streamBytes.length == 0) throw new BGIParseException("Empty stream data");

    ByteBuffer buffer = ByteBuffer.wrap(streamBytes);

    if (BGIField.COMMANDS.key() == key) {
      return CommandStreamObject.from(key, buffer);
    }

    if (BGIField.ABILITY.key() == key) {
      return AbilityStreamObject.from(key, buffer);
    }

    if (BGIField.ARROW_PARTICLES.key() == key) {
      return ArrowParticlesStreamObject.from(key, buffer);
    }

    if (BGIField.CONSUMABLE_BUFFS.key() == key) {
      return ConsumableBuffStreamObject.from(key, buffer);
    }

    if (BGIField.GEM_SOCKETS.key() == key) {
      return GemSocketStreamObject.from(key, buffer);
    }

    return HistoryStreamObject.from(key, buffer);
  }
}
