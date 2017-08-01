package org.sing_group.seda.datatype;

import java.util.Map;

public interface DatatypeFactory {
  public Sequence newSequence(String name, String description, String sequence, Map<String, Object> properties);

  public SequencesGroup newSequencesGroup(String name, Sequence... sequences);

  public SequencesGroupDataset newSequencesGroupDataset(SequencesGroup... sequencesGroups);
}
