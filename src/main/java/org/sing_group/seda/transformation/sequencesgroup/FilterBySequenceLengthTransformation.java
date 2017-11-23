package org.sing_group.seda.transformation.sequencesgroup;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;

public class FilterBySequenceLengthTransformation extends FilterSequencesGroupTransformation {

  public FilterBySequenceLengthTransformation(int minSequenceLength, int maxSequenceLength) {
    super(sequence -> filterBySequenceLenth(sequence, minSequenceLength, maxSequenceLength));
  }

  public FilterBySequenceLengthTransformation(int minSequenceLength, int maxSequenceLength, DatatypeFactory factory) {
    super(sequence -> filterBySequenceLenth(sequence, minSequenceLength, maxSequenceLength), factory);
  }

  private static boolean filterBySequenceLenth(Sequence s, int minSequenceLength, int maxSequenceLength) {
    if (minSequenceLength > 0 && s.getLength() < minSequenceLength) {
      return false;
    }

    if (maxSequenceLength > 0 && s.getLength() > maxSequenceLength) {
      return false;
    }

    return true;
  }
}
