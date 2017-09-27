package org.sing_group.seda.split;

import java.util.List;

import org.sing_group.seda.datatype.SequencesGroup;

public interface SequencesGroupSplitter {
  public List<SequencesGroup> split(SequencesGroup group);
}
