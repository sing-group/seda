/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.seda.util;

import static java.lang.System.lineSeparator;

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
        fw.write(lineSeparator());
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
