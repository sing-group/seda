/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.transformation.TransformationException;

public class ReallocateReferenceSequencesTransformation implements SequencesGroupTransformation {
  private final SequencesGroupBuilder builder;
  private PatternFilteringSequencesGroupTransformation filter;

  public ReallocateReferenceSequencesTransformation(
    EvaluableSequencePattern pattern, SequenceTranslationConfiguration configuration
  ) {
    this(pattern, configuration, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public ReallocateReferenceSequencesTransformation(
    EvaluableSequencePattern pattern, SequenceTranslationConfiguration configuration, DatatypeFactory factory
  ) {
    this.builder = factory::newSequencesGroup;
    this.filter = new PatternFilteringSequencesGroupTransformation(pattern, configuration);
  }

  public ReallocateReferenceSequencesTransformation(EvaluableSequencePattern pattern, SequenceTarget sequenceTarget) {
    this(pattern, sequenceTarget, DatatypeFactory.getDefaultDatatypeFactory());
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

      return this.builder.of(sequencesGroup.getName(), sequencesGroup.getProperties(), newSequences);
    } catch (TransformationException ex) {
      return sequencesGroup;
    }
  }

}
