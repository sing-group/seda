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