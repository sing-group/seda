package org.sing_group.seda.bio.consensus;

import java.util.List;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.SequenceBuilder;

public class ProteinSequencesGroupConsensus extends AbstractSequencesGroupConsensus {

  public ProteinSequencesGroupConsensus(SequenceBuilder sequenceBuilder, double minimumPresence, boolean verbose) {
    super(sequenceBuilder, minimumPresence, verbose);
  }

  @Override
  protected String solveAmbiguity(List<Character> maxCharacter, double maxPresence) {
    if (maxPresence < minimumPresence) {
      return AMBIGUITY_CHARACTER;
    } else {
      if (verbose) {
        return maxCharacter.stream().map(c -> c.toString()).collect(Collectors.joining("", "[", "]"));
      } else {
        return AMBIGUITY_CHARACTER;
      }
    }
  }
}
