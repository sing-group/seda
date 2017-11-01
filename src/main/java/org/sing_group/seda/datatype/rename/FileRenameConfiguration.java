package org.sing_group.seda.datatype.rename;

import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation.RenameMode;

public class FileRenameConfiguration {
  private final RenameMode mode;
  private final String delimiter;
  private final boolean stopAtFirstMatch;
  private ReplaceCharacterConfiguration replaceCharacterConfiguration;

  public FileRenameConfiguration(RenameMode mode) {
    this(mode, "", new ReplaceCharacterConfiguration());
  }

  public FileRenameConfiguration(
    RenameMode mode, String delimiter
  ) {
    this(mode, delimiter, new ReplaceCharacterConfiguration(), false);
  }

  public FileRenameConfiguration(
    RenameMode mode, String delimiter, ReplaceCharacterConfiguration replaceCharacterConfiguration
  ) {
    this(mode, delimiter, replaceCharacterConfiguration, false);
  }

  public FileRenameConfiguration(
    RenameMode mode, String delimiter, ReplaceCharacterConfiguration replaceCharacterConfiguration,
    boolean stopAtFirstMatch
  ) {
    this.mode = mode;
    this.delimiter = delimiter;
    this.replaceCharacterConfiguration = replaceCharacterConfiguration;
    this.stopAtFirstMatch = stopAtFirstMatch;
  }

  public RenameMode getMode() {
    return mode;
  }

  public String getDelimiter() {
    return delimiter;
  }

  public boolean isStopAtFirstMatch() {
    return stopAtFirstMatch;
  }

  public ReplaceCharacterConfiguration getReplaceCharacterConfiguration() {
    return replaceCharacterConfiguration;
  }
}