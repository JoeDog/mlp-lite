package org.joedog.util.config;

import java.util.ArrayList;
import java.util.List;

import org.joedog.util.io.FileUtils;
import org.joedog.util.io.FileLineIterator;
import org.joedog.util.NumberUtils;

public final class Columns {
  public static List<Object[]> get(String name) {
    return Columns.read(name, ",");
  }

  public static List<Object[]> read(String name, String sep) {
    List<Object[]>   rows = new ArrayList<>();
    FileLineIterator iter = FileUtils.fileLineIterator(name);
    for (String line : iter) {
      line = line.trim();
      if (line.startsWith("#")) {
        continue;
      }
      if (! line.contains(sep)) {
        continue;
      }
      String[] res = line.split("["+sep+"]", 0);
      Object[] obj = new Object[res.length];
      for (int i = 0; i < res.length; i++) {
        res[i] = res[i].trim();
        if (NumberUtils.isNumeric(res[i])) {
          if (res[i].contains(".")) {
            obj[i] = Double.parseDouble(res[i]);
          } else {
            obj[i] = Integer.parseInt(res[i]);
          }
        } else {
          obj[i] = res[i];
        }
      }
      rows.add(obj);
    }    
    return rows;
  }
}
