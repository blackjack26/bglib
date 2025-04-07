package dev.bnjc.bglib;

import java.util.Map;
import java.util.Optional;

public class BGIData {
  private final int dataVersion;
  private final Map<Integer, Object> properties;

  public BGIData(int dataVersion, Map<Integer, Object> properties) {
    this.dataVersion = dataVersion;
    this.properties = properties;
  }

  public Optional<Object> getAttribute(String key) {
    return Optional.ofNullable(properties.getOrDefault(key.hashCode(), null));
  }

  public Optional<Object> getAttribute(BGIField field) {
    return getAttribute(field.name());
  }

  public Optional<String> getString(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof String)
        .map(value -> (String) value);
  }

  public Optional<String> getString(BGIField field) {
    return getString(field.name());
  }

  public Optional<Integer> getInt(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof Integer)
        .map(value -> (Integer) value);
  }

  public Optional<Integer> getInt(BGIField field) {
    return getInt(field.name());
  }

  public Optional<Double> getDouble(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof Double)
        .map(value -> (Double) value);
  }

  public Optional<Double> getDouble(BGIField field) {
    return getDouble(field.name());
  }

  public Optional<Boolean> getBoolean(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof Boolean)
        .map(value -> (Boolean) value);
  }

  public Optional<Boolean> getBoolean(BGIField field) {
    return getBoolean(field.name());
  }

  public Optional<String[]> getStringArray(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof String[])
        .map(value -> (String[]) value);
  }

  public Optional<String[]> getStringArray(BGIField field) {
    return getStringArray(field.name());
  }

  @Override
  public String toString() {
    return "BGIData{" +
        "dataVersion=" + dataVersion +
        ", properties=" + properties +
        '}';
  }
}
