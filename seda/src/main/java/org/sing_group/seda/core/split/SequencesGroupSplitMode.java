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
package org.sing_group.seda.core.split;

public enum SequencesGroupSplitMode {
  FIXED_SEQUENCES_PER_FILE("Fixed number of sequences per file"),
  FIXED_FILES("Fixed number of files"),
  SEQUENCES_PER_FILE_AND_FILES("Fixed number of sequences per defined number of files");

  private String description;

  SequencesGroupSplitMode(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.description;
  }
}
