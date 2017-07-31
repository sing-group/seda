package org.sing_group.seda.datatype;

public interface DatatypeFactory {
  public Sequence newSequence(String name, String sequence);
  
  public MultipleSequenceAlignment newMSA(String name, Sequence ... sequences);
  
  public MultipleSequenceAlignmentDataset newMSADataset(MultipleSequenceAlignment ... alignments);
}
