package org.sing_group.seda.datatype;

import java.util.stream.Stream;

public interface MultipleSequenceAlignment {
  public static MultipleSequenceAlignment of(String name, Sequence ... sequences) {
    return new DefaultDatatypeFactory().newMSA(name, sequences);
  }
  
  public String getName();
  
  public Stream<Sequence> getSequences();
  
  public int getSequenceCount();
  
  public default Sequence getSequence(int index) {
    return this.getSequences()
      .skip(index)
    .findFirst()
    .orElseThrow(() -> new IndexOutOfBoundsException("Invalid sequence index"));
  }
  
  public default Sequence getSequence(String name) {
    return this.getSequences()
      .filter(sequence -> sequence.getName().equals(name))
    .findFirst()
    .orElseThrow(() -> new IndexOutOfBoundsException("No sequence found with name: " + name));
  }
}
