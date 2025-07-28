package dev.bnjc.bglib;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A class representing the data extracted from the BGI byte array.
 *
 * @since 0.1.0
 * @author Jack Grzechowiak
 */
public class BGIData {
  private final int dataVersion;
  private final Map<Integer, Object> properties;

  /**
   * Constructs a BGIData object with the given configuration. This is used internally as the result of parsing
   * the BGI byte array.
   *
   * @param dataVersion The data version of this data
   * @param properties The keyed properties found when parsing the data. The key of these properties match the
   *                   {@link #hashCode()} of key's string name. A subset of these keys can be found in {@link BGIField}.
   */
  public BGIData(int dataVersion, Map<Integer, Object> properties) {
    this.dataVersion = dataVersion;
    this.properties = properties;
  }

  /**
   * Returns the data version found and used to parse this data
   *
   * @return the BGI data version
   * @since 0.1.3
   */
  public int getDataVersion() {
    return dataVersion;
  }

  /**
   * Returns the number of attributes found
   *
   * @return the number of attributes
   * @since 0.1.7
   */
  public int getNumAttributes() {
    return properties.size();
  }

  /**
   * Returns a generic object property found with the given key (if any)
   *
   * @param key The property name
   * @return An {@link Object} property found using the given key (if any)
   * @since 0.1.0
   */
  public Optional<Object> getAttribute(String key) {
    return Optional.ofNullable(properties.getOrDefault(key.hashCode(), null));
  }

  /**
   * Returns a generic object property found using the given BGI field name
   *
   * @param field The BGI field to search
   * @return An {@link Object} property found using the given BGI field
   * @since 0.1.0
   */
  public Optional<Object> getAttribute(BGIField field) {
    return getAttribute(field.name());
  }

  /**
   * Returns a string property found using the given key (if any). If a property exists but is not a string,
   * then nothing will be returned.
   *
   * @param key The property name
   * @return A {@link String} property found using the given key (if any).
   * @since 0.1.0
   */
  public Optional<String> getString(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof String)
        .map(value -> (String) value);
  }

  /**
   * Returns a string property found using the given BGI field name (if any). If a property exists but is
   * not a string, then nothing will be returned.
   *
   * @param field The BGI field to search
   * @return A {@link String} property found using the given BGI field (if any).
   * @since 0.1.0
   */
  public Optional<String> getString(BGIField field) {
    return getString(field.name());
  }

  /**
   * Returns an integer property found using the given key (if any). If a property exists but is not an integer,
   * then nothing will be returned.
   *
   * @param key The property name
   * @return An {@link Integer} property found using the given key (if any).
   * @since 0.1.0
   */
  public Optional<Integer> getInt(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof Integer)
        .map(value -> (Integer) value);
  }

  /**
   * Returns an integer property found using the given BGI field name (if any). If a property exists but is
   * not an integer, then nothing will be returned.
   *
   * @param field The BGI field to search
   * @return An {@link Integer} property found using the given BGI field (if any).
   * @since 0.1.0
   */
  public Optional<Integer> getInt(BGIField field) {
    return getInt(field.name());
  }

  /**
   * Returns a double property found using the given key (if any). If a property exists but is not a double,
   * then nothing will be returned.
   *
   * @param key The property name
   * @return A {@link Double} property found using the given key (if any).
   * @since 0.1.0
   */
  public Optional<Double> getDouble(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof Double)
        .map(value -> (Double) value);
  }

  /**
   * Returns a double property found using the given BGI field name (if any). If a property exists but is
   * not a double, then nothing will be returned.
   *
   * @param field The BGI field to search
   * @return A {@link Double} property found using the given BGI field (if any).
   * @since 0.1.0
   */
  public Optional<Double> getDouble(BGIField field) {
    return getDouble(field.name());
  }

  /**
   * Returns a boolean property found using the given key (if any). If a property exists but is not a boolean,
   * then nothing will be returned.
   *
   * @param key The property name
   * @return A {@link Boolean} property found using the given key (if any).
   * @since 0.1.0
   */
  public Optional<Boolean> getBoolean(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof Boolean)
        .map(value -> (Boolean) value);
  }

  /**
   * Returns a boolean property found using the given BGI field name (if any). If a property exists but is
   * not a boolean, then nothing will be returned.
   *
   * @param field The BGI field to search
   * @return A {@link Boolean} property found using the given BGI field (if any).
   * @since 0.1.0
   */
  public Optional<Boolean> getBoolean(BGIField field) {
    return getBoolean(field.name());
  }

  /**
   * Returns a string array property found using the given key (if any). If a property exists but is not a string
   * array, then nothing will be returned.
   *
   * @param key The property name
   * @return A property with an array of {@link String} values found using the given key (if any).
   * @since 0.1.0
   */
  public Optional<String[]> getStringArray(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof String[])
        .map(value -> (String[]) value);
  }

  /**
   * Returns a string array property found using the given BGI field name (if any). If a property exists but is
   * not a string array, then nothing will be returned.
   *
   * @param field The BGI field to search
   * @return A property with an array of {@link String} values found using the given BGI field (if any).
   * @since 0.1.0
   */
  public Optional<String[]> getStringArray(BGIField field) {
    return getStringArray(field.name());
  }

  /**
   * Returns a stream property found using the given key (if any). If a property exists but is not a stream,
   * then nothing will be returned.
   *
   * @param key The property name
   * @return A stream property found using the given key (if any).
   * @since 0.1.2
   */
  public Optional<byte[]> getStream(String key) {
    return getAttribute(key)
        .filter(value -> value instanceof byte[])
        .map(value -> (byte[]) value);
  }

  /**
   * Returns a stream property found using the given BGI field name (if any). If a property exists but is
   * not a stream, then nothing will be returned.
   *
   * @param field The BGI field to search
   * @return A stream property found using the given BGI field (if any).
   * @since 0.1.2
   */
  public Optional<byte[]> getStream(BGIField field) {
    return getStream(field.name());
  }

  @Override
  public String toString() {
    return "BGIData{" +
        "dataVersion=" + dataVersion +
        ", properties=" + properties +
        '}';
  }

  public HashMap<Integer, Object> getAttributes() {
    return new HashMap<>(this.properties);
  }
}
