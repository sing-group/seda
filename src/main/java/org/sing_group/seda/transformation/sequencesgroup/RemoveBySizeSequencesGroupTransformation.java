package org.sing_group.seda.transformation.sequencesgroup;

import java.util.function.BiPredicate;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class RemoveBySizeSequencesGroupTransformation extends FilterSequencesGroupTransformation {
  public RemoveBySizeSequencesGroupTransformation(Sequence referenceSequence, double maxSizeDifference) {
    super(buildPredicate(referenceSequence, maxSizeDifference));
  }

  public RemoveBySizeSequencesGroupTransformation(Sequence referenceSequence, double maxSizeDifference, DatatypeFactory factory) {
    super(buildPredicate(referenceSequence, maxSizeDifference), factory);
  }

  private final static BiPredicate<SequencesGroup, Sequence> buildPredicate(
    Sequence referenceSequence, double maxSizeDifference
  ) {
    return (sequencesGroup, sequence) -> {
      return filter(sequence, referenceSequence, maxSizeDifference);
    };
  }

  public RemoveBySizeSequencesGroupTransformation(int referenceSequenceIndex, double maxSizeDifference) {
    super(buildPredicate(referenceSequenceIndex, maxSizeDifference));
  }

  public RemoveBySizeSequencesGroupTransformation(int referenceSequenceIndex, double maxSizeDifference, DatatypeFactory factory) {
    super(buildPredicate(referenceSequenceIndex, maxSizeDifference), factory);
  }


  private final static BiPredicate<SequencesGroup, Sequence> buildPredicate(
    int referenceSequenceIndex, double maxSizeDifference
  ) {
    return (sequencesGroup, sequence) -> {
      final Sequence referenceSequence = sequencesGroup.getSequence(referenceSequenceIndex);

      return filter(sequence, referenceSequence, maxSizeDifference);
    };
  }

  private final static boolean filter(Sequence reference, Sequence sequence, double maxSizeDifference) {
    final int referenceLength = reference.getLength();
    final int minLength = (int) (referenceLength * (1d - maxSizeDifference));
    final int maxLength = (int) (referenceLength * (1d + maxSizeDifference));

    final int sequenceLength = sequence.getLength();

    return sequenceLength >= minLength && sequenceLength <= maxLength;
  }
}
