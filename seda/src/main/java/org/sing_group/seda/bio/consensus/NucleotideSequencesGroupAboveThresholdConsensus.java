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
package org.sing_group.seda.bio.consensus;

import java.util.List;

import org.sing_group.seda.datatype.SequenceBuilder;

public class NucleotideSequencesGroupAboveThresholdConsensus extends AbstractSequencesGroupAboveThresholdConsensus {
  private static final String AMBIGUITY_CHARACTER = "N";

  public NucleotideSequencesGroupAboveThresholdConsensus(
    SequenceBuilder sequenceBuilder, double minimumPresence, boolean verbose
  ) {
    super(sequenceBuilder, minimumPresence, verbose);
  }

  @Override
  protected String solveAmbiguity(List<Character> maxCharacter) {
    return new NucleotideAmbiguitySolver(AMBIGUITY_CHARACTER).solve(maxCharacter);
  }

  @Override
  protected String ambiguityCharacter() {
    return AMBIGUITY_CHARACTER;
  }
}