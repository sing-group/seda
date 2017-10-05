package org.sing_group.seda.io;

import static java.util.Arrays.stream;
import static org.sing_group.seda.io.FastaWriter.writeFasta;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class DatasetProcessor {
  private final DatatypeFactory factory;

  public DatasetProcessor(DatatypeFactory factory) {
    this.factory = factory;
  }

  public void process(Path[] inputs, Path output, SequencesGroupDatasetTransformation transformation, int groupSize) throws IOException {
    process(stream(inputs), output, transformation, groupSize);
  }

  public void process(Path inputDirectory, Path output, SequencesGroupDatasetTransformation transformation, int groupSize) throws IOException {
    process(findSequencesGroupFiles(inputDirectory), output, transformation, groupSize);
  }

  public void process(Stream<Path> inputs, Path output, SequencesGroupDatasetTransformation transformation, int groupSize) throws IOException {
    try (final Stream<Path> sequenceFiles = inputs) {
      final SequencesGroup[] sequences = sequenceFiles
        .map(this.factory::newSequencesGroup)
      .toArray(SequencesGroup[]::new);

      final SequencesGroupDataset dataset = transformation.transform(
        this.factory.newSequencesGroupDataset(sequences)
      );
      final SequencesGroup[] sequencesGroups = dataset.getSequencesGroups().toArray(SequencesGroup[]::new);


      final Namer namer = new Namer();
      int count = 0;
      Path groupOutput = output;
      for (SequencesGroup sequencesGroup : sequencesGroups) {
        if (groupSize >= 1 && count % groupSize == 0) {
          groupOutput = output.resolve("group" + (count / groupSize + 1));
          Files.createDirectories(groupOutput);
          namer.clearNames();
        }

        final String name = namer.uniqueName(sequencesGroup.getName());
        writeFasta(groupOutput.resolve(name), sequencesGroup.getSequences());
        count++;
      };
    }
  }

  private static class Namer {
    private final Set<String> names;

    public Namer() {
      this.names = new HashSet<>();
    }

    public void clearNames() {
      this.names.clear();
    }

    public String uniqueName(String name) {
      int i = 1;

      String uniqueName = name;
      while (names.contains(uniqueName)) {
        uniqueName = composeName(name, i++);
      }
      names.add(uniqueName);

      return uniqueName;
    }
  }

  private static String composeName(String name, int count) {
    if (name.contains(".")) {
      final int dotIndex = name.lastIndexOf('.');

      if (dotIndex == name.length() - 1) {
        return name + "_" + count;
      } else {
        final String filename = name.substring(0, dotIndex);
        final String extension = name.substring(dotIndex + 1);

        return filename + "_" + count + "." + extension;
      }
    } else {
      return name + "_" + count;
    }
  }

  protected static Stream<Path> findSequencesGroupFiles(Path input) throws IOException {
    return Files.find(
      input, Integer.MAX_VALUE,
      (file, attrs) -> attrs.isRegularFile() && file.getFileName().toString().toLowerCase().endsWith("fasta"),
      FileVisitOption.FOLLOW_LINKS
    );
  }
}
