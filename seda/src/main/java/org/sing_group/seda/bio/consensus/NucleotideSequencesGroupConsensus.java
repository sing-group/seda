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

public class NucleotideSequencesGroupConsensus extends AbstractSequencesGroupConsensus {
  private static final String AMBIGUITY_CHARACTER = "N";
  
  public NucleotideSequencesGroupConsensus(SequenceBuilder sequenceBuilder, double minimumPresence, boolean verbose) {
    super(sequenceBuilder, minimumPresence, verbose);
  }

  @Override
  protected String solveAmbiguity(List<Character> maxCharacter, double maxPresence) {
    if (maxPresence < minimumPresence) {
      return AMBIGUITY_CHARACTER;
    } else {
      if (maxCharacter.size() == 2) {
        if (maxCharacter.contains('T') && maxCharacter.contains('C')) {
          return "Y";
        } else if (maxCharacter.contains('A') && maxCharacter.contains('G')) {
          return "R";
        } else if (maxCharacter.contains('A') && maxCharacter.contains('T')) {
          return "W";
        } else if (maxCharacter.contains('G') && maxCharacter.contains('C')) {
          return "S";
        } else if (maxCharacter.contains('T') && maxCharacter.contains('G')) {
          return "K";
        } else if (maxCharacter.contains('A') && maxCharacter.contains('C')) {
          return "M";
        } else {
          return AMBIGUITY_CHARACTER;
        }
      } else if (maxCharacter.size() == 3) {
        if (!maxCharacter.contains('C')) {
          return "D";
        } else if (!maxCharacter.contains('T')) {
          return "V";
        }
        if (!maxCharacter.contains('G')) {
          return "H";
        } else {
          return "B";
        }
      } else {
        return AMBIGUITY_CHARACTER;
      }
    }
  }
}