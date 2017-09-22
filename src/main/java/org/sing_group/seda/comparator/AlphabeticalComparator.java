package org.sing_group.seda.comparator;

import org.sing_group.seda.datatype.SequenceTarget;

public class AlphabeticalComparator extends AbstractTargetedSequenceComparator {

  public AlphabeticalComparator(SequenceTarget sequenceTarget) {
    super(sequenceTarget);
  }

  @Override
  protected int compare(String o1, String o2) {
    return o1.compareTo(o2);
  }
}
