package org.sing_group.seda.datatype;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface DatatypeFactory {

  public static DatatypeFactory getDefaultDatatypeFactory() {
    return new DefaultDatatypeFactory();
  }

  public Sequence newSequence(String name, String description, String sequence, Map<String, Object> properties);

  public SequencesGroup newSequencesGroup(Path path);

  public SequencesGroup newSequencesGroup(String name, Sequence... sequences);

  public SequencesGroup newSequencesGroup(String name, List<Sequence> sequences);

  public SequencesGroupDataset newSequencesGroupDataset(SequencesGroup... sequencesGroups);
}
