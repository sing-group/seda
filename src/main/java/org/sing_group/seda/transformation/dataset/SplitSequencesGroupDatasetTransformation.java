package org.sing_group.seda.transformation.dataset;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.split.SequencesGroupSplitter;
import org.sing_group.seda.transformation.TransformationException;

public class SplitSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final SequencesGroupSplitter splitter;

  public SplitSequencesGroupDatasetTransformation(SequencesGroupSplitter splitter) {
    this(splitter, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public SplitSequencesGroupDatasetTransformation(SequencesGroupSplitter splitter, DatatypeFactory factory) {
    this.builder = factory::newSequencesGroupDataset;
    this.splitter = splitter;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    final List<SequencesGroup> sequencesGroups = dataset.getSequencesGroups()
      .map(splitter::split)
      .flatMap(Collection::stream).collect(Collectors.toList());

    if (sequencesGroups.size() == 0)
      throw new TransformationException("No sequences after filtering");

    return this.builder.apply(sequencesGroups.toArray(new SequencesGroup[sequencesGroups.size()]));
  }
}
