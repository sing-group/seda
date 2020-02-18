/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
      case SUFFIX:
        return Optional.of(Position.SUFFIX);
      default:
        return Optional.empty();
    }
  }
}