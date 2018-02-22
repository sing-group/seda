package org.sing_group.seda.transformation.dataset;

import java.util.Map;
import java.util.function.Function;

import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.HeaderMatcherCount;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;

public class HeaderCountFilteringSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private int min;
  private HeaderMatcher matcher;
  private int max;
  private boolean include;


  public HeaderCountFilteringSequencesGroupDatasetTransformation(HeaderMatcher matcher, int min, int max, boolean include) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), matcher, min, max, include);
  }

  public HeaderCountFilteringSequencesGroupDatasetTransformation(
    DatatypeFactory factory, HeaderMatcher matcher, int min, int max, boolean include
  ) {
    this.builder = factory::newSequencesGroupDataset;
    this.matcher = matcher;
    this.min = min;
    this.max = max;
    this.include = include;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    final SequencesGroup[] sequencesGroups = dataset.getSequencesGroups()
      .filter(
        sequenceGroup -> {

          Map<String, Integer> counts = new HeaderMatcherCount(this.matcher).count(sequenceGroup);
          boolean invalid;

          if (counts.isEmpty()) {
            invalid = true;
          } else {
            invalid = counts.values().stream().filter(
              count -> {
                return count < min || count > max;
              }
            ).findAny().isPresent();
          }

          if (include) {
            return !invalid;
          } else {
            return invalid;
          }
        }
      )
      .toArray(SequencesGroup[]::new);

    if (sequencesGroups.length == 0)
      throw new TransformationException("No sequences after filtering");

    return this.builder.apply(sequencesGroups);
  }
}
