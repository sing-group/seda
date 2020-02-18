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

import static java.util.Objects.requireNonNull;
import static org.sing_group.seda.io.FastaWriter.writeFasta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
    if (!Files.isRegularFile(file) && !Files.isReadable(file)) {
      throw new IllegalArgumentException("file should be a regular and readable file");
    }

    this.name = requireNonNull(name, "name can't be null");
    this.file = file;
    this.isTempFile = false;
    this.properties = new HashMap<>();
    this.sequences = readFileSequences(this.file);
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
    this(name, Collections.emptyMap(), sequences);
  }

  public LazyFileSequencesGroup(String name, Map<String, Object> properties,  Sequence ... sequences) {
    try {
      this.name = name;
      this.properties = properties;
      this.file = Files.createTempFile("seda_" + name, ".fasta");
      this.file.toFile().deleteOnExit();
      this.isTempFile = true;

      writeFasta(this.file, sequences);

      this.sequences = readFileSequences(this.file);
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

  private static class SequenceBuilder {
    private final Path file;

    private long nameLocation;
    private int nameLength;

    private long descriptionLocation;
    private int descriptionLength;

    private long headerLocation;
    private int headerLength;

    private long chainLocation;
    private int chainLength;

    private int columnWidth;

    private Map<String, Object> properties;

    public SequenceBuilder(Path file) {
      this.file = file;

      this.clear();
    }

    public boolean hasSequenceInfo() {
      return this.nameLength > 0 && this.chainLength > 0;
    }

    public boolean hasName() {
      return this.nameLength > 0;
    }

    public boolean hasChain() {
      return this.chainLength > 0;
    }

    public void setNameLocation(long nameLocation) {
      if (nameLocation < 0)
        throw new IllegalArgumentException("Name location should be greater than 0");

      this.nameLocation = nameLocation;
    }

    public void setNameLength(int nameLength) {
      if (this.nameLocation < 0)
        throw new IllegalStateException("Name location must be set before name length");
      if (nameLength < 0)
        throw new IllegalArgumentException("Name length should be greater than 0");

      this.nameLength = nameLength;
    }

    public void setDescriptionLocation(long descriptionLocation) {
      if (descriptionLocation < 0)
        throw new IllegalArgumentException("Description location should be greater than 0");

      this.descriptionLocation = descriptionLocation;
    }

    public void setDescriptionLength(int descriptionLength) {
      if (this.descriptionLocation < 0)
        throw new IllegalStateException("Description location must be set before description length");
      if (descriptionLength < 0)
        throw new IllegalArgumentException("Description length should be greater than 0");

      this.descriptionLength = descriptionLength;
    }

    public void setHeaderLocation(long headerLocation) {
      if (headerLocation < 0)
        throw new IllegalArgumentException("Header location should be greater than 0");

      this.headerLocation = headerLocation;
    }

    public void setHeaderLength(int headerLength) {
      if (this.headerLocation < 0)
        throw new IllegalStateException("Header location must be set before header length");
      if (headerLength < 0)
        throw new IllegalArgumentException("Header length should be greater than 0");

      this.headerLength = headerLength;
    }

    public long getChainLocation() {
      return this.chainLocation;
    }

    public void setChainLocation(long chainLocation) {
      if (chainLocation < 0)
        throw new IllegalArgumentException("Chain location should be greater than 0");

      this.chainLocation = chainLocation;
    }

    public void setChainLength(int chainLength) {
      if (this.chainLocation < 0)
        throw new IllegalStateException("Chain location must be set before chain length");
      if (chainLength < 0)
        throw new IllegalArgumentException("Chain length should be greater than 0");

      this.chainLength = chainLength;
    }

    public void updateChainColumns(int columns) {
      if (columns < 0)
        throw new IllegalArgumentException("Chain columns should be greater than 0");

      this.columnWidth = Math.max(this.columnWidth, columns);

      this.properties.put(Sequence.PROPERTY_CHAIN_COLUMNS, this.columnWidth);
    }

    public Sequence buildSequence() {
      return new LazyFileSequence(file, nameLocation, nameLength, descriptionLocation, descriptionLength, headerLocation, headerLength, chainLocation, chainLength, properties);
    }

    public void clear() {
      this.nameLocation = -1;
      this.nameLength = -1;
      this.descriptionLocation = -1;
      this.descriptionLength = -1;
      this.headerLocation = -1;
      this.headerLength = -1;
      this.chainLocation = -1;
      this.chainLength = -1;
      this.columnWidth = 0;
      this.properties = new HashMap<>();
    }
  }

  private static List<Sequence> readFileSequences(Path file) {
    try (NumberedLineReader reader = new NumberedLineReader(file)) {
      final List<Sequence> sequencesList = new LinkedList<>();

      final SequenceBuilder builder = new SequenceBuilder(file);

      NumberedLineReader.Line nline;
      while ((nline = reader.readLine()) != null) {
        final String line = nline.getLine().trim();

        final boolean isEmpty = line.isEmpty();
        final boolean isSequenceStart = line.startsWith(">");

        if (isEmpty || isSequenceStart) {
          if (builder.hasSequenceInfo()) {
            sequencesList.add(builder.buildSequence());

            builder.clear();
          } else if (builder.hasName() || builder.hasChain()) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", file, line));
          }
        }

        if (isSequenceStart) {
          final int spaceIndex = line.indexOf(" ");
          final String name, description;
          if (spaceIndex > 0) {
            name = line.substring(0, spaceIndex);
            description = line.substring(spaceIndex + 1);
          } else {
            name = line;
            description = "";
          }

          final int nameLength = name.getBytes().length;
          builder.setHeaderLocation(nline.getStart());
          builder.setHeaderLength(nline.getLength());

          builder.setNameLocation(nline.getStart() + 1);
          builder.setNameLength(nameLength);

          if (description.length() > 0) {
            builder.setDescriptionLocation(nline.getStart() + nameLength + 1);
            builder.setDescriptionLength((int) (nline.getEnd() - nline.getStart() - nameLength - 1));
          }

          builder.setChainLocation(reader.getCurrentPosition());
        } else if (!isEmpty) {
          if (!builder.hasName()) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", file, line));
          } else {
            final String nextChainLine = line.trim();

            builder.updateChainColumns(nextChainLine.length());
            builder.setChainLength((int) (reader.getCurrentPosition() - builder.getChainLocation() - nline.getLineEndingLength()));
          }
        }
      }

      if (builder.hasSequenceInfo()) {
        sequencesList.add(builder.buildSequence());

        builder.clear();
      } else if (builder.hasName() || builder.hasChain()) {
        throw new IOException("Incomplete file");
      }

      return sequencesList;
    } catch (IOException ioe) {
      throw new RuntimeException("Error reading file: " + file.toString(), ioe);
    }
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
