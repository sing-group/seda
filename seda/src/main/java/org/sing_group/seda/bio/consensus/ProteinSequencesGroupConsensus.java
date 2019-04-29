/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.SequenceBuilder;

public class ProteinSequencesGroupConsensus extends AbstractSequencesGroupConsensus {
  private static final String AMBIGUITY_CHARACTER = "X";

  public ProteinSequencesGroupConsensus(SequenceBuilder sequenceBuilder, double minimumPresence, boolean verbose) {
    super(sequenceBuilder, minimumPresence, verbose);
  }

  @Override
  protected String solveAmbiguity(List<Character> maxCharacter, double maxPresence) {
    if (maxPresence < minimumPresence) {
      return AMBIGUITY_CHARACTER;
    } else {
      if (verbose) {
        return maxCharacter.stream().map(c -> c.toString()).collect(Collectors.joining("", "[", "]"));
      } else {
        return AMBIGUITY_CHARACTER;
      }
    }
  }
}
