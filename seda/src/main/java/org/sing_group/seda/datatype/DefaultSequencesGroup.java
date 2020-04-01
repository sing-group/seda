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
import static java.util.Arrays.stream;
import static java.util.Collections.emptyMap;
import static org.sing_group.seda.io.FastaReader.readFasta;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.sing_group.seda.io.FastaReader.SequenceFromTextBuilder;
import org.sing_group.seda.io.LineBreakType;

public class DefaultSequencesGroup implements SequencesGroup, Serializable {
  private final static SequenceFromTextBuilder SEQUENCE_BUILDER = info -> new DefaultSequence(
    info.getName(),
    info.getDescription(),
    info.getChain(),
    info.getProperties()
  );
  
  private static final long serialVersionUID = 1L;
  
  private final String name;
  private final Sequence[] sequences;
  private Map<String, Object> properties;
  
  public DefaultSequencesGroup(Path file) {
    this(file.getFileName().toString(), file);
  }
  
  public DefaultSequencesGroup(String name, Path file) {
    if (!isRegularFile(file) && !isReadable(file)) {
      throw new IllegalArgumentException("file should be a regular and readable file");
    }
    
    this.name = name;
    this.sequences = readFasta(file, SEQUENCE_BUILDER).toArray(Sequence[]::new);
    this.properties = new HashMap<>();
    
    if (this.sequences.length > 0) {
      final LineBreakType lineBreakType = LineBreakType.forFile(file);
      if (!this.properties.containsKey(PROPERTY_LINE_BREAK_OS) && !lineBreakType.equals(LineBreakType.defaultType())) {
        this.properties.put(PROPERTY_LINE_BREAK_OS, lineBreakType.getLineBreak());
      }
    }
  }

  public DefaultSequencesGroup(String name, Sequence... sequences) {
    this(name, emptyMap(), sequences);
  }

  public DefaultSequencesGroup(String name, Map<String, Object> properties, Sequence... sequences) {
    this.name = name;
    this.properties = properties;
    this.sequences = sequences;
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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((properties == null) ? 0 : properties.hashCode());
    result = prime * result + Arrays.hashCode(sequences);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DefaultSequencesGroup other = (DefaultSequencesGroup) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (properties == null) {
      if (other.properties != null)
        return false;
    } else if (!properties.equals(other.properties))
      return false;
    if (!Arrays.equals(sequences, other.sequences))
      return false;
    return true;
  }
}
