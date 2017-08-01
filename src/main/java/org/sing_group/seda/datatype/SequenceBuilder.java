package org.sing_group.seda.datatype;

import java.util.Map;

public interface SequenceBuilder {
  public Sequence of(String name, String description, String chain, Map<String, Object> additionalInformation);
}
