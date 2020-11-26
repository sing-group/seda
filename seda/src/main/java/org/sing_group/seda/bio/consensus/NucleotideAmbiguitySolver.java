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

public class NucleotideAmbiguitySolver {

  private String ambiguityChar;

  public NucleotideAmbiguitySolver(String ambiguityChar) {
    this.ambiguityChar = ambiguityChar;
  }

  protected String solve(List<Character> bases) {
    if (bases.size() == 2) {
      if (bases.contains('T') && bases.contains('C')) {
        return "Y";
      } else if (bases.contains('A') && bases.contains('G')) {
        return "R";
      } else if (bases.contains('A') && bases.contains('T')) {
        return "W";
      } else if (bases.contains('G') && bases.contains('C')) {
        return "S";
      } else if (bases.contains('T') && bases.contains('G')) {
        return "K";
      } else if (bases.contains('A') && bases.contains('C')) {
        return "M";
      } else {
        return this.ambiguityChar;
      }
    } else if (bases.size() == 3) {
      if (!bases.contains('C')) {
        return "D";
      } else if (!bases.contains('T')) {
        return "V";
      }
      if (!bases.contains('G')) {
        return "H";
      } else {
        return "B";
      }
    } else {
      return this.ambiguityChar;
    }
  }
}
