package org.sing_group.seda.transformation.sequencesgroup;

import org.sing_group.seda.datatype.DatatypeFactory;

public class FilterBySequenceLengthTransformation extends FilterSequencesGroupTransformation {

  public FilterBySequenceLengthTransformation(int minSequenceLength) {
    super(sequence -> sequence.getLength() >= minSequenceLength);
  }

  public FilterBySequenceLengthTransformation(int minSequenceLength, DatatypeFactory factory) {
    super(sequence -> sequence.getLength() >= minSequenceLength, factory);
  }
}
