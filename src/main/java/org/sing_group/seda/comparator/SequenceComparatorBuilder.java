package org.sing_group.seda.comparator;

import org.sing_group.seda.datatype.SequenceTarget;

public interface SequenceComparatorBuilder {
  public AbstractTargetedSequenceComparator of(SequenceTarget target);
}
