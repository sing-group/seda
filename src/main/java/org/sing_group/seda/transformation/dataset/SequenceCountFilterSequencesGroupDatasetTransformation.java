package org.sing_group.seda.transformation.dataset;

import java.util.function.Function;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;

public class SequenceCountFilterSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final int minSequences;
  
  public SequenceCountFilterSequencesGroupDatasetTransformation(int minSequences) {
    this.builder = SequencesGroupDataset::of;
    this.minSequences = minSequences;
  }
  
  public SequenceCountFilterSequencesGroupDatasetTransformation(int minAligments, DatatypeFactory factory) {
    this.builder = factory::newSequencesGroupDataset;
    this.minSequences = minAligments;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    final SequencesGroup[] sequencesGroups = dataset.getSequencesGroups()
      .filter(sequenceGroup -> sequenceGroup.getSequenceCount() >= this.minSequences)
    .toArray(SequencesGroup[]::new);

    if (sequencesGroups.length == 0)
      throw new TransformationException("No sequences after filtering");
    
    return this.builder.apply(sequencesGroups);
  }

}
