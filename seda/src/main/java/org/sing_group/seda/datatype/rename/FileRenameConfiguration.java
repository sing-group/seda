/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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