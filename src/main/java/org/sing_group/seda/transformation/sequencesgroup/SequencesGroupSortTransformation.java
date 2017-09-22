package org.sing_group.seda.transformation.sequencesgroup;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class SequencesGroupSortTransformation implements SequencesGroupTransformation {
  private final BiFunction<String, Sequence[], SequencesGroup> builder;
  private final Comparator<? super Sequence> sequencesComparator;
  private final boolean descending;

  public SequencesGroupSortTransformation(Comparator<? super Sequence> sequencesComparator) {
    this(sequencesComparator, false);
  }
  
  public SequencesGroupSortTransformation(Comparator<? super Sequence> sequencesComparator, boolean descending) {
    this.builder = SequencesGroup::of;
    this.sequencesComparator = sequencesComparator;
    this.descending = descending;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    List<Sequence> newSequences =
      sequencesGroup.getSequences().sorted(descending ? sequencesComparator.reversed() : sequencesComparator)
        .collect(toList());
    
    return this.builder.apply(sequencesGroup.getName(), newSequences.toArray(new Sequence[sequencesGroup.getSequenceCount()]));
  }
}
