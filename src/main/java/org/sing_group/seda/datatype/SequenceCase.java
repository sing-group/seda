package org.sing_group.seda.datatype;

public enum SequenceCase {
  ORIGINAL("Original"), LOWERCASE("Lower case"), UPPERCASE("Upper case");

  private String description;

  SequenceCase(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.description;
  }

  public static SequenceCase defaultType() {
    return ORIGINAL;
  }
}