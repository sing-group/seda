package org.sing_group.seda.bio.consensus;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public interface SequencesGroupConsensus {
  public Sequence getConsensus(SequencesGroup consensus);
}
