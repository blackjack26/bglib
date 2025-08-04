package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;
import dev.bnjc.bglib.utils.ByteParser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AbilityStreamObject extends VersionedStreamObject {
  private final List<Ability> abilities = new ArrayList<>();

  private AbilityStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);
  }

  public static AbilityStreamObject from(int key, ByteBuffer buffer) throws BGIParseException {
    var aso = new AbilityStreamObject(key, buffer);
    aso.parse();
    return aso;
  }

  public List<Ability> getAbilities() {
    return abilities;
  }

  public boolean hasAbilities() {
    return !abilities.isEmpty();
  }

  @Override
  protected void parseBuffer() throws BGIParseException {
    if (this.version > 1) {
      throw new BGIParseException("Unsupported ABILITY stream version [" + this.version + "]", ErrorCode.UNSUPPORTED_STREAM_VERSION);
    }

    int entryCount = ByteParser.getVarInt(this.buffer);
    for (int i = 0; i < entryCount; i++) {
      String id = ByteParser.getString(this.buffer);
      String castMode = ByteParser.getString(this.buffer);
      List<Modifier> modifiers = new ArrayList<>();

      int modCount = ByteParser.getVarInt(this.buffer);
      for (int m = 0; m < modCount; m++) {
        String name = ByteParser.getString(this.buffer);
        double value = ByteParser.getDouble(this.buffer);
        modifiers.add(new Modifier(name, value));
      }

      this.abilities.add(new Ability(id, castMode, modifiers));
    }
  }

  @Override
  public String toString() {
    return "AbilityStreamObject{" +
        "abilities=" + abilities +
        ", version=" + version +
        '}';
  }

  public record Ability(String id, String castMode, List<Modifier> modifiers) {}
  public record Modifier(String name, double value) {}
}
