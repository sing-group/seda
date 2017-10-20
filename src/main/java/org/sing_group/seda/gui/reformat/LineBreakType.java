package org.sing_group.seda.gui.reformat;

public enum LineBreakType {
  WINDOWS("Windows", "\r\n"), 
  UNIX("Unix", "\n");

  private String description;
  private String lineBreak;

  LineBreakType(String description, String lineBreak) {
    this.description = description;
    this.lineBreak = lineBreak;
  }

  public String getLineBreak() {
    return lineBreak;
  }

  @Override
  public String toString() {
    return this.description;
  }

  public static LineBreakType defaultType() {
    return UNIX;
  }
}
