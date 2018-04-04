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
package org.sing_group.seda.split;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class NumberOfSequencesAndNumberOfFilesSplitter extends AbstractSequencesGroupSplitter {
  private int numFiles;
  private int numSequences;
  private boolean independentExtractions;

  public NumberOfSequencesAndNumberOfFilesSplitter(int numFiles, int numSequences, boolean independentExtractions) {
    this(numFiles, numSequences, false, independentExtractions, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public NumberOfSequencesAndNumberOfFilesSplitter(
    int numFiles, int numSequences, boolean randomize, boolean independentExtractions, DatatypeFactory factory
  ) {
    super(randomize, factory);
    this.numFiles = numFiles;
    this.numSequences = numSequences;
    this.independentExtractions = independentExtractions;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup group) {
    if (this.independentExtractions) {
      return independentExtractions(group);
    } else {
      return splitGroup(group);
    }
  }

  private List<SequencesGroup> independentExtractions(SequencesGroup group) {
    List<SequencesGroup> toret = new LinkedList<>();
    for (int file = 0; file < this.numFiles; file++) {
      List<Sequence> input = getInputSequencesGroup(group);
      Iterator<Sequence> inputIterator = input.iterator();
      List<Sequence> currentSubList = new LinkedList<>();
      for (int i = 0; i < this.numSequences; i++) {
        if (!inputIterator.hasNext()) {
          inputIterator = input.iterator();
        }
        currentSubList.add(inputIterator.next());
      }
      toret.add(createGroup(group.getName() + "_" + (file + 1), currentSubList));
    }

    return toret;
  }

  private List<SequencesGroup> splitGroup(SequencesGroup group) {
    if (this.numFiles * this.numSequences > group.getSequenceCount()) {
      throw new TransformationException("Not enough sequences");
    }

    List<SequencesGroup> toret = new LinkedList<>();
    List<Sequence> input = getInputSequencesGroup(group);
    for (int file = 0; file < this.numFiles; file++) {
      int startIndex = file * this.numSequences;
      int endIndex = (file + 1) * this.numSequences;
      List<Sequence> currentSubList = input.subList(startIndex, endIndex);

      toret.add(createGroup(group.getName() + "_" + (file + 1), currentSubList));
    }
    return toret;
  }
}
