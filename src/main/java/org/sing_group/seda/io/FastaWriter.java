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
        .map(sequence -> new String[] { getSequenceHeader(sequence), formatSequenceChain(sequence) })
        .flatMap(Arrays::stream)
      .collect(toList());

      final FileWriter fileWriter = new FileWriter(file.toFile());
      for (String line : fastaLines) {
        fileWriter.write(line);
        fileWriter.write(lineBreak);
      }
      ;
      fileWriter.close();

    } catch (IOException e) {
      throw new RuntimeException("Unexpected error creating temporary file.", e);
    }
  }

  private static String getSequenceHeader(Sequence sequence) {
    return ">" + sequence.getName() + (sequence.getDescription().isEmpty() ? "" : " " + sequence.getDescription());
  }

  private static String formatSequenceChain(Sequence sequence) {
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
      return getParts(sequenceChain, columns.get()).stream().collect(Collectors.joining("\n"));
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
