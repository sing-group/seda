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

public abstract class AbstractSequencesGroupAboveThresholdConsensus implements SequencesGroupConsensus {
  private SequenceBuilder sequenceBuilder;
  protected double minimumPresence;
  protected boolean verbose;

  public AbstractSequencesGroupAboveThresholdConsensus(
    SequenceBuilder sequenceBuilder, double minimumPresence, boolean verbose
  ) {
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
      List<Character> maxCharacter = new LinkedList<>();
      for (Entry<Character, Integer> entry : consensusEntry.getValue().entrySet()) {
        double entryPresence = (double) entry.getValue() / (double) sequencesGroup.getSequenceCount();
        if (entryPresence >= minimumPresence) {
          maxCharacter.add(entry.getKey());
        }
      }

      if (maxCharacter.size() == 0) {
        consensusChain.append(ambiguityCharacter());
      } else if (maxCharacter.size() == 1) {
        consensusChain.append(maxCharacter.get(0).toString());
      } else {
        consensusChain.append(solveAmbiguity(maxCharacter));
      }
    }

    Sequence consensus =
      this.sequenceBuilder.of(
        "consensus_" + sequencesGroup.getName(), "", consensusChain.toString(), Collections.emptyMap()
      );

    return consensus;
  }

  protected abstract String solveAmbiguity(List<Character> maxCharacter);

  protected abstract String ambiguityCharacter();
}
