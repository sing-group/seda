package org.sing_group.seda.io;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;

public class LazyDatatypeFactory implements DatatypeFactory {
  private final DatatypeFactory defaultFactory = DatatypeFactory.getDefaultDatatypeFactory();

  @Override
  public Sequence newSequence(String name, String description, String sequence, Map<String, Object> properties) {
    return new LazyFileSequence(name, description, sequence, properties);
  }

  @Override
  public SequencesGroup newSequencesGroup(Path path) {
    return new LazyFileSequencesGroup(path);
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, Sequence... sequences) {
    return new LazyFileSequencesGroup(name, sequences);
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, List<Sequence> sequences) {
    return newSequencesGroup(name, sequences.toArray(new Sequence[sequences.size()]));
  }

  @Override
  public SequencesGroupDataset newSequencesGroupDataset(SequencesGroup... sequencesGroups) {
    return this.defaultFactory.newSequencesGroupDataset(sequencesGroups);
  }
}
