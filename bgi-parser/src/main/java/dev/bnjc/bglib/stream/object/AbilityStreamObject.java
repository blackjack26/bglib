package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.utils.ByteParser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AbilityStreamObject extends StreamObject {
  private final List<Ability> abilities;

  public AbilityStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);

    this.abilities = new ArrayList<>();

    this.parseBuffer();
  }

  public List<Ability> getAbilities() {
    return abilities;
  }

  public boolean hasAbilities() {
    return !abilities.isEmpty();
  }

  private void parseBuffer() {
    boolean hasAbilities = ByteParser.getBoolean(this.buffer);

    if (hasAbilities) {
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
  }

  @Override
  public String toString() {
    return "AbilityStreamObject{" +
        "abilities=" + abilities +
        "}";
  }

  public record Ability(String id, String castMode, List<Modifier> modifiers) {}
  public record Modifier(String name, double value) {}
}
