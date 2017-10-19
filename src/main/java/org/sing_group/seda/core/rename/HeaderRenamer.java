package org.sing_group.seda.core.rename;

import org.sing_group.seda.datatype.SequencesGroup;

public interface HeaderRenamer {
  public SequencesGroup rename(SequencesGroup sequences);
}
