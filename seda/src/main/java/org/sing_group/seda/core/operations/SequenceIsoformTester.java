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
package org.sing_group.seda.core.operations;

import org.sing_group.seda.datatype.Sequence;

public class SequenceIsoformTester {
  private Sequence longest;
  private Sequence shortest;

  public SequenceIsoformTester(Sequence a, Sequence b) {
    if (a.getLength() > b.getLength()) {
      this.longest = a;
      this.shortest = b;
    } else {
      this.longest = b;
      this.shortest = a;
    }
  }

  public boolean test(int wordLength) {
    if (this.longest.getLength() < wordLength || this.shortest.getLength() < wordLength) {
      return false;
    }

    for (int i = 0; i <= shortest.getLength() - wordLength; i++) {
      String currentWord = shortest.getChain().substring(i, i + wordLength);
      if (this.longest.getChain().contains(currentWord)) {
        return true;
      }
    }

    return false;
  }
}
