package org.sing_group.seda.transformation.msa;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.bio.StopCodon;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;

public class RemoveInFrameStopCodonsMSATransformation extends FilterMSATransformation {
  
  public RemoveInFrameStopCodonsMSATransformation() {
    super(RemoveInFrameStopCodonsMSATransformation::doesNotHaveInFrameStopCodons);
  }

  public RemoveInFrameStopCodonsMSATransformation(DatatypeFactory factory) {
    super(RemoveInFrameStopCodonsMSATransformation::doesNotHaveInFrameStopCodons, factory);
  }

  private final static boolean doesNotHaveInFrameStopCodons(Sequence sequence) {
    final String chain = sequence.getChain();
    final String chainWithoutLastCodon = chain.substring(0, chain.length() - 3);

    return SequenceUtils.toCodons(chainWithoutLastCodon)
      .noneMatch(StopCodon::isAStopCodon);
  }

}
