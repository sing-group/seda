package org.sing_group.seda.bio.consensus;

import java.util.List;

import org.sing_group.seda.datatype.SequenceBuilder;

public class NucleotideSequencesGroupConsensus extends AbstractSequencesGroupConsensus {

  public NucleotideSequencesGroupConsensus(SequenceBuilder sequenceBuilder, double minimumPresence, boolean verbose) {
    super(sequenceBuilder, minimumPresence, verbose);
  }

  @Override
  protected String solveAmbiguity(List<Character> maxCharacter, double maxPresence) {
    if (maxPresence < minimumPresence) {
      return AMBIGUITY_CHARACTER;
    } else {
      if (maxCharacter.size() == 2) {
        if (maxCharacter.contains('T') && maxCharacter.contains('C')) {
          return "Y";
        } else if (maxCharacter.contains('A') && maxCharacter.contains('G')) {
          return "R";
        } else if (maxCharacter.contains('A') && maxCharacter.contains('T')) {
          return "W";
        } else if (maxCharacter.contains('G') && maxCharacter.contains('C')) {
          return "S";
        } else if (maxCharacter.contains('T') && maxCharacter.contains('G')) {
          return "K";
        } else {
          return "M";
        }
      } else if (maxCharacter.size() == 3) {
        if (!maxCharacter.contains('C')) {
          return "D";
        } else if (!maxCharacter.contains('T')) {
          return "V";
        }
        if (!maxCharacter.contains('G')) {
          return "H";
        } else {
          return "B";
        }
      } else {
        return AMBIGUITY_CHARACTER;
      }
    }
  }
}