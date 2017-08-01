package org.sing_group.seda.transformation.sequencesgroup;

import org.sing_group.seda.datatype.DatatypeFactory;

public class RemoveNonTripletsSequencesGroupTransformation extends FilterSequencesGroupTransformation {
  
  public RemoveNonTripletsSequencesGroupTransformation() {
    super(sequence -> sequence.getChain().length() % 3 == 0);
  }

  public RemoveNonTripletsSequencesGroupTransformation(DatatypeFactory factory) {
    super(sequence -> sequence.getChain().length() % 3 == 0, factory);
  }

}
