package org.sing_group.seda.transformation.sequence;

import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;

public class ChangePropertiesSequenceTransformation implements SequenceTransformation {
  private final SequenceBuilder builder;
  private Map<String, Object> newProperties;

  public ChangePropertiesSequenceTransformation(Map<String, Object> newProperties) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), newProperties);
  }

  public ChangePropertiesSequenceTransformation(DatatypeFactory factory, Map<String, Object> newProperties) {
    this.builder = factory::newSequence;
    this.newProperties = newProperties;
  }

  @Override
  public Sequence transform(Sequence sequence) {
    return this.builder.of(sequence.getName(), sequence.getDescription(), sequence.getChain(), newProperties);
  }
}
