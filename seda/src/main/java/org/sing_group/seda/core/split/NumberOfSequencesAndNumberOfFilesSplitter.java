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
package org.sing_group.seda.core.split;

import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class NumberOfSequencesAndNumberOfFilesSplitter extends AbstractSequencesGroupSplitter {
  private int numFiles;
  private int numSequences;

  public NumberOfSequencesAndNumberOfFilesSplitter(int numFiles, int numSequences) {
    this(
      numFiles, numSequences, new DefaultSequencesSort(), DatatypeFactory.getDefaultDatatypeFactory()
    );
  }

  public NumberOfSequencesAndNumberOfFilesSplitter(
    int numFiles, int numSequences, SequencesSort sequencesSort, DatatypeFactory factory
  ) {
    super(sequencesSort, factory);
    this.numFiles = numFiles;
    this.numSequences = numSequences;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup group) {
    if (this.numFiles * this.numSequences > group.getSequenceCount()) {
      throw new TransformationException("Not enough sequences. The result of multiplying the number of "
        + "files by the number of sequences must be less than or equal to the actual number of sequences.");
    }

    List<SequencesGroup> toret = new LinkedList<>();
    List<Sequence> input = getInputSequencesGroup(group);
    for (int file = 0; file < this.numFiles; file++) {
      int startIndex = file * this.numSequences;
      int endIndex = (file + 1) * this.numSequences;
      List<Sequence> currentSubList = input.subList(startIndex, endIndex);

      toret.add(createGroup(group.getName() + "_" + (file + 1), group.getProperties(), currentSubList));
    }
    return toret;
  }
}
