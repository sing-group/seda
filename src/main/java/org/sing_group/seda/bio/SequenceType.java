package org.sing_group.seda.bio;

import org.sing_group.seda.util.StringUtils;

public enum SequenceType {
  NUCLEOTIDE, PROTEIN;

  @Override
  public String toString() {
    return StringUtils.capitalize(super.toString());
  }
}
