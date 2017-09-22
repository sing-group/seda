package org.sing_group.seda.datatype;

public enum SequenceTarget {
  HEADER("Header"), SEQUENCE("Sequence");

  private String description;

  SequenceTarget(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.description;
  }

  public boolean isSequence() {
    return this.equals(SEQUENCE);
  }
}