package org.sing_group.seda.util;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

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

  public static Optional<Path> getTemporaryClipboardFile() {
    try {
      return writeClipboardToPath(Files.createTempFile("seda-temporary-clipboard-", ".fasta"));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  public static Optional<Path> writeClipboardToPath(Path path) {
    try {
      String data = (String) Toolkit.getDefaultToolkit()
        .getSystemClipboard().getData(DataFlavor.stringFlavor);

      if (!data.trim().isEmpty()) {
        Files.write(path, data.getBytes());
        return Optional.of(path);
      } else {
        return Optional.empty();
      }
    } catch (HeadlessException | UnsupportedFlavorException | IOException e) {
      return Optional.empty();
    }
  }
}
