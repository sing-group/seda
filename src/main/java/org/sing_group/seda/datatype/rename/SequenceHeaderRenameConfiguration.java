package org.sing_group.seda.datatype.rename;

import java.util.Optional;

import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer.Position;
import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation.RenameMode;

public class SequenceHeaderRenameConfiguration {
  private final RenameMode renameMode;
  private final String delimiter;
  private final boolean addIndex;
  private final String indexDelimiter;
  private ReplaceCharacterConfiguration replaceCharacterConfiguration;

  public SequenceHeaderRenameConfiguration() {
    this(RenameMode.NONE, "", false, "", new ReplaceCharacterConfiguration());
  }

  public SequenceHeaderRenameConfiguration(
    RenameMode renameMode, String delimiter, boolean addIndex, String indexDelimiter,
    ReplaceCharacterConfiguration replaceCharacterConfiguration
  ) {
    this.renameMode = renameMode;
    this.delimiter = delimiter;
    this.addIndex = addIndex;
    this.indexDelimiter = indexDelimiter;
    this.replaceCharacterConfiguration = replaceCharacterConfiguration;
  }

  public RenameMode getRenameMode() {
    return renameMode;
  }

  public String getDelimiter() {
    return delimiter;
  }

  public boolean isAddIndex() {
    return addIndex;
  }

  public String getIndexDelimiter() {
    return indexDelimiter;
  }

  public ReplaceCharacterConfiguration getReplaceCharacterConfiguration() {
    return replaceCharacterConfiguration;
  }

  public Optional<AddStringHeaderRenamer.Position> getPosition() {
    switch (this.renameMode) {
      case OVERRIDE:
        return Optional.of(Position.OVERRIDE);
      case PREFIX:
        return Optional.of(Position.PREFIX);
      case SUFIX:
        return Optional.of(Position.SUFFIX);
      default:
        return Optional.empty();
    }
  }
}