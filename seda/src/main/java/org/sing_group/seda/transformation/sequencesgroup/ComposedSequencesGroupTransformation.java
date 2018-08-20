/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.seda.transformation.sequencesgroup;

import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.bio.FunctionUtil.wrapWithExceptionToNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;

public class ComposedSequencesGroupTransformation implements SequencesGroupTransformation {
  private final SequencesGroupBuilder builder;
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

    final List<Sequence> sequences = sequenceStream.collect(toList());

    if (sequences.size() == 0) {
      throw new TransformationException("Empty sequences after filtering");
    }

    return this.builder.of(sequencesGroup.getName(), sequencesGroup.getProperties(), sequences);
  }

}
