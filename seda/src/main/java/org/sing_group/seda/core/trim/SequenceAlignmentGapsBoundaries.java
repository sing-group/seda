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
package org.sing_group.seda.core.trim;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sing_group.seda.datatype.Sequence;

public class SequenceAlignmentGapsBoundaries {

  private Sequence sequence;
  private int leadingBases = -1;
  private int trailingBases = -1;

  public SequenceAlignmentGapsBoundaries(Sequence sequence) {
    this.sequence = sequence;
  }

  public int leadingBases() {
    if (this.leadingBases == -1) {
      Pattern pattern = Pattern.compile("(^-*)");
      Matcher matcher = pattern.matcher(sequence.getChain());

      if (matcher.find()) {
        leadingBases = matcher.group(1).length();
      } else {
        this.leadingBases = 0;
      }
    }
    return this.leadingBases;
  }

  public int trailingBases() {
    if (this.trailingBases == -1) {
      Pattern pattern = Pattern.compile("(-*)$");
      Matcher matcher = pattern.matcher(sequence.getChain());

      if (matcher.find()) {
        trailingBases = matcher.group(1).length();
      } else {
        this.trailingBases = 0;
      }
    }
    return this.trailingBases;
  }
}
