package dev.bnjc.bglib;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AllItemsTest {
  private static Map<Integer, List<Object>> unknownKeys = new HashMap<>();
  private static Set<BGIField> unusedFields = new HashSet<>();

  public static void main(String[] args) throws IOException, URISyntaxException {
    byte[] fileBytes = Files.readAllBytes(Path.of(BGIParser.class.getClassLoader().getResource("itemBGI.bytes").toURI()));
    byte[] separator = "<<<END>>>".getBytes(StandardCharsets.UTF_8);

    List<byte[]> chunks = splitBySeparator(fileBytes, separator);

    unusedFields.addAll(Arrays.asList(BGIField.values()));

    for (byte[] chunk : chunks) {
      doParse(chunk);
    }

    unknownKeys.forEach((integer, objects) -> {
      System.out.println(integer);
      System.out.println(objects);
    });

    System.out.println("\nUnused Fields");
    Arrays.stream(unusedFields.toArray()).sorted().forEach(System.out::println);
  }

  private static List<byte[]> splitBySeparator(byte[] input, byte[] separator) {
    List<byte[]> result = new ArrayList<>();
    int start = 0;
    for (int i = 0; i <= input.length - separator.length; i++) {
      boolean match = true;
      for (int j = 0; j < separator.length; j++) {
        if (input[i + j] != separator[j]) {
          match = false;
          break;
        }
      }
      if (match) {
        result.add(Arrays.copyOfRange(input, start, i));
        start = i + separator.length;
        i += separator.length - 1;
      }
    }
    // Add remaining part
    if (start < input.length) {
      result.add(Arrays.copyOfRange(input, start, input.length));
    }
    return result;
  }

  private static void doParse(byte[] bytes) {
    var result = BGIParser.parse(bytes);
    if (result.isSuccess()) {
      var data = result.result().get();
      System.out.println(data);
      for (Integer key : data.getAttributes().keySet()) {
        var maybeField = BGIField.fromHashCode(key);
        maybeField.ifPresent(bgiField -> unusedFields.remove(bgiField));
      }
    }
  }

  private static void checkForMissingKeys(BGIData data) {
    var attrs = data.getAttributes();

    for (var key : attrs.keySet()) {
      boolean found = false;
      for (BGIField field : BGIField.values()) {
        if (field.name().hashCode() == key) {
          found = true;
          break;
        }
      }

      if (!found) {
        unknownKeys.compute(key, (hash, objs) -> {
          if (objs == null) {
            objs = new ArrayList<>();
          }
          objs.add(attrs.get(key));
          return objs;
        });
        System.out.println("- Unknown key[" + key + "]: " + attrs.get(key));
      }
    }
  }
}
