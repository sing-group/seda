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
package org.sing_group.seda.io;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceCase;
import org.sing_group.seda.datatype.SequencesGroup;

public class FastaWriter {
  public static void writeFasta(Path file, Sequence ... sequences) {
    writeFasta(file, stream(sequences));
  }

  public static void writeFasta(Path file, Stream<Sequence> sequences) {
    writeFasta(file, sequences, SequencesGroup.DEFAULT_LINE_BREAK_OS);
  }

  public static void writeFasta(Path file, Stream<Sequence> sequences, String lineBreak) {
    try {
      final List<String> fastaLines = sequences
        .map(sequence -> new String[] { getSequenceHeader(sequence), formatSequenceChain(sequence, lineBreak) })
        .flatMap(Arrays::stream)
      .collect(toList());

      final FileWriter fileWriter = new FileWriter(file.toFile());
      for (String line : fastaLines) {
        fileWriter.write(line);
        fileWriter.write(lineBreak);
      }
      fileWriter.close();

    } catch (IOException e) {
      throw new RuntimeException("Unexpected error creating temporary file.", e);
    }
  }

  private static String getSequenceHeader(Sequence sequence) {
    return ">" + sequence.getName() + (sequence.getDescription().isEmpty() ? "" : " " + sequence.getDescription());
  }

  private static String formatSequenceChain(Sequence sequence, String lineBreak) {
    Optional<SequenceCase> sequenceCase = sequence.getProperty(Sequence.PROPERTY_CASE);

    String sequenceChain;
    if (sequenceCase.isPresent() && !sequenceCase.get().equals(SequenceCase.ORIGINAL)) {
      if (sequenceCase.get().equals(SequenceCase.LOWERCASE)) {
        sequenceChain = sequence.getChain().toLowerCase();
      } else {
        sequenceChain = sequence.getChain().toUpperCase();
      }
    } else {
      sequenceChain = sequence.getChain();
    }

    Optional<Integer> columns = sequence.getProperty(Sequence.PROPERTY_CHAIN_COLUMNS);
    if (columns.isPresent()) {
      return getParts(sequenceChain, columns.get()).stream().collect(Collectors.joining(lineBreak));
    } else {
      return sequenceChain;
    }
  }

  private static List<String> getParts(String string, int partitionSize) {
    List<String> parts = new ArrayList<String>();
    int len = string.length();
    for (int i = 0; i < len; i += partitionSize) {
      parts.add(string.substring(i, Math.min(len, i + partitionSize)));
    }
    return parts;
  }
}
