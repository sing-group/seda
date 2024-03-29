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
package org.sing_group.seda.transformation.sequencesgroup;

import static java.lang.System.lineSeparator;
import static java.nio.file.Files.write;
import static java.util.Optional.ofNullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
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
  
  public enum Mode {
    EXACT_DUPLICATES, CONTAINED_SEQUENCES
  };

  @XmlRootElement
  public static class RemoveRedundantSequencesTransformationConfiguration {

    @XmlElement
    private Mode mode;
    @XmlElement
    private boolean mergeHeaders;
    @XmlElement
    private File mergedSequences;
    @XmlElement
    private SequenceTranslationConfiguration sequenceTranslationConfiguration;

    public RemoveRedundantSequencesTransformationConfiguration() {}

    public RemoveRedundantSequencesTransformationConfiguration(Mode mode, boolean mergeHeaders) {
      this(mode, mergeHeaders, null, null);
    }

    public RemoveRedundantSequencesTransformationConfiguration(
      Mode mode, boolean mergeHeaders, SequenceTranslationConfiguration sequenceTranslationConfiguration
    ) {
      this(mode, mergeHeaders, null, sequenceTranslationConfiguration);
    }

    public RemoveRedundantSequencesTransformationConfiguration(
      Mode mode, boolean mergeHeaders, File mergedSequencesListDirectory
    ) {
      this(mode, mergeHeaders, mergedSequencesListDirectory, null);
    }

    public RemoveRedundantSequencesTransformationConfiguration(
      Mode mode, boolean mergeHeaders,
      File mergedSequencesListDirectory, SequenceTranslationConfiguration sequenceTranslationConfiguration
    ) {
      this.mode = mode;
      this.mergeHeaders = mergeHeaders;
      this.mergedSequences = mergedSequencesListDirectory;
      this.sequenceTranslationConfiguration = sequenceTranslationConfiguration;
    }

    public Mode getMode() {
      return mode;
    }

    public boolean isMergeHeaders() {
      return mergeHeaders;
    }

    public Optional<File> getMergedSequencesListDirectory() {
      return ofNullable(mergedSequences);
    }

    public Optional<SequenceTranslationConfiguration> getSequenceTranslationConfiguration() {
      return ofNullable(this.sequenceTranslationConfiguration);
    }
  }

  private final SequenceBuilder sequenceBuilder;
  private final SequencesGroupBuilder groupBuilder;
  private final Mode mode;
  private boolean mergeHeaders;
  private File mergedSequencesListDirectory;
  private Optional<SequenceTranslationConfiguration> translationConfiguration;

  public RemoveRedundantSequencesTransformation() {
    this(new RemoveRedundantSequencesTransformationConfiguration(DEFAULT_MODE, DEFAULT_MERGE_HEADERS));
  }

  public RemoveRedundantSequencesTransformation(RemoveRedundantSequencesTransformationConfiguration configuration) {
    this(configuration, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public RemoveRedundantSequencesTransformation(
    RemoveRedundantSequencesTransformationConfiguration configuration,
    SequenceTranslationConfiguration translationConfiguration
  ) {
    this(configuration, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public RemoveRedundantSequencesTransformation(
    RemoveRedundantSequencesTransformationConfiguration configuration,
    DatatypeFactory factory
  ) {
    this.mode = configuration.getMode();
    this.translationConfiguration = configuration.getSequenceTranslationConfiguration();
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

    for (Sequence inputSequence : sortedSequences) {
      Optional<Sequence> match = currentSequenceIsRedundant(inputSequence, filteredSequences);
      if (match.isPresent()) {
        Sequence matchSequence = match.get();

        String matchSequenceHeader = matchSequence.getHeader();

        mergedSequences.putIfAbsent(matchSequenceHeader, new LinkedList<String>());
        StringBuilder inputSequenceHeaderSb = new StringBuilder(inputSequence.getName().replace(">", ""));
        if (!inputSequence.getDescription().isEmpty()) {
          inputSequenceHeaderSb.append(" ").append(inputSequence.getDescription());
        }
        String inputSequenceHeader = inputSequenceHeaderSb.toString();
        mergedSequences.get(matchSequenceHeader).add(inputSequenceHeader);

        if (mergeHeaders) {
          StringBuilder newDescriptionSb = new StringBuilder();
          if (!matchSequence.getDescription().isEmpty()) {
            newDescriptionSb.append(matchSequence.getDescription()).append(" ");
          }
          newDescriptionSb.append("[").append(inputSequenceHeader).append("]");

          filteredSequences.remove(matchSequence);
          Sequence mergedSequence = this.sequenceBuilder.of(
            matchSequence.getName(), newDescriptionSb.toString(), matchSequence.getChain(),
            matchSequence.getProperties()
          );
          filteredSequences.add(mergedSequence);
        }
      } else {
        filteredSequences.add(inputSequence);
      }
    }

    if (this.mergedSequencesListDirectory != null) {
      saveMergedSequences(mergedSequences, sequencesGroup.getName());
    }

    return this.groupBuilder.of(
      sequencesGroup.getName(), sequencesGroup.getProperties(),
      new LinkedList<>(filteredSequences)
    );
  }

  private void saveMergedSequences(Map<String, List<String>> mergedSequences, String groupName) {
    StringBuilder sb = new StringBuilder();

    for (Entry<String, List<String>> entry : mergedSequences.entrySet()) {
      List<String> values = entry.getValue();

      if (values.isEmpty()) {
        continue;
      }

      sb
        .append("\"")
        .append(entry.getKey().substring(1))
        .append("\" replaces the following sequences:")
        .append(lineSeparator());

      values.forEach(v -> {
        sb
          .append("\t")
          .append(v)
          .append(lineSeparator());
      });
      sb.append(lineSeparator());
    }

    try {
      this.mergedSequencesListDirectory.mkdirs();
      write(
        new File(this.mergedSequencesListDirectory, groupName + "_merge-list.txt").toPath(), sb.toString().getBytes()
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class EvaluableSequence {
    List<String> evaluableChains;
    private Sequence sequence;

    public EvaluableSequence(Sequence sequence, SequenceTranslationConfiguration translationConfiguration) {
      this.sequence = sequence;
      this.evaluableChains = new LinkedList<>();
      for (int frame : translationConfiguration.getFrames()) {
        this.evaluableChains.add(translate(sequence.getChain(), frame, translationConfiguration));
      }
    }

    private String translate(String chain, int frame, SequenceTranslationConfiguration translationConfiguration) {
      return SequenceUtils.translate(
        chain,
        translationConfiguration.isReverseComplement(),
        frame,
        translationConfiguration.getCodonTable()
      );
    }

    public EvaluableSequence(Sequence sequence) {
      this.sequence = sequence;
      this.evaluableChains = new LinkedList<>(Arrays.asList(sequence.getChain()));
    }

    public int count() {
      return evaluableChains.size();
    }

    public String get(int i) {
      return evaluableChains.get(i);
    }

    public Sequence getSequence() {
      return sequence;
    }

    public int length() {
      return this.sequence.getLength();
    }
  }

  private Optional<Sequence> currentEvaluableSequenceIsRedundant(
    EvaluableSequence inputSequence, Set<EvaluableSequence> filteredSequences
  ) {
    for (EvaluableSequence filteredSequence : filteredSequences) {

      for (int i = 0; i < filteredSequence.count(); i++) {
        String inputSequenceChain = inputSequence.get(i);
        String filteredSequenceChain = filteredSequence.get(i);

        if (inputSequenceChain.equals(filteredSequenceChain)) {
          return Optional.of(filteredSequence.getSequence());
        } else {
          if (this.mode.equals(Mode.CONTAINED_SEQUENCES)) {
            if (filteredSequenceChain.contains(inputSequenceChain)) {
              return Optional.of(filteredSequence.getSequence());
            }
          }
        }
      }

      if (inputSequence.length() > filteredSequence.length()) {
        break;
      }
    }

    return Optional.empty();
  }

  private Optional<Sequence> currentSequenceIsRedundant(Sequence inputSequence, Set<Sequence> filteredSequences) {
    return currentEvaluableSequenceIsRedundant(
      asEvaluableSequence(inputSequence), asEvaluableSequences(filteredSequences)
    );
  }

  private EvaluableSequence asEvaluableSequence(Sequence sequence) {
    if (this.translationConfiguration.isPresent()) {
      return new EvaluableSequence(sequence, translationConfiguration.get());
    } else {
      return new EvaluableSequence(sequence);
    }
  }

  private Set<EvaluableSequence> asEvaluableSequences(Set<Sequence> filteredSequences) {
    Set<EvaluableSequence> evaluableSequences =
      new TreeSet<EvaluableSequence>(
        new Comparator<EvaluableSequence>() {

          @Override
          public int compare(EvaluableSequence o1, EvaluableSequence o2) {
            int comparison = o2.getSequence().getChain().length() - o1.getSequence().getChain().length();
            return comparison == 0 ? o2.getSequence().getChain().compareTo(o1.getSequence().getChain()) : comparison;
          }
        }
      );
    filteredSequences.forEach(s -> evaluableSequences.add(asEvaluableSequence(s)));

    return evaluableSequences;
  }

  private List<Sequence> sortBySequenceLength(SequencesGroup sequencesGroup) {
    List<Sequence> sorted = new LinkedList<>();
    sorted.addAll(sequencesGroup.getSequences().collect(Collectors.toList()));
    Collections.sort(sorted, SEQUENCE_COMPARATOR);

    return sorted;
  }
}
