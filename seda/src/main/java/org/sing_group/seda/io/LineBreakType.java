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
package org.sing_group.seda.io;

import static org.sing_group.seda.io.IOUtils.createNumberedLineReader;

import java.io.IOException;
import java.nio.file.Path;

public enum LineBreakType {
  WINDOWS("Windows", "\r\n"), UNIX("Unix", "\n");

  private String description;
  private String lineBreak;

  LineBreakType(String description, String lineBreak) {
    this.description = description;
    this.lineBreak = lineBreak;
  }

  public String getLineBreak() {
    return lineBreak;
  }

  public boolean isDefault() {
    return this == defaultType();
  }

  @Override
  public String toString() {
    return this.description;
  }

  public static LineBreakType defaultType() {
    return UNIX;
  }

  public static LineBreakType forString(String lineBreak) {
    if (lineBreak.equals(WINDOWS.getLineBreak())) {
      return WINDOWS;
    } else {
      return UNIX;
    }
  }

  public static LineBreakType forFile(Path file) {
    try (NumberedLineReader reader = createNumberedLineReader(file)) {
      NumberedLineReader.Line nline = reader.readLine();
      if (nline != null) {
        return forString(nline.getLineEnding());
      } else {
        throw new RuntimeException("Error reading file: " + file.toString());
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading file: " + file.toString());
    }
  }
}
