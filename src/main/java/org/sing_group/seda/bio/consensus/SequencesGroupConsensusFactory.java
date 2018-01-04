package org.sing_group.seda.bio.consensus;

import org.sing_group.seda.bio.SequenceType;
import org.sing_group.seda.datatype.SequenceBuilder;

public class SequencesGroupConsensusFactory {

  public static SequencesGroupConsensus getConsensusCreator(
    SequenceType sequenceType, SequenceBuilder sequenceBuilder, double minimumPresence, boolean verbose
  ) {
    if (sequenceType.equals(SequenceType.NUCLEOTIDE)) {
      return new NucleotideSequencesGroupConsensus(sequenceBuilder, minimumPresence, verbose);
    } else {
      return new ProteinSequencesGroupConsensus(sequenceBuilder, minimumPresence, verbose);
    }
  }
}
