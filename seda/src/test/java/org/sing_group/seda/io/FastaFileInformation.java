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
package org.sing_group.seda.io;

import static java.util.Arrays.stream;

import java.nio.file.Path;
import java.util.Random;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.io.FastaReader.SequenceTextInfo;
import org.sing_group.seda.io.FastaReader.SequenceLocationsInfo;

public interface FastaFileInformation {
  public Path getPath();
  public Sequence[] getSequences();
  public SequenceTextInfo[] getSequenceTextInfos();
  public SequenceLocationsInfo[] getSequenceLocationInfos();
  
  public default String getSequenceGroupName() {
    return this.getPath().getFileName().toString();
  }
  
  public default int getSequencesCount() {
    return this.getSequences().length;
  }

  public default Sequence[] getMixedSequences() {
    final Random random = new Random(33);

    return stream(getSequences())
      .map(sequence -> random.nextBoolean() ? sequence : new LazyFileSequence(sequence))
    .toArray(Sequence[]::new);
  }

  public default Sequence[] getLazySequences() {
    return stream(getSequences())
      .map(LazyFileSequence::new)
    .toArray(Sequence[]::new);
  }
  
  public static FastaFileInformation of(
    final Path path,
    final Sequence[] sequences,
    final SequenceTextInfo[] sequenceTextInfos,
    final SequenceLocationsInfo[] sequenceLocationInfos
  ) {
    return new FastaFileInformation() {
      @Override
      public Path getPath() {
        return path;
      }
      
      @Override
      public Sequence[] getSequences() {
        return sequences;
      }
      
      @Override
      public SequenceTextInfo[] getSequenceTextInfos() {
        return sequenceTextInfos;
      }
      
      @Override
      public SequenceLocationsInfo[] getSequenceLocationInfos() {
        return sequenceLocationInfos;
      }
    };
  }
}
