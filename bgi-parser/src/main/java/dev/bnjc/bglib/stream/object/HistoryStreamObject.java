package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.utils.ByteParser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistoryStreamObject extends StreamObject {
  private String statName;

  private List<HistoryEntry> ogStories;
  private List<GemStory> gemStories;
  private List<ModStory> modStories;

  private boolean unknown1;

  private HistoryStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);

    this.ogStories = new ArrayList<>();
    this.gemStories = new ArrayList<>();
    this.modStories = new ArrayList<>();

    this.unknown1 = false;
  }

  public static HistoryStreamObject from(int key, ByteBuffer buffer) throws BGIParseException {
    var hso = new HistoryStreamObject(key, buffer);
    hso.parse();
    return hso;
  }

  public void parse() throws BGIParseException {
    this.parseBuffer();
  }

  public String getStatName() {
    return statName;
  }

  public List<GemStory> getGemStories() {
    return gemStories;
  }

  public boolean hasGemStories() {
    return !gemStories.isEmpty();
  }

  public List<HistoryEntry> getOgStories() {
    return ogStories;
  }

  public boolean hasOGStories() {
    return !ogStories.isEmpty();
  }

  public List<ModStory> getModStories() {
    return modStories;
  }

  public boolean hasModStories() {
    return !modStories.isEmpty();
  }

  private void parseBuffer() throws BGIParseException {
    this.statName = ByteParser.getString(this.buffer);
    boolean hasOGStory = ByteParser.getBoolean(this.buffer);
    if (hasOGStory) {
      int entryCount = ByteParser.getVarInt(this.buffer);
      for (int i = 0; i < entryCount; i++) {
        String key = ByteParser.getString(this.buffer);
        Object value = ByteParser.getByType(key.hashCode(), this.buffer);
        this.ogStories.add(new HistoryEntry(key, value));
      }
    }

    boolean hasGemStory = ByteParser.getBoolean(this.buffer);
    if (hasGemStory) {
      int storyCount = ByteParser.getVarInt(this.buffer);
      for (int i = 0; i < storyCount; i++) {
        UUID key = ByteParser.getUUID(this.buffer);
        int entryCount = ByteParser.getVarInt(this.buffer);
        List<HistoryEntry> gems = new ArrayList<>();
        for (int e = 0; e < entryCount; e++) {
          String entryKey = ByteParser.getString(this.buffer);
          Object value = ByteParser.getByType(entryKey.hashCode(), this.buffer);
          gems.add(new HistoryEntry(entryKey, value));
        }
        this.gemStories.add(new GemStory(key, gems));
      }
    }

    this.unknown1 = ByteParser.getBoolean(this.buffer);

    boolean hasModStory = ByteParser.getBoolean(this.buffer);
    if (hasModStory) {
      int storyCount = ByteParser.getVarInt(this.buffer);
      for (int i = 0; i < storyCount; i++) {
        UUID key = ByteParser.getUUID(this.buffer);
        int entryCount = ByteParser.getVarInt(this.buffer);
        List<HistoryEntry> mods = new ArrayList<>();
        for (int e = 0; e < entryCount; e++) {
          String entryKey = ByteParser.getString(this.buffer);
          Object value = ByteParser.getByType(entryKey.hashCode(), this.buffer);
          mods.add(new HistoryEntry(entryKey, value));
        }
        this.modStories.add(new ModStory(key, mods));
      }
    }
  }

  @Override
  public String toString() {
    return "HistoryStreamObject{" +
        "statName='" + statName + '\'' +
        ", ogStories=" + ogStories +
        ", gemStories=" + gemStories +
        ", modStories=" + modStories +
        ", unknown1=" + unknown1 +
        '}';
  }

  public record HistoryEntry(String key, Object value) {}
  public record GemStory(UUID key, List<HistoryEntry> entries) {}
  public record ModStory(UUID key, List<HistoryEntry> entries) {}
}
