package org.sing_group.seda.datatype;

import java.util.Map;

public interface DatatypeFactory {
  public Sequence newSequence(String name, String description, String sequence, Map<String, Object> properties);

  public MultipleSequenceAlignment newMSA(String name, Sequence... sequences);

  public MultipleSequenceAlignmentDataset newMSADataset(MultipleSequenceAlignment... alignments);
}
