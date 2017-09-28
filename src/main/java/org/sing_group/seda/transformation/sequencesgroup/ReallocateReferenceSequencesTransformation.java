package org.sing_group.seda.transformation.sequencesgroup;

import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequencesgroup.PatternFilteringSequencesGroupTransformation.SequenceTranslationConfiguration;

public class ReallocateReferenceSequencesTransformation implements SequencesGroupTransformation {
  private final BiFunction<String, Sequence[], SequencesGroup> builder;
  private PatternFilteringSequencesGroupTransformation filter;

  public ReallocateReferenceSequencesTransformation(
    EvaluableSequencePattern pattern, SequenceTranslationConfiguration configuration
  ) {
    this.builder = SequencesGroup::of;
    this.filter = new PatternFilteringSequencesGroupTransformation(pattern, configuration);
  }

  public ReallocateReferenceSequencesTransformation(
    EvaluableSequencePattern pattern, SequenceTranslationConfiguration configuration, DatatypeFactory factory
  ) {
    this.builder = factory::newSequencesGroup;
    this.filter = new PatternFilteringSequencesGroupTransformation(pattern, configuration);
  }

  public ReallocateReferenceSequencesTransformation(EvaluableSequencePattern pattern, SequenceTarget sequenceTarget) {
    this.builder = SequencesGroup::of;
    this.filter = new PatternFilteringSequencesGroupTransformation(pattern, sequenceTarget);
  }

  public ReallocateReferenceSequencesTransformation(EvaluableSequencePattern pattern, SequenceTarget sequenceTarget,
    DatatypeFactory factory
   ){
    this.builder = factory::newSequencesGroup;
    this.filter = new PatternFilteringSequencesGroupTransformation(pattern, sequenceTarget);
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    try {
      List<Sequence> referenceSequences =
        this.filter.transform(sequencesGroup).getSequences().collect(Collectors.toList());

      List<Sequence> newSequences = new LinkedList<>(referenceSequences);

      newSequences.addAll(
        sequencesGroup.getSequences().filter(s -> !referenceSequences.contains(s))
          .collect(toList())
      );

      return this.builder.apply(
        sequencesGroup.getName(), newSequences.toArray(new Sequence[sequencesGroup.getSequenceCount()])
      );
    } catch (TransformationException ex) {
      return sequencesGroup;
    }
  }

}
