package org.sing_group.seda.datatype;

import java.util.Map;

public class DefaultDatatypeFactory implements DatatypeFactory {

  @Override
  public Sequence newSequence(String name, String description, String sequence, Map<String, Object> properties) {
    return new DefaultSequence(name, description, sequence, properties);
  }

  @Override
  public MultipleSequenceAlignment newMSA(String name, Sequence... sequences) {
    return new DefaultMultipleSequenceAlignment(name, sequences);
  }

  @Override
  public MultipleSequenceAlignmentDataset newMSADataset(MultipleSequenceAlignment... alignments) {
    return new DefaultMultipleSequenceAlignmentDataset(alignments);
  }

}
