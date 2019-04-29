/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.awt.Toolkit.getDefaultToolkit;
import static java.awt.datatransfer.DataFlavor.stringFlavor;
import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.write;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.awt.HeadlessException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class FileUtils {

  public static void writeMap(File file, Map<String, String> map) throws IOException {
    if (file != null) {
      StringBuilder sb = new StringBuilder();
      for (Entry<String, String> entry : map.entrySet()) {
        sb
          .append(entry.getKey())
          .append("\t")
          .append(entry.getValue())
          .append(lineSeparator());
      }
      write(file.toPath(), sb.toString().getBytes(UTF_8));
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
      String data = (String) getDefaultToolkit().getSystemClipboard().getData(stringFlavor);

      if (!data.trim().isEmpty()) {
        write(path, data.replace("\n", System.lineSeparator()).getBytes(UTF_8));
        return of(path);
      } else {
        return empty();
      }
    } catch (HeadlessException | UnsupportedFlavorException | IOException e) {
      return Optional.empty();
    }
  }

  public static void deleteIfExists(File directory) throws IOException {
    deleteIfExists(directory.toPath());
  }

  public static void deleteIfExists(Path directory) throws IOException {
    if (Files.exists(directory)) {
      if (!Files.isDirectory(directory)) {
        throw new IllegalArgumentException("input path must be a directory");
      } else {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
            Files.delete(file);

            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc)
            throws IOException {
            Files.delete(dir);

            return FileVisitResult.CONTINUE;
          }
        });
      }
    }
  }
}
