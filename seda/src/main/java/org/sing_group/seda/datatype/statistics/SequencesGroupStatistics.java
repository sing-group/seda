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
package org.sing_group.seda.datatype.statistics;

import static java.util.stream.Collectors.summarizingInt;

import java.nio.file.Path;
import java.util.IntSummaryStatistics;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class SequencesGroupStatistics {

  private Path path;
  private SequencesGroup sequences;
  private String name;
  private int sequenceCount;
  private IntSummaryStatistics lengthSummary;

  public SequencesGroupStatistics(Path path, SequencesGroup sequences) {
    this.path = path;
    this.sequences = sequences;
    this.name = sequences.getName();
    this.sequenceCount = sequences.getSequenceCount();
    this.lengthSummary = sequences.getSequences().collect(summarizingInt(Sequence::getLength));
  }

  public String getName() {
    return name;
  }

  public int getSequenceCount() {
    return sequenceCount;
  }

  public int getMaxSequenceLength() {
    return this.lengthSummary.getMax();
  }

  public int getMinSequenceLength() {
    return this.lengthSummary.getMin();
  }

  public SequencesGroup getSequences() {
    return sequences;
  }

  public long getFileSize() {
    return path.toFile().length();
  }
}
