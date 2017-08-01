package org.sing_group.seda.transformation.dataset;

import static org.sing_group.seda.bio.FunctionUtil.wrapWithExceptionToNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class ComposedSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final SequencesGroupTransformation[] transformations;
  
  public ComposedSequencesGroupDatasetTransformation(SequencesGroupTransformation ... transformations) {
    this.builder = SequencesGroupDataset::of;
    this.transformations = transformations;
  }
  
  public ComposedSequencesGroupDatasetTransformation(DatatypeFactory factory, SequencesGroupTransformation ... transformations) {
    this.builder = factory::newSequencesGroupDataset;
    this.transformations = transformations;
  }
  
  public ComposedSequencesGroupDatasetTransformation(Collection<SequencesGroupTransformation> transformations) {
    this(transformations.stream().toArray(SequencesGroupTransformation[]::new));
  }
  
  public ComposedSequencesGroupDatasetTransformation(DatatypeFactory factory, Collection<SequencesGroupTransformation> transformations) {
    this(factory, transformations.stream().toArray(SequencesGroupTransformation[]::new));
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) throws TransformationException {
    Stream<SequencesGroup> sequencesGroupsStream = dataset.getSequencesGroups().parallel();
    
    for (SequencesGroupTransformation transformation : this.transformations) {
      sequencesGroupsStream = sequencesGroupsStream
        .map(wrapWithExceptionToNull(transformation::transform, Throwable::printStackTrace))
        .filter(Objects::nonNull);
    }
    
    final SequencesGroup[] sequencesGroups = sequencesGroupsStream.toArray(SequencesGroup[]::new);

    if (sequencesGroups.length == 0)
      throw new TransformationException("No sequences after filtering");
    
    return this.builder.apply(sequencesGroups);
  }
}
