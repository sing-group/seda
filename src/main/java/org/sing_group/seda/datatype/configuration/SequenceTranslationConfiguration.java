package org.sing_group.seda.datatype.configuration;

import java.util.Map;

public class SequenceTranslationConfiguration {

  private Map<String, String> codonTable;
  private int[] frames;
  private boolean joinFrames;
  private boolean reverseComplement;

  public SequenceTranslationConfiguration(Map<String, String> codonTable, boolean reverseComplement, int... frames) {
    this(codonTable, reverseComplement, false, frames);
  }

  public SequenceTranslationConfiguration(
    Map<String, String> codonTable, boolean reverseComplement, boolean joinFrames, int... frames
  ) {
    this.codonTable = codonTable;
    this.frames = frames;
    this.joinFrames = joinFrames;
    this.reverseComplement = reverseComplement;
  }

  public int[] getFrames() {
    return frames;
  }

  public Map<String, String> getCodonTable() {
    return codonTable;
  }

  public boolean isJoinFrames() {
    return joinFrames;
  }

  public boolean isReverseComplement() {
    return reverseComplement;
  }
}
