package org.sing_group.seda.datatype.rename;

import java.util.List;

import org.sing_group.seda.util.OsUtils;

public class ReplaceCharacterConfiguration {

  private boolean replaceBlankSpaces;
  private boolean replaceSpecialCharacters;
  private String replacement;

  public ReplaceCharacterConfiguration() {
    this(false, false, "");
  }

  public ReplaceCharacterConfiguration(
    boolean replaceBlankSpaces, boolean replaceSpecialCharacters, String replacement
  ) {
    this.replaceBlankSpaces = replaceBlankSpaces;
    this.replaceSpecialCharacters = replaceSpecialCharacters;
    this.replacement = replacement;
  }

  public boolean isReplaceBlankSpaces() {
    return replaceBlankSpaces;
  }

  public String getReplacement() {
    return replacement;
  }

  public boolean isReplaceSpecialCharacters() {
    return replaceSpecialCharacters;
  }

  public static List<String> getSpecialCharacters() {
    return OsUtils.getInvalidWindowsFileCharacters();
  }
}