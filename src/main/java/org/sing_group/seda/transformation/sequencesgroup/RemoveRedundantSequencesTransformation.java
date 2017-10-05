package org.sing_group.seda.transformation.sequencesgroup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class RemoveRedundantSequencesTransformation implements SequencesGroupTransformation {
  private static final Mode DEFAULT_MODE = Mode.EXACT_DUPLICATES;
  private static final boolean DEFAULT_MERGE_HEADERS = false;
  private static final Comparator<? super Sequence> SEQUENCE_COMPARATOR = new Comparator<Sequence>() {

    @Override
    public int compare(Sequence o1, Sequence o2) {
      int comparison = o2.getChain().length() - o1.getChain().length();
      return comparison == 0 ? o2.getChain().compareTo(o1.getChain()) : comparison;
    }
  };

  public static class RemoveRedundantSequencesTransformationConfiguration {

    private Mode mode;
    private boolean mergeHeaders;
    private File mergedSequences;

    public RemoveRedundantSequencesTransformationConfiguration(Mode mode, boolean mergeHeaders) {
      this(mode, mergeHeaders, null);
    }

    public RemoveRedundantSequencesTransformationConfiguration(Mode mode, boolean mergeHeaders, File mergedSequencesListDirectory) {
      this.mode = mode;
      this.mergeHeaders = mergeHeaders;
      this.mergedSequences = mergedSequencesListDirectory;
    }

    public Mode getMode() {
      return mode;
    }

    public boolean isMergeHeaders() {
      return mergeHeaders;
    }

    public Optional<File> getMergedSequencesListDirectory() {
      return Optional.ofNullable(mergedSequences);
    }
  }

  private final SequenceBuilder sequenceBuilder;
  private final BiFunction<String, List<Sequence>, SequencesGroup> groupBuilder;

  public enum Mode {
    EXACT_DUPLICATES, CONTAINED_SEQUENCES
  };

  private final Mode mode;
  private boolean mergeHeaders;
  private File mergedSequencesListDirectory;

  public RemoveRedundantSequencesTransformation() {
    this(new RemoveRedundantSequencesTransformationConfiguration(DEFAULT_MODE, DEFAULT_MERGE_HEADERS));
  }

  public RemoveRedundantSequencesTransformation(RemoveRedundantSequencesTransformationConfiguration configuration) {
    this(configuration, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public RemoveRedundantSequencesTransformation(
    RemoveRedundantSequencesTransformationConfiguration configuration, DatatypeFactory factory
  ) {
    this.mode = configuration.getMode();
    this.mergeHeaders = configuration.isMergeHeaders();
    if (configuration.getMergedSequencesListDirectory().isPresent()) {
      this.mergedSequencesListDirectory = configuration.getMergedSequencesListDirectory().get();
    }
    this.groupBuilder = factory::newSequencesGroup;
    this.sequenceBuilder = factory::newSequence;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    Map<String, List<String>> mergedSequences = new HashMap<>();
    List<Sequence> sortedSequences = sortBySequenceLength(sequencesGroup);
    Set<Sequence> filteredSequences = new TreeSet<Sequence>(SEQUENCE_COMPARATOR);

    for(Sequence inputSequence : sortedSequences) {
      Optional<Sequence> match = currentSequenceIsRedundant(inputSequence, filteredSequences);
      if(match.isPresent()) {
        Sequence matchSequence = match.get();

        String matchSequenceHeader = matchSequence.getName() + matchSequence.getDescription();

        mergedSequences.putIfAbsent(matchSequenceHeader, new LinkedList<String>());
        String inputSequenceHeader = inputSequence.getName().replace(">", "") + " " + inputSequence.getDescription();
        mergedSequences.get(matchSequenceHeader).add(inputSequenceHeader);

        if(mergeHeaders) {
          filteredSequences.remove(matchSequence);
          Sequence mergedSequence =
            this.sequenceBuilder.of(
              matchSequence.getName(), matchSequence.getDescription() + " [" + inputSequenceHeader + "]",
              matchSequence.getChain(), matchSequence.getProperties()
            );
          filteredSequences.add(mergedSequence);
        }
      } else {
        filteredSequences.add(inputSequence);
      }
    }

    if(this.mergedSequencesListDirectory != null) {
      saveMergedSequences(mergedSequences, sequencesGroup.getName());
    }

    return this.groupBuilder.apply(sequencesGroup.getName(), new LinkedList<>(filteredSequences));
  }

  private void saveMergedSequences(Map<String, List<String>> mergedSequences, String groupName) {
    StringBuilder sb = new StringBuilder();

    for(String key : mergedSequences.keySet()) {
      List<String> values = mergedSequences.get(key);

      if(values.isEmpty()) {
        continue;
      }

      sb
        .append("\"")
        .append(key)
        .append("\" replaces the following sequences:")
        .append("\n");

      values.forEach(v -> {
        sb
        .append("\t")
        .append(v)
        .append("\n");
      });
      sb.append("\n");
    }

    try {
      Files.write(
        new File(this.mergedSequencesListDirectory, groupName + "_merge-list.txt").toPath(), sb.toString().getBytes()
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Optional<Sequence> currentSequenceIsRedundant(Sequence inputSequence, Set<Sequence> filteredSequences) {
    for (Sequence filteredSequence : filteredSequences) {

      if (inputSequence.getChain().equals(filteredSequence.getChain())) {
        return Optional.of(filteredSequence);
      } else {
        if (this.mode.equals(Mode.CONTAINED_SEQUENCES)) {
          if (filteredSequence.getChain().contains(inputSequence.getChain())) {
            return Optional.of(filteredSequence);
          }
        }
      }

      if(inputSequence.getChain().length() > filteredSequence.getChain().length()) {
        break;
      }
    }

    return Optional.empty();
  }

  private List<Sequence> sortBySequenceLength(SequencesGroup sequencesGroup) {
    List<Sequence> sorted = new LinkedList<>();
    sorted.addAll(sequencesGroup.getSequences().collect(Collectors.toList()));
    Collections.sort(sorted, SEQUENCE_COMPARATOR);

    return sorted;
  }
}
