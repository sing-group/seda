package org.sing_group.seda.datatype;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.sing_group.seda.io.LazyDatatypeFactory;

public class DefaultDatatypeFactory implements DatatypeFactory {

  @Override
  public Sequence newSequence(String name, String description, String sequence, Map<String, Object> properties) {
    return new DefaultSequence(name, description, sequence, properties);
  }

  @Override
  public SequencesGroup newSequencesGroup(Path path) {
    SequencesGroup sequence = new LazyDatatypeFactory().newSequencesGroup(path);

    return newSequencesGroup(sequence.getName(), sequence.getSequences().collect(toList()));
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, Sequence... sequences) {
    return new DefaultSequencesGroup(name, sequences);
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, Map<String, Object> properties, Sequence... sequences) {
    return new DefaultSequencesGroup(name, properties, sequences);
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, List<Sequence> sequences) {
    return newSequencesGroup(name, sequences.toArray(new Sequence[sequences.size()]));
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, Map<String, Object> properties, List<Sequence> sequences) {
    return newSequencesGroup(name, properties, sequences.toArray(new Sequence[sequences.size()]));
  }

  @Override
  public SequencesGroupDataset newSequencesGroupDataset(SequencesGroup... sequencesGroups) {
    return new DefaultSequencesGroupDataset(sequencesGroups);
  }
}
