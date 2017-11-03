package org.sing_group.seda.datatype.configuration;

import java.util.Map;

public class SequenceTranslationConfiguration {

  private Map<String, String> codonTable;
  private int[] frames;
  private boolean joinFrames;

  public SequenceTranslationConfiguration(Map<String, String> codonTable, int... frames) {
    this(codonTable, false, frames);
  }

  public SequenceTranslationConfiguration(Map<String, String> codonTable, boolean joinFrames, int... frames) {
    this.codonTable = codonTable;
    this.frames = frames;
    this.joinFrames = joinFrames;
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
}