package dev.bnjc;

import java.util.Map;
import java.util.Optional;

public class BGIData {
  private final int dataVersion;
  private final Map<Integer, Object> attributes;

  public BGIData(int dataVersion, Map<Integer, Object> attributes) {
    this.dataVersion = dataVersion;
    this.attributes = attributes;
  }

  public Optional<String> getItemId() {
    return getString(BGIField.ITEM_ID.name());
  }

  public Optional<Object> getAttribute(String key) {
    return Optional.ofNullable(attributes.getOrDefault(key.hashCode(), null));
  }

  // Specialized methods for each type
  public Optional<String> getString(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof String)
        .map(value -> (String) value);
  }

  public Optional<Integer> getInt(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof Integer)
        .map(value -> (Integer) value);
  }

  public Optional<Double> getDouble(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof Double)
        .map(value -> (Double) value);
  }

  public Optional<Boolean> getBoolean(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof Boolean)
        .map(value -> (Boolean) value);
  }

  public Optional<String[]> getStringArray(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof String[])
        .map(value -> (String[]) value);
  }

  @Override
  public String toString() {
    return "BGIData{" +
        "dataVersion=" + dataVersion +
        ", attributes=" + attributes +
        '}';
  }
}
