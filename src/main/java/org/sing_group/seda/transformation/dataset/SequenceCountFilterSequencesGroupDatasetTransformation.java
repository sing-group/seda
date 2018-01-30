package org.sing_group.seda.transformation.dataset;

import java.util.function.Function;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;

public class SequenceCountFilterSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final int minSequences;
  private final int maxSequences;


  public SequenceCountFilterSequencesGroupDatasetTransformation(int minSequences, int maxSequences) {
    this(minSequences, maxSequences, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public SequenceCountFilterSequencesGroupDatasetTransformation(
    int minSequences, int maxSequences, DatatypeFactory factory
  ) {
    this.builder = factory::newSequencesGroupDataset;
    this.minSequences = minSequences;
    this.maxSequences = maxSequences;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    final SequencesGroup[] sequencesGroups = dataset.getSequencesGroups()
      .filter(
        sequenceGroup -> {

          if (minSequences > 0 && sequenceGroup.getSequenceCount() < minSequences) {
            return false;
          }

          if (maxSequences > 0 && sequenceGroup.getSequenceCount() > maxSequences) {
            return false;
          }

          return true;
        }

      )
      .toArray(SequencesGroup[]::new);

    if (sequencesGroups.length == 0)
      throw new TransformationException("No sequences after filtering");

    return this.builder.apply(sequencesGroups);
  }
}
