/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
    for (Entry<Integer, Map<Character, Integer>> consensusEntry : consensusMap.entrySet()) {
      int maxCount = 0;
      List<Character> maxCharacter = new LinkedList<>();
      for (Entry<Character, Integer> entry : consensusEntry.getValue().entrySet()) {
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
