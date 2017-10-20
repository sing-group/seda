package org.sing_group.seda.transformation.sequencesgroup;

import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.transformation.TransformationException;

public class ChangePropertiesSequencesGroupTransformation implements SequencesGroupTransformation {
  private final SequencesGroupBuilder builder;
  private Map<String, Object> newProperties;

  public ChangePropertiesSequencesGroupTransformation(Map<String, Object> newProperties) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), newProperties);
  }

  public ChangePropertiesSequencesGroupTransformation(DatatypeFactory factory, Map<String, Object> newProperties) {
    this.builder = factory::newSequencesGroup;
    this.newProperties = newProperties;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    return builder.of(
      sequencesGroup.getName(), this.newProperties, sequencesGroup.getSequences().toArray(size -> new Sequence[size])
    );
  }
}
