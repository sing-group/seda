package org.sing_group.seda.transformation.sequencesgroup;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.bio.StopCodon;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;

public class RemoveInFrameStopCodonsSequencesGroupTransformation extends FilterSequencesGroupTransformation {
  
  public RemoveInFrameStopCodonsSequencesGroupTransformation() {
    super(RemoveInFrameStopCodonsSequencesGroupTransformation::doesNotHaveInFrameStopCodons);
  }

  public RemoveInFrameStopCodonsSequencesGroupTransformation(DatatypeFactory factory) {
    super(RemoveInFrameStopCodonsSequencesGroupTransformation::doesNotHaveInFrameStopCodons, factory);
  }

  private final static boolean doesNotHaveInFrameStopCodons(Sequence sequence) {
    final String chain = sequence.getChain();
    final String chainWithoutLastCodon = chain.substring(0, chain.length() - 3);

    return SequenceUtils.toCodons(chainWithoutLastCodon)
      .noneMatch(StopCodon::isAStopCodon);
  }

}
