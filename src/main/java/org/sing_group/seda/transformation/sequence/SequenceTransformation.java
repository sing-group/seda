package org.sing_group.seda.transformation.sequence;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.transformation.TransformationException;

public interface SequenceTransformation {
  
  public Sequence transform(Sequence sequence)
    throws TransformationException;
  
  public static SequenceTransformation concat(SequenceTransformation ... transformations) {
    return sequence -> {
      Sequence transformedSequence = sequence;
      
      for (SequenceTransformation transformation : transformations) {
        transformedSequence = transformation.transform(transformedSequence);
      }
      
      return transformedSequence;
    };
  }
}
