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

import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.isRegularFile;
import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;
import static org.sing_group.seda.io.FastaReader.readFasta;
import static org.sing_group.seda.io.FastaWriter.writeFasta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.gui.reformat.LineBreakType;

public class LazyFileSequencesGroup implements SequencesGroup {
  private final String name;
  private final Path file;
  private final boolean isTempFile;
  private final List<Sequence> sequences;
  private Map<String, Object> properties;

  public LazyFileSequencesGroup(Path file) {
    this(file.getFileName().toString(), file);
  }

  public LazyFileSequencesGroup(String name, Path file) {
    if (!isRegularFile(file) && !isReadable(file)) {
      throw new IllegalArgumentException("file should be a regular and readable file");
    }

    this.name = requireNonNull(name, "name can't be null");
    this.file = file;
    this.isTempFile = false;
    this.properties = new HashMap<>();
    this.sequences = readFasta(this.file);
    if (!this.sequences.isEmpty()) {
      this.populateProperties();
    }
  }

  private void populateProperties() {
    LineBreakType lineBreakType = getLineBreakType(this.file);
    if (!this.properties.containsKey(PROPERTY_LINE_BREAK_OS) && !lineBreakType.equals(LineBreakType.defaultType())) {
      this.properties.put(PROPERTY_LINE_BREAK_OS, lineBreakType.getLineBreak());
    }
  }

  public LazyFileSequencesGroup(String name, Sequence... sequences) {
    this(name, emptyMap(), sequences);
  }

  public LazyFileSequencesGroup(String name, Map<String, Object> properties,  Sequence ... sequences) {
    try {
      this.name = name;
      this.properties = properties;
      this.file = Files.createTempFile("seda_" + name, ".fasta");
      this.file.toFile().deleteOnExit();
      this.isTempFile = true;

      writeFasta(this.file, sequences);

      this.sequences = readFasta(this.file);
      this.populateProperties();
    } catch (IOException e) {
      throw new RuntimeException("Unexpected error creating temporary file.", e);
    }
  }

  public Path getFile() {
    return file;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Stream<Sequence> getSequences() {
    return this.sequences.stream();
  }

  @Override
  public int getSequenceCount() {
    return this.sequences.size();
  }

  @Override
  public Map<String, Object> getProperties() {
    return this.properties;
  }

  @Override
  protected void finalize() throws Throwable {
    if (this.isTempFile)
      Files.deleteIfExists(this.file);
  }

  private static LineBreakType getLineBreakType(Path file) {
    try (NumberedLineReader reader = new NumberedLineReader(file)) {
      NumberedLineReader.Line nline = reader.readLine();
      if (nline != null) {
        return LineBreakType.getLineBreakType(nline.getLineEnding());
      } else {
        throw new RuntimeException("Error reading file: " + file.toString());
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading file: " + file.toString());
    }
  }
}
