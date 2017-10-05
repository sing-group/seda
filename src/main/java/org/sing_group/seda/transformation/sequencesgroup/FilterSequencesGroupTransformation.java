package org.sing_group.seda.transformation.sequencesgroup;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.transformation.TransformationException;

public abstract class FilterSequencesGroupTransformation implements SequencesGroupTransformation {
  private final BiFunction<String, Sequence[], SequencesGroup> builder;
  private final BiPredicate<SequencesGroup, Sequence> filter;

  public FilterSequencesGroupTransformation(Predicate<Sequence> filter) {
    this((sequencesGroup, sequence) -> filter.test(sequence));
  }

  public FilterSequencesGroupTransformation(BiPredicate<SequencesGroup, Sequence> filter) {
    this(filter, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public FilterSequencesGroupTransformation(Predicate<Sequence> filter, DatatypeFactory factory) {
    this((sequencesGroup, sequence) -> filter.test(sequence), factory);
  }
  
  public FilterSequencesGroupTransformation(BiPredicate<SequencesGroup, Sequence> filter, DatatypeFactory factory) {
    this.builder = factory::newSequencesGroup;
    this.filter = filter;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) {
    try {
      final Sequence[] sequences = sequencesGroup.getSequences()
        .filter(sequence -> filter.test(sequencesGroup, sequence))
      .toArray(Sequence[]::new);

      if (sequences.length == 0)
        throw new TransformationException("Empty sequences after filtering");

      return this.builder.apply(sequencesGroup.getName(), sequences);
    } catch (RuntimeException e) {
      if (e instanceof TransformationException)
        throw e;
      else
        throw new TransformationException("Unexpected error during transformation", e);
    }
  }

}
