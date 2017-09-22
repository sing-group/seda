package org.sing_group.seda.comparator;

import java.util.Comparator;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;

public enum SequenceComparator {
  LENGTH("Length", LengthComparator::new),
  ALPHABPETICAL("Alphabetical", AlphabeticalComparator::new);
  
  private String description;
  private SequenceComparatorBuilder builder;

  SequenceComparator(String description, SequenceComparatorBuilder builder) {
    this.description = description;
    this.builder = builder;
  }
  
  @Override
  public String toString() {
    return description;
  }
  
  public Comparator<Sequence> getComparator(SequenceTarget target) {
    return this.builder.of(target);
  }
}
