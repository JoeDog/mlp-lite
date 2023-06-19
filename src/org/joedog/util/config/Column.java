package org.joedog.util.config;

import java.util.ArrayList;
import java.util.List;

import org.joedog.util.io.FileUtils;
import org.joedog.util.io.FileLineIterator;
import org.joedog.util.NumberUtils;

public final class Column {

  public static List<Object> read(String name) {
    List<Object>   rows = new ArrayList<>();
    FileLineIterator iter = FileUtils.fileLineIterator(name);
    for (String line : iter) {
      line = line.trim();
      if (line.startsWith("#")) {
        continue;
      }
      if (line.isEmpty()) {
        continue;
      }
      if (NumberUtils.isNumeric(line)) {
        if (line.contains(".")) {
          rows.add(Double.parseDouble(line));
        } else {
          rows.add(Integer.parseInt(line));
        }
      } else {
        rows.add(line);
      }
    }    
    return rows;
  }
}
