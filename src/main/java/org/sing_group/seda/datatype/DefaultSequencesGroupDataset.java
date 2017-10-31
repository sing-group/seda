package org.sing_group.seda.datatype;

import static java.util.Arrays.stream;

import java.io.Serializable;
import java.util.stream.Stream;

public class DefaultSequencesGroupDataset implements SequencesGroupDataset, Serializable {
  private static final long serialVersionUID = 1L;

  private final SequencesGroup[] sequencesGroups;

  public DefaultSequencesGroupDataset(SequencesGroup... sequencesGroups) {
    this.sequencesGroups = sequencesGroups;
  }

  @Override
  public Stream<SequencesGroup> getSequencesGroups() {
    return stream(this.sequencesGroups);
  }

  @Override
  public int getSequencesGroupsCount() {
    return this.sequencesGroups.length;
  }
}
