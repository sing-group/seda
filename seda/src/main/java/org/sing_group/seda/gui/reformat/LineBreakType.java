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