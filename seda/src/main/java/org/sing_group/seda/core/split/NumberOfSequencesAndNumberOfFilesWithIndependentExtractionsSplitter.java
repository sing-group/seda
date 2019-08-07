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
package org.sing_group.seda.core.split;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter extends AbstractSequencesGroupSplitter {
  private int numFiles;
  private int numSequences;

  public NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter(int numFiles, int numSequences) {
    this(
      numFiles, numSequences, new DefaultSequencesSort(), DatatypeFactory.getDefaultDatatypeFactory()
    );
  }

  public NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter(
    int numFiles, int numSequences, SequencesSort sequencesSort, DatatypeFactory factory
  ) {
    super(sequencesSort, factory);
    this.numFiles = numFiles;
    this.numSequences = numSequences;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup group) {
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
      toret.add(createGroup(group.getName() + "_" + (file + 1), group.getProperties(), currentSubList));
    }

    return toret;
  }
}
