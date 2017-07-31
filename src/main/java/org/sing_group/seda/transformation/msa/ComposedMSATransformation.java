package org.sing_group.seda.transformation.msa;

import static org.sing_group.seda.bio.FunctionUtil.wrapWithExceptionToNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.MultipleSequenceAlignment;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;

public class ComposedMSATransformation implements MultipleSequenceAlignmentTransformation {
  private final BiFunction<String, Sequence[], MultipleSequenceAlignment> builder;
  private final SequenceTransformation[] transformations;
  
  public ComposedMSATransformation(SequenceTransformation ... transformations) {
    this.builder = MultipleSequenceAlignment::of;
    this.transformations = transformations;
  }
  
  public ComposedMSATransformation(DatatypeFactory factory, SequenceTransformation ... transformations) {
    this.builder = factory::newMSA;
    this.transformations = transformations;
  }
  
  public ComposedMSATransformation(Collection<SequenceTransformation> transformations) {
    this(transformations.stream().toArray(SequenceTransformation[]::new));
  }
  
  public ComposedMSATransformation(DatatypeFactory factory, Collection<SequenceTransformation> transformations) {
    this(factory, transformations.stream().toArray(SequenceTransformation[]::new));
  }

  @Override
  public MultipleSequenceAlignment transform(MultipleSequenceAlignment msa) throws TransformationException {
    Stream<Sequence> sequenceStream = msa.getSequences().parallel();
    
    for (SequenceTransformation transformation : this.transformations) {
      sequenceStream = sequenceStream
        .map(wrapWithExceptionToNull(transformation::transform, Throwable::printStackTrace))
        .filter(Objects::nonNull);
    }
    
    final Sequence[] sequences = sequenceStream.toArray(Sequence[]::new);
    
    if (sequences.length == 0)
      throw new TransformationException("Empty sequences after filtering");
    
    return this.builder.apply(msa.getName(), sequences);
  }

}
