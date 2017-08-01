package org.sing_group.seda.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.Sequence;

public class LazyFileSequencesGroup implements SequencesGroup {
  private final String name;
  private final Path file;

  public LazyFileSequencesGroup(String name, Sequence ... sequences) {
    try {
      this.name = name;
      this.file = Files.createTempFile("seda_" + name, ".fasta");
      this.file.toFile().deleteOnExit();

      DatasetProcessor.writeFasta(this.file, sequences);
    } catch (IOException e) {
      throw new RuntimeException("Unexpected error creating temporary file.", e);
    }
  }

  public LazyFileSequencesGroup(Path file) {
    this.name = file.getFileName().toString();
    this.file = file;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Stream<Sequence> getSequences() {
    return readFileSequences().stream();
  }

  @Override
  public int getSequenceCount() {
    return readFileSequences().size();
  }

  private List<Sequence> readFileSequences() {
    try {
      final List<String> lines = Files.readAllLines(this.file);
      final List<Sequence> sequencesList = new LinkedList<>();

      String name = null;
      String description = null;
      int lineWidth = 0;
      Map<String, Object> additionalInformation = new HashMap<>();
      StringBuilder chain = new StringBuilder();

      for (String line : lines) {
        if (line.trim().isEmpty()) {
          if (name != null && chain.length() > 0) {
            additionalInformation.put(Sequence.PROPERTY_CHAIN_COLUMNS, lineWidth);
            sequencesList.add(Sequence.of(name, description, chain.toString(), additionalInformation));

            name = null;
            description = null;
            lineWidth = 0;
            additionalInformation = new HashMap<>();
            chain = new StringBuilder();

          } else if (name != null || chain.length() == 0) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", this.file, line));
          }
        } else if (line.startsWith(">")) {
          if (name != null && chain.length() > 0) {
            additionalInformation.put(Sequence.PROPERTY_CHAIN_COLUMNS, lineWidth);
            sequencesList.add(Sequence.of(name, description, chain.toString(), additionalInformation));

            name = null;
            description = null;
            lineWidth = 0;
            additionalInformation = new HashMap<>();
            chain = new StringBuilder();

          } else if (name != null || chain.length() > 0) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", this.file, line));
          }

          final int spaceIndex = line.indexOf(" ");
          if (spaceIndex > 0) {
            name = line.substring(0, spaceIndex);
            description = line.substring(spaceIndex + 1);
          } else {
            name = line;
            description = "";
          }
        } else {
          if (name == null) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", this.file, line));
          } else {
            String nextChainLine = line.trim();
            lineWidth = Math.max(lineWidth, nextChainLine.length());
            chain.append(nextChainLine);
          }
        }
      }

      if (name != null && chain.length() > 0) {
        additionalInformation.put(Sequence.PROPERTY_CHAIN_COLUMNS, lineWidth);
        sequencesList.add(Sequence.of(name, description, chain.toString(), additionalInformation));
      }

      return sequencesList;
    } catch (IOException ioe) {
      throw new RuntimeException("Error reading file: " + this.file.toString(), ioe);
    }
  }
}
