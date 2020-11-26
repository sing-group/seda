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

import static java.util.stream.Collectors.joining;

import java.util.Collections;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;

public class ProteinSequencesGroupAboveThresholdConsensus extends AbstractSequencesGroupAboveThresholdConsensus {
  private static final String AMBIGUITY_CHARACTER = "X";

  public ProteinSequencesGroupAboveThresholdConsensus(
    SequenceBuilder sequenceBuilder, double minimumPresence, boolean verbose
  ) {
    super(sequenceBuilder, minimumPresence, verbose);
  }

  @Override
  protected String solveAmbiguity(List<Character> maxCharacter) {
    if (verbose) {
      return maxCharacter.stream().map(c -> c.toString()).collect(joining("", "[", "]"));
    } else {
      return AMBIGUITY_CHARACTER;
    }
  }

  public static void main(String[] args) {
    String c = new ProteinSequencesGroupAboveThresholdConsensus(DatatypeFactory.getDefaultDatatypeFactory()::newSequence, 0, true)
      .getConsensus(
        SequencesGroup.of(
          "name", Collections.emptyMap(),
          Sequence.of("1", "", "A"),
          Sequence.of("2", "", "A"),
          Sequence.of("3", "", "T"),
          Sequence.of("4", "", "C")
        )
      ).getChain();
    System.out.println(c);
  }

  @Override
  protected String ambiguityCharacter() {
    return AMBIGUITY_CHARACTER;
  }
}
