package org.sing_group.seda.io;

import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.DefaultDatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.datatype.Sequence;

public class LazyDatatypeFactory implements DatatypeFactory {
  private final DatatypeFactory defaultFactory = new DefaultDatatypeFactory();

  @Override
  public Sequence newSequence(String name, String description, String sequence, Map<String, Object> properties) {
    return this.defaultFactory.newSequence(name, description, sequence, properties);
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, Sequence... sequences) {
    return new LazyFileSequencesGroup(name, sequences);
  }

  @Override
  public SequencesGroupDataset newSequencesGroupDataset(SequencesGroup... sequencesGroups) {
    return this.defaultFactory.newSequencesGroupDataset(sequencesGroups);
  }

}
