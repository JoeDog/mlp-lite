package org.joedog.util.config;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joedog.util.io.FileUtils;
import org.joedog.util.io.FileLineIterator;
import org.joedog.util.NumberUtils;

public final class INI {
  /*public static List<Object[]> get(String name) {
    return Columns.get(name, ",");
  }*/

  public static Map<String, HashMap<String, Object>> read(String name, String sep) {
    int     index   = 0;
    String  section = "default";
    Pattern pattern = Pattern.compile("^\\[([^\\]]*)\\]$", Pattern.CASE_INSENSITIVE);
    Map<String, HashMap<String, Object>> ini = new HashMap<String, HashMap<String, Object>>();

    FileLineIterator iter = FileUtils.fileLineIterator(name);
    for (String line : iter) {
      line = line.trim();
      if (line.startsWith("#")) {
        continue;
      }
      int idx = line.indexOf("#");
      if (idx >= 0) {
        line = line.substring(0, idx);
        line = line.trim();
      }
      Matcher match = pattern.matcher(line);

      boolean found = match.find();
      if (found) {
        section = INI.dequote(match.group(1));
        continue;
      }

      if (line.isEmpty()) {
        continue;
      }

      if (line.contains(sep)) {
        if (! ini.containsKey(section)) {
          ini.put(section, new HashMap<String, Object>());  
        }
        String[] res = line.split("["+sep+"]", 0);
        ini.get(section).put(res[0].trim(), res[1].trim());
      } else {
        if (! ini.containsKey(section)) {
          index = 0;
          ini.put(section, new HashMap<String, Object>());  
        }
        ini.get(section).put(""+index, line.trim());  
        index++;
      }
/*********************************************************
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
*********************************************************/
    }    
    return ini;
  }

  public static String dequote(String str) {
    str = str.replaceAll("^\"|\"$", "");
    str = str.replaceAll("^\'|\'$", "");
    return str;
  }
}
