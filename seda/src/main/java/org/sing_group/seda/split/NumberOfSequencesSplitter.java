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
package org.sing_group.seda.split;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class NumberOfSequencesSplitter extends AbstractSequencesGroupSplitter {
  private int numSequences;

  public NumberOfSequencesSplitter(int numSequences) {
    this(numSequences, false, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public NumberOfSequencesSplitter(int numSequences, boolean randomize, DatatypeFactory factory) {
    super(randomize, factory);
    this.numSequences = numSequences;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup group) {
    List<Sequence> input = getInputSequencesGroup(group);

    Spliterator<Sequence> splitIterator = input.spliterator();

    int splitCount = 1;
    List<SequencesGroup> toret = new LinkedList<>();

    while (true) {
      List<Sequence> chunk = new ArrayList<>(this.numSequences);

      for (int i = 0; i < this.numSequences && splitIterator.tryAdvance(chunk::add); i++) {
      }

      if (chunk.isEmpty()) {
        break;
      }

      toret.add(createGroup(group.getName() + "_" + (splitCount++), group.getProperties(), chunk));
    }
    return toret;
  }
}
