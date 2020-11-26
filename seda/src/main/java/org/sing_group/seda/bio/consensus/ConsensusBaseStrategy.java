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

public enum ConsensusBaseStrategy {
  MOST_FREQUENT(
    "Most frequent",
    "Considers the most frequent nucleotide (DNA) or amino acid (protein) bases at each "
      + "position.<br/>Those positions where the most frequent base is under the Minimum presence "
      + "threshold are represented <br/>by an N (nucleotide sequences) or X (protein sequences) in "
      + "the consensus sequence."
  ),
  ABOVE_THRESHOLD(
    "Above threshold",
    "Considers all nucleotide (DNA) or amino acid (protein) bases with a frequence above the "
      + "<br/>Minimum presence threshold at each position. Those positions where all base frequencies "
      + "are below the <br/>Minimum presence threshold are represented by an N (nucleotide sequences) "
      + "<br/>or X (protein sequences) in the consensus sequence."
  );

  private String name;
  private String description;

  ConsensusBaseStrategy(String name, String description) {
    this.name = name;
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name;
  }

  public String getDescription() {
    return description;
  }
}
