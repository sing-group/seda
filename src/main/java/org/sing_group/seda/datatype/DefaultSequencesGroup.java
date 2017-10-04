package org.sing_group.seda.datatype;

import static java.util.Arrays.stream;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

public class DefaultSequencesGroup implements SequencesGroup, Serializable {
  private static final long serialVersionUID = 1L;
  
  private final String name;
  private final Sequence[] sequences;

  public DefaultSequencesGroup(String name, Sequence... sequences) {
    this.name = name;
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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    if (!Arrays.equals(sequences, other.sequences))
      return false;
    return true;
  }
}