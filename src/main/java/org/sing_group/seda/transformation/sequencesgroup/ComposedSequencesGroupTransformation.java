package org.sing_group.seda.transformation.sequencesgroup;

import static org.sing_group.seda.bio.FunctionUtil.wrapWithExceptionToNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;

public class ComposedSequencesGroupTransformation implements SequencesGroupTransformation {
  private final BiFunction<String, Sequence[], SequencesGroup> builder;
  private final SequenceTransformation[] transformations;
  
  public ComposedSequencesGroupTransformation(SequenceTransformation... transformations) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), transformations);
  }

  public ComposedSequencesGroupTransformation(DatatypeFactory factory, SequenceTransformation ... transformations) {
    this.builder = factory::newSequencesGroup;
    this.transformations = transformations;
  }
  
  public ComposedSequencesGroupTransformation(Collection<SequenceTransformation> transformations) {
    this(transformations.stream().toArray(SequenceTransformation[]::new));
  }
  
  public ComposedSequencesGroupTransformation(
    DatatypeFactory factory, Collection<SequenceTransformation> transformations
  ) {
    this(factory, transformations.stream().toArray(SequenceTransformation[]::new));
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    Stream<Sequence> sequenceStream = sequencesGroup.getSequences().parallel();
    
    for (SequenceTransformation transformation : this.transformations) {
      sequenceStream = sequenceStream
        .map(
          wrapWithExceptionToNull(
            s -> transformation.transform(s), (s, e) -> {
              System.err.println("An exception occurred processing " + ((Sequence) s).getName());
              System.err.println("Exception message: " + e.getMessage());
              if (e.getCause() != null) {
                System.err.println("Exception message: " + e.getCause().getMessage());
              }
              System.err.println();
            }
          )
        )
        .filter(Objects::nonNull);
    }

    final Sequence[] sequences = sequenceStream.toArray(Sequence[]::new);
    
    if (sequences.length == 0)
      throw new TransformationException("Empty sequences after filtering");
    
    return this.builder.apply(sequencesGroup.getName(), sequences);
  }

}
