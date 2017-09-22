package org.sing_group.seda.comparator;

import org.sing_group.seda.datatype.SequenceTarget;

public class LengthComparator extends AbstractTargetedSequenceComparator {

  public LengthComparator(SequenceTarget sequenceTarget) {
    super(sequenceTarget);
  }

  @Override
  protected int compare(String o1, String o2) {
    return new Integer(o1.length()).compareTo(o2.length());
  }
}
