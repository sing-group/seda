package org.sing_group.seda.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class FileUtils {

  public static void writeMap(File file, Map<String, String> map) throws IOException {
    if (file != null) {
      FileWriter fw = new FileWriter(file);
      for (Entry<String, String> entry : map.entrySet()) {
        fw.write(entry.getKey());
        fw.write("\t");
        fw.write(entry.getValue());
        fw.write("\n");
      }
      fw.close();
    }
  }
}
