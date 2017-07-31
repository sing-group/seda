package org.sing_group.seda.datatype;

import java.util.stream.Stream;

public interface MultipleSequenceAlignmentDataset {
  public static MultipleSequenceAlignmentDataset of(MultipleSequenceAlignment ... alignments) {
    return new DefaultDatatypeFactory().newMSADataset(alignments);
  }
  
  public Stream<MultipleSequenceAlignment> getAlignments();
  
  public int getAlignmentCount();
}
