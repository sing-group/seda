package org.sing_group.seda.datatype;

import java.util.Map;

public class DefaultDatatypeFactory implements DatatypeFactory {

  @Override
  public Sequence newSequence(String name, String description, String sequence, Map<String, Object> properties) {
    return new DefaultSequence(name, description, sequence, properties);
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, Sequence... sequences) {
    return new DefaultSequencesGroup(name, sequences);
  }

  @Override
  public SequencesGroupDataset newSequencesGroupDataset(SequencesGroup... sequencesGroups) {
    return new DefaultSequencesGroupDataset(sequencesGroups);
  }

}
