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

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import org.sing_group.seda.bio.SequenceType;
import org.sing_group.seda.bio.consensus.SequencesGroupConsensusFactory;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class GenerateConsensusSequencesGroupTransformation implements SequencesGroupTransformation {
  private final BiFunction<String, List<Sequence>, SequencesGroup> builder;
  private final SequenceBuilder sequenceBuilder;
  private SequenceType sequenceType;
  private double minimumPresence;
  private boolean verbose;

  public GenerateConsensusSequencesGroupTransformation(SequenceType sequenceType, double minimumPresence, boolean verbose) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), sequenceType, minimumPresence, verbose);
  }

  public GenerateConsensusSequencesGroupTransformation(
    DatatypeFactory factory, SequenceType sequenceType, double minimumPresence, boolean verbose
  ) {
    this.builder = factory::newSequencesGroup;
    this.sequenceBuilder = factory::newSequence;
    this.sequenceType = sequenceType;
    this.minimumPresence = minimumPresence;
    this.verbose = verbose;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    Sequence consensus =
      SequencesGroupConsensusFactory
        .getConsensusCreator(sequenceType, sequenceBuilder, minimumPresence, verbose)
        .getConsensus(sequencesGroup);

    return this.builder.apply("consensus_" + sequencesGroup.getName(), Arrays.asList(consensus));
  }
}
