package org.sing_group.seda.split;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class NumberOfSequencesSplitter extends AbstractSequencesGroupSplitter {
  private int numSequences;

  public NumberOfSequencesSplitter(int numSequences) {
    this(numSequences, false, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public NumberOfSequencesSplitter(int numSequences, boolean randomize, DatatypeFactory factory) {
    super(randomize, factory);
    this.numSequences = numSequences;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup group) {
    List<Sequence> input = getInputSequencesGroup(group);

    Spliterator<Sequence> splitIterator = input.spliterator();

    int splitCount = 1;
    List<SequencesGroup> toret = new LinkedList<>();

    while (true) {
      List<Sequence> chunk = new ArrayList<>(this.numSequences);

      for (int i = 0; i < this.numSequences && splitIterator.tryAdvance(chunk::add); i++) {}

      if (chunk.isEmpty()) {
        break;
      }

      toret.add(createGroup(group.getName() + "_" + (splitCount++), chunk));
    }
    return toret;
  }
}
