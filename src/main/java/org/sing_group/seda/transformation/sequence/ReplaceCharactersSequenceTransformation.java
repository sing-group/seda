package org.sing_group.seda.transformation.sequence;

import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;

public class ReplaceCharactersSequenceTransformation implements SequenceTransformation {
  private final SequenceBuilder builder;
  private final Map<String, String> replacements;

  public ReplaceCharactersSequenceTransformation(Map<String, String> replacements) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), replacements);
  }

  public ReplaceCharactersSequenceTransformation(DatatypeFactory factory, Map<String, String> replacements) {
    this.builder = factory::newSequence;
    this.replacements = replacements;
  }

  @Override
  public Sequence transform(Sequence sequence) {
    String chain = sequence.getChain();

    for (String key : replacements.keySet()) {
      chain = chain.replace(key, replacements.get(key));
    }

    return this.builder.of(sequence.getName(), sequence.getDescription(), chain, sequence.getProperties());
  }
}
