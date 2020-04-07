/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.io;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.READ;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

import org.mozilla.universalchardet.UniversalDetector;

public final class IOUtils {
  private IOUtils() {}

  // Code based on:
  // https://stackoverflow.com/questions/30507653/how-to-check-whether-file-is-gzip-or-not-in-java
  public static boolean isGZipped(InputStream in) {
    if (!in.markSupported()) {
      throw new IllegalArgumentException("in must support mark");
    }
    in.mark(2);

    try {
      final int magic = in.read() & 0xff | ((in.read() << 8) & 0xff00);
      in.reset();

      return magic == GZIPInputStream.GZIP_MAGIC;
    } catch (IOException e) {
      e.printStackTrace(System.err);
      return false;
    }
  }
  
  public static Path extractIfNeeded(Path file) throws IOException {
    try (BufferedInputStream in = new BufferedInputStream(newInputStream(file, READ))) {
      if (isGZipped(in)) {
        final String name = file.getFileName().toString();
        final Path tempFile = Files.createTempFile("seda_" + name, ".fasta");
        
        try (GZIPInputStream gis = new GZIPInputStream(in)) {
          copy(gis, tempFile, REPLACE_EXISTING);
          return tempFile;
        }
      } else {
        return file;
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Error uncompressing file: " + file, e);
    }
  }

  public static InputStream createInputStream(Path file) throws IOException {
    final BufferedInputStream in = new BufferedInputStream(newInputStream(file, READ));

    return isGZipped(in) ? new GZIPInputStream(in) : in;
  }

  public static Reader createReader(Path file, Charset charset) throws IOException {
    return new InputStreamReader(
      createInputStream(file),
      charset == null ? Charset.defaultCharset() : charset
    );
  }

  public static Charset detectCharset(Path file) throws IOException {
    try (InputStream input = createInputStream(file)) {
      final String charsetName = UniversalDetector.detectCharset(input);
      return charsetName == null ? Charset.defaultCharset() : Charset.forName(charsetName);
    }
  }
  
  public static NumberedLineReader createNumberedLineReader(Path file, Charset charset) throws IOException {
    return new NumberedLineReader(createInputStream(file), charset);
  }
}
