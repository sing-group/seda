package org.sing_group.seda.split;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public abstract class AbstractSequencesGroupSplitter implements SequencesGroupSplitter {

  private boolean randomize;

  public AbstractSequencesGroupSplitter(boolean randomize) {
    this.randomize = randomize;
  }

  protected List<Sequence> getInputSequencesGroup(SequencesGroup group) {
    List<Sequence> input = group.getSequences().collect(Collectors.toList());
    if (randomize) {
      Collections.shuffle(input);
    }
    return input;
  }
}
