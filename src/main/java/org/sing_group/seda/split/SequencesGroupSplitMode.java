package org.sing_group.seda.split;

public enum SequencesGroupSplitMode {
  FIXED_SEQUENCES_PER_FILE("Fixed number of sequences per file"),
  FIXED_FILES("Fixed number of files"),
  SEQUENCES_PER_FILE_AND_FILES("Fixed number of sequences per defined number of files");

  private String description;

  SequencesGroupSplitMode(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.description;
  }
}
