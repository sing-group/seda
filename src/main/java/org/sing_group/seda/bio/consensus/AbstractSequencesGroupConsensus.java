package org.sing_group.seda.bio.consensus;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public abstract class AbstractSequencesGroupConsensus implements SequencesGroupConsensus {
  public static final String AMBIGUITY_CHARACTER = "X";

  private SequenceBuilder sequenceBuilder;
  protected double minimumPresence;
  protected boolean verbose;

  public AbstractSequencesGroupConsensus(SequenceBuilder sequenceBuilder, double minimumPresence, boolean verbose) {
    this.minimumPresence = minimumPresence;
    this.sequenceBuilder = sequenceBuilder;
    this.verbose = verbose;
  }

  @Override
  public Sequence getConsensus(SequencesGroup sequencesGroup) {
    Map<Integer, Map<Character, Integer>> consensusMap = new HashMap<>();

    for (Sequence sequence : sequencesGroup.getSequences().collect(Collectors.toList())) {
      if (consensusMap.keySet().size() > 0 && consensusMap.keySet().size() != sequence.getLength()) {
        throw new TransformationException("All sequences must have the same length");
      }

      String chain = sequence.getChain();
      for (int i = 0; i < chain.length(); i++) {
        Character charAt = chain.charAt(i);
        consensusMap.putIfAbsent(i, new HashMap<>());
        consensusMap.get(i).putIfAbsent(charAt, 0);
        consensusMap.get(i).put(charAt, consensusMap.get(i).get(charAt) + 1);
      }
    }

    StringBuilder consensusChain = new StringBuilder();
    for (Integer key : consensusMap.keySet()) {
      int maxCount = 0;
      List<Character> maxCharacter = new LinkedList<>();
      for (Entry<Character, Integer> entry : consensusMap.get(key).entrySet()) {
        if (entry.getValue() > maxCount) {
          maxCount = entry.getValue();
          maxCharacter.clear();
          maxCharacter.add(entry.getKey());
        } else if (entry.getValue() == maxCount) {
          maxCharacter.add(entry.getKey());
        }
      }

      double maxPresence = (double) maxCount / (double) sequencesGroup.getSequenceCount();

      if (maxCharacter.size() == 1 && maxPresence >= minimumPresence) {
        consensusChain.append(maxCharacter.get(0));
      } else {
        consensusChain.append(solveAmbiguity(maxCharacter, maxPresence));
      }
    }

    Sequence consensus =
      this.sequenceBuilder.of(
        "consensus_" + sequencesGroup.getName(), "", consensusChain.toString(), Collections.emptyMap()
      );

    return consensus;
  }

  protected abstract String solveAmbiguity(List<Character> maxCharacter, double maxPresence);
}
