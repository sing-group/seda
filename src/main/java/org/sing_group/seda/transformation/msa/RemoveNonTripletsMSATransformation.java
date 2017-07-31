package org.sing_group.seda.transformation.msa;

import org.sing_group.seda.datatype.DatatypeFactory;

public class RemoveNonTripletsMSATransformation extends FilterMSATransformation {
  
  public RemoveNonTripletsMSATransformation() {
    super(sequence -> sequence.getChain().length() % 3 == 0);
  }

  public RemoveNonTripletsMSATransformation(DatatypeFactory factory) {
    super(sequence -> sequence.getChain().length() % 3 == 0, factory);
  }

}
