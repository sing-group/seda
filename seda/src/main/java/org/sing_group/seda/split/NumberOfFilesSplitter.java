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

import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class NumberOfFilesSplitter extends AbstractSequencesGroupSplitter {
  private int numFiles;

  public NumberOfFilesSplitter(int numFiles) {
    this(numFiles, false);
  }

  public NumberOfFilesSplitter(int numFiles, DatatypeFactory factory) {
    this(numFiles, false, factory);
  }

  public NumberOfFilesSplitter(int numFiles, boolean randomize) {
    this(numFiles, randomize, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public NumberOfFilesSplitter(int numFiles, boolean randomize, DatatypeFactory factory) {
    super(randomize, factory);
    this.numFiles = numFiles;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup group) {
    if (this.numFiles > group.getSequenceCount()) {
      throw new TransformationException(
        "The required number of files must be lesser than or equal to the actual number of sequences."
      );
    }

    List<SequencesGroup> toret = new LinkedList<>();
    List<Sequence> input = getInputSequencesGroup(group);

    int partitionSize = Math.round(group.getSequenceCount() / this.numFiles);
    
    for(int i = 0; i < this.numFiles; i++) {
      int startIndex = i * partitionSize;
      int endIndex = i == this.numFiles-1 ? input.size() : (i+1) * partitionSize;
      List<Sequence> currentSubList = input.subList(startIndex, endIndex);

      toret.add(createGroup(group.getName() + "_" + (i + 1), group.getProperties(), currentSubList));
    }

    return toret;
  }
}
