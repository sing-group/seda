package org.sing_group.seda.transformation.sequencesgroup;

import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public interface SequencesGroupTransformation {
  
  public SequencesGroup transform(SequencesGroup sequencesGroup)
    throws TransformationException;

  public static SequencesGroupTransformation concat(SequencesGroupTransformation... transformations) {
    return sequencesGroup -> {
      SequencesGroup transformedSequencesGroup = sequencesGroup;

      for (SequencesGroupTransformation transformation : transformations) {
        transformedSequencesGroup = transformation.transform(transformedSequencesGroup);
      }

      return transformedSequencesGroup;
    };
  }
}
