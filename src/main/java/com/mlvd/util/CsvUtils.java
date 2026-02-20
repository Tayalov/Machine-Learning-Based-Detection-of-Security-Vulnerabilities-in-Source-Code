package com.mlvd.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CsvUtils {
  private CsvUtils() {
  }

  public static List<Map<String, String>> parseSimpleCsv(Path filePath) throws IOException {
    String content = Files.readString(filePath).trim();
    String[] lines = content.split("\\R");
    if (lines.length == 0) {
      return List.of();
    }

    String[] header = splitRow(lines[0]);
    List<Map<String, String>> rows = new ArrayList<>();

    for (int i = 1; i < lines.length; i += 1) {
      if (lines[i].trim().isEmpty()) {
        continue;
      }
      String[] cols = splitRow(lines[i]);
      Map<String, String> row = new HashMap<>();
      for (int j = 0; j < header.length; j += 1) {
        String value = j < cols.length ? cols[j] : "";
        row.put(header[j], value);
      }
      rows.add(row);
    }

    return rows;
  }

  private static String[] splitRow(String line) {
    String[] cols = line.split(",", -1);
    for (int i = 0; i < cols.length; i += 1) {
      cols[i] = cols[i].trim();
    }
    return cols;
  }
}
