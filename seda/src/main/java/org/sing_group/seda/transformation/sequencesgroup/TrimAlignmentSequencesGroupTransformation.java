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
import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;

import java.util.List;

import org.sing_group.seda.core.trim.SequenceAlignmentGapsBoundaries;
import org.sing_group.seda.core.trim.StringTrimmer;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.transformation.TransformationException;

public class TrimAlignmentSequencesGroupTransformation implements SequencesGroupTransformation {
  private final SequencesGroupBuilder builder;
  private final SequenceBuilder sequenceBuilder;

  public TrimAlignmentSequencesGroupTransformation() {
    this(getDefaultDatatypeFactory());
  }

  public TrimAlignmentSequencesGroupTransformation(DatatypeFactory factory) {
    this.builder = factory::newSequencesGroup;
    this.sequenceBuilder = factory::newSequence;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    List<SequenceAlignmentGapsBoundaries> boundaries =
      sequencesGroup.getSequences().map(SequenceAlignmentGapsBoundaries::new).collect(toList());

    int leading = boundaries.stream().mapToInt(SequenceAlignmentGapsBoundaries::leadingBases).max().getAsInt();
    int trailing = boundaries.stream().mapToInt(SequenceAlignmentGapsBoundaries::trailingBases).max().getAsInt();
    StringTrimmer trimmer = new StringTrimmer(leading, trailing);

    List<Sequence> trimmedSequences = sequencesGroup.getSequences().map(s -> {
      return this.sequenceBuilder.of(s.getName(), s.getDescription(), trimmer.trim(s.getChain()), s.getProperties());
    }).collect(toList());

    return builder.of(sequencesGroup.getName(), sequencesGroup.getProperties(), trimmedSequences);
  }
}
