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
package org.sing_group.seda.datatype;

import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.isRegularFile;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;
import static org.sing_group.seda.io.FastaReader.readFasta;
import static org.sing_group.seda.io.FastaWriter.writeFasta;
import static org.sing_group.seda.io.IOUtils.extractIfNeeded;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.sing_group.seda.io.FastaReader.SequenceFromLocationsBuilder;
import org.sing_group.seda.io.FastaWriter;
import org.sing_group.seda.io.LineBreakType;

public class InDiskSequencesGroup implements SequencesGroup {
  private final static SequenceFromLocationsBuilder SEQUENCE_BUILDER = info -> new InDiskSequence(
    info.getFile(),
    // Charset subtype is used because when a sequence is read the first bytes
    // of the file are not read and, therefore, the charset subtype can't be
    // inferred by the reader. This is the case of, for example, UTF-16LE or
    // UTF-16BE.
    info.getCharsetSubtype(),
    info.getNameLocation(),
    info.getNameLength(),
    info.getDescriptionLocation(),
    info.getDescriptionLength(),
    info.getHeaderLocation(),
    info.getHeaderLength(),
    info.getChainLocation(),
    info.getChainLength(),
    info.getProperties()
  );
  
  private final String name;
  private final Path file;
  private final boolean isTempFile;
  private final InDiskSequence[] sequences;
  private Map<String, Object> properties;

  public InDiskSequencesGroup(Path file) {
    this(file, null);
  }
  
  public InDiskSequencesGroup(Path file, Charset charset) {
    this(file.getFileName().toString(), file, charset);
  }

  public InDiskSequencesGroup(String name, Path file) {
    this(name, file, null);
  }
  
  public InDiskSequencesGroup(String name, Path file, Charset charset) {
    if (!isRegularFile(file) && !isReadable(file)) {
      throw new IllegalArgumentException("file should be a regular and readable file");
    }
    
    this.name = requireNonNull(name, "name can't be null");
    
    try {
      this.file = extractIfNeeded(file);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error uncompressing file: " + file, e);
    }

    this.isTempFile = !this.file.equals(file);
    if (this.isTempFile)
      this.file.toFile().deleteOnExit();
    this.properties = new HashMap<>();
    this.sequences = readFasta(this.file, charset, SEQUENCE_BUILDER)
      .toArray(InDiskSequence[]::new);
    
    if (this.sequences.length > 0) {
      this.populateProperties(charset);
    }
  }

  public InDiskSequencesGroup(String name, Sequence... sequences) {
    this(name, emptyMap(), sequences);
  }

  public InDiskSequencesGroup(String name, Map<String, Object> properties, Sequence... sequences) {
    try {
      this.name = name;
      this.properties = properties;
      this.file = Files.createTempFile("seda_" + name, ".fasta");
      this.file.toFile().deleteOnExit();
      this.isTempFile = true;
      this.sequences = cloneSequences(sequences);
      this.populateProperties(null);
    } catch (IOException e) {
      throw new RuntimeException("Unexpected error creating temporary file.", e);
    }
  }

  private void populateProperties(Charset charset) {
    if (!this.properties.containsKey(PROPERTY_LINE_BREAK_OS)) {
      LineBreakType lineBreakType;
      try {
        lineBreakType = LineBreakType.forFile(this.file, charset);
      } catch (RuntimeException re) {
        // If this group was created only with LazySequences, then the group
        // file would be empty and no line break can be inferred from it. In
        // such case, the file of the first sequence is used.
        lineBreakType = LineBreakType.forFile(this.sequences[0].getFile(), charset);
      }
      
      if (!lineBreakType.equals(LineBreakType.defaultType())) {
        this.properties.put(PROPERTY_LINE_BREAK_OS, lineBreakType.getLineBreak());
      }
    }
  }
  
  private InDiskSequence[] cloneSequences(Sequence... sequences) {
    final Sequence[] clonedSequences = copyOf(sequences, sequences.length);
    
    final Stream<Sequence> nonLazySequences = stream(sequences)
      .filter(sequence -> !(sequence instanceof InDiskSequence));
    
    writeFasta(this.file, nonLazySequences);
    readFasta(this.file, FastaWriter.DEFAULT_CHARSET, SEQUENCE_BUILDER)
      .forEach(lazySequence -> {
        boolean found = false;
        for (int i = 0; i < clonedSequences.length; i++) {
          final Sequence sequence = clonedSequences[i];
          if (sequence.getHeader().equals(lazySequence.getHeader())
            && sequence.getChain().equals(lazySequence.getChain())) {
            clonedSequences[i] = lazySequence;
            found = true;
            break;
          }
        }

        if (!found) {
          throw new IllegalArgumentException("Error cloning sequence: " + lazySequence.getHeader());
        }
      });
    
    return stream(clonedSequences).toArray(InDiskSequence[]::new);
  }

  public Path getFile() {
    return file;
  }
  
  public boolean isTempFile() {
    return isTempFile;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Stream<Sequence> getSequences() {
    return stream(this.sequences);
  }

  @Override
  public int getSequenceCount() {
    return this.sequences.length;
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
}
