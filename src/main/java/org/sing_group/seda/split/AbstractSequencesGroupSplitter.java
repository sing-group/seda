package org.sing_group.seda.split;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public abstract class AbstractSequencesGroupSplitter implements SequencesGroupSplitter {
  private boolean randomize;
  private final BiFunction<String, List<Sequence>, SequencesGroup> builder;

  public AbstractSequencesGroupSplitter(boolean randomize, DatatypeFactory factory) {
    this.randomize = randomize;
    this.builder = factory::newSequencesGroup;
  }

  protected List<Sequence> getInputSequencesGroup(SequencesGroup group) {
    List<Sequence> input = group.getSequences().collect(Collectors.toList());
    if (randomize) {
      Collections.shuffle(input);
    }
    return input;
  }

  protected SequencesGroup createGroup(String name, List<Sequence> sequences) {
    return builder.apply(name, sequences);
  }
}
