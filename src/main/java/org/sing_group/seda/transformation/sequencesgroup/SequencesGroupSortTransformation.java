package org.sing_group.seda.transformation.sequencesgroup;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class SequencesGroupSortTransformation implements SequencesGroupTransformation {
  private final boolean descending;
  private final Comparator<? super Sequence> sequencesComparator;
  private final BiFunction<String, List<Sequence>, SequencesGroup> builder;

  public SequencesGroupSortTransformation(Comparator<? super Sequence> sequencesComparator) {
    this(sequencesComparator, false);
  }
  
  public SequencesGroupSortTransformation(
    Comparator<? super Sequence> sequencesComparator, boolean descending
  ) {
    this(sequencesComparator, descending, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public SequencesGroupSortTransformation(
    Comparator<? super Sequence> sequencesComparator, boolean descending, DatatypeFactory factory
  ) {
    this.builder = factory::newSequencesGroup;
    this.sequencesComparator = sequencesComparator;
    this.descending = descending;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    List<Sequence> newSequences =
      sequencesGroup.getSequences().sorted(descending ? sequencesComparator.reversed() : sequencesComparator)
        .collect(toList());
    
    return this.builder.apply(sequencesGroup.getName(), newSequences);
  }
}
