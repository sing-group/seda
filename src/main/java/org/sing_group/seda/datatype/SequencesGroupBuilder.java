package org.sing_group.seda.datatype;

import java.util.Map;

public interface SequencesGroupBuilder {
  public SequencesGroup of(String name, Map<String, Object> additionalInformation, Sequence[] sequences);
}
