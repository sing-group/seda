package org.sing_group.seda.transformation.msa;

import org.sing_group.seda.datatype.MultipleSequenceAlignment;
import org.sing_group.seda.transformation.TransformationException;

public interface MultipleSequenceAlignmentTransformation {
  
  public MultipleSequenceAlignment transform(MultipleSequenceAlignment alignment)
    throws TransformationException;
  
  public static MultipleSequenceAlignmentTransformation concat(MultipleSequenceAlignmentTransformation ... transformations) {
    return alignment -> {
      MultipleSequenceAlignment transformedAlignment = alignment;
      
      for (MultipleSequenceAlignmentTransformation transformation : transformations) {
        transformedAlignment = transformation.transform(transformedAlignment);
      }
      
      return transformedAlignment;
    };
  }
}
