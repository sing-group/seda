package org.sing_group.seda.datatype;

import java.util.stream.Stream;

public interface SequencesGroupDataset {
  public static SequencesGroupDataset of(SequencesGroup... sequencesGroups) {
    return new DefaultDatatypeFactory().newSequencesGroupDataset(sequencesGroups);
  }

  public Stream<SequencesGroup> getSequencesGroups();

  public int getSequencesGroupsCount();
}