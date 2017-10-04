package org.sing_group.seda.datatype.statistics;

import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class SequencesGroupStatistics {

  private String name;
  private int sequenceCount;
  private IntSummaryStatistics lengthSummary;

  public SequencesGroupStatistics(SequencesGroup sequences) {
    this.name = sequences.getName();
    this.sequenceCount = sequences.getSequenceCount();
    this.lengthSummary = sequences.getSequences().collect(Collectors.summarizingInt(Sequence::getLength));
  }

  public String getName() {
    return name;
  }

  public int getSequenceCount() {
    return sequenceCount;
  }

  public int getMaxSequenceLength() {
    return this.lengthSummary.getMax();
  }

  public int getMinSequenceLength() {
    return this.lengthSummary.getMin();
  }
}
