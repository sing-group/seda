package org.sing_group.seda.comparator;

import java.util.Comparator;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;

public abstract class AbstractTargetedSequenceComparator implements Comparator<Sequence> {

  private final SequenceTarget sequenceTarget;

  public AbstractTargetedSequenceComparator(SequenceTarget sequenceTarget) {
    this.sequenceTarget = sequenceTarget;
  }

  @Override
  public int compare(Sequence o1, Sequence o2) {
    if (sequenceTarget.isSequence()) {
      return compare(o1.getChain(), o2.getChain());
    } else {
      return compare(o1.getHeader(), o2.getHeader());
    }
  }

  protected abstract int compare(String o1, String o2);
}
