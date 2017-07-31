package org.sing_group.seda.datatype;

import static java.util.Arrays.stream;

import java.io.Serializable;
import java.util.stream.Stream;

public class DefaultMultipleSequenceAlignmentDataset implements MultipleSequenceAlignmentDataset, Serializable {
  private static final long serialVersionUID = 1L;
  
  private final MultipleSequenceAlignment[] alignments;

  public DefaultMultipleSequenceAlignmentDataset(MultipleSequenceAlignment ... alignments) {
    this.alignments = alignments;
  }

  @Override
  public Stream<MultipleSequenceAlignment> getAlignments() {
    return stream(this.alignments);
  }

  @Override
  public int getAlignmentCount() {
    return this.alignments.length;
  }

}
