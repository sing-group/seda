package org.sing_group.seda.transformation.sequence;

import org.sing_group.seda.bio.StopCodon;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;

public class RemoveStopCodonsSequenceTransformation implements SequenceTransformation {
  private final SequenceBuilder builder;
  
  public RemoveStopCodonsSequenceTransformation() {
    this.builder = DatatypeFactory.getDefaultDatatypeFactory()::newSequence;
  }

  public RemoveStopCodonsSequenceTransformation(DatatypeFactory factory) {
    this.builder = factory::newSequence;
  }

  @Override
  public Sequence transform(Sequence sequence) {
    String chain = sequence.getChain();
    
    for (StopCodon codon : StopCodon.values()) {
      final String codonSequence = codon.getChain();
      
      if (chain.endsWith(codonSequence)) {
        chain = chain.substring(0, chain.length() - codonSequence.length());
        break;
      }
    }
    
    return this.builder.of(sequence.getName(), sequence.getDescription(), chain, sequence.getProperties());
  }

}
