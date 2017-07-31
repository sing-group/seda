package org.sing_group.seda.transformation.msa;

import java.util.function.BiPredicate;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.MultipleSequenceAlignment;
import org.sing_group.seda.datatype.Sequence;

public class RemoveBySizeMSATransformation extends FilterMSATransformation {
  public RemoveBySizeMSATransformation(int referenceSequenceIndex, double maxSizeDifference) {
    super(buildPredicate(referenceSequenceIndex, maxSizeDifference));
  }
  
  public RemoveBySizeMSATransformation(int referenceSequenceIndex, double maxSizeDifference, DatatypeFactory factory) {
    super(buildPredicate(referenceSequenceIndex, maxSizeDifference), factory);
  }
  
  private final static BiPredicate<MultipleSequenceAlignment, Sequence> buildPredicate(
    int referenceSequenceIndex, double maxSizeDifference
  ) {
    return (msa, sequence) -> {
      final Sequence referenceSequence = msa.getSequence(referenceSequenceIndex);
      final int referenceLength = referenceSequence.getLength();
      final int minLength = (int) (referenceLength * (1d - maxSizeDifference));
      final int maxLength = (int) (referenceLength * (1d + maxSizeDifference));
      
      final int sequenceLength = sequence.getLength();
      
      return sequenceLength >= minLength && sequenceLength <= maxLength;
    };
  }
}
