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
package org.sing_group.seda.datatype.configuration;

import java.util.Map;

public class SequenceTranslationConfiguration {

  private Map<String, String> codonTable;
  private int[] frames;
  private boolean joinFrames;
  private boolean reverseComplement;

  public SequenceTranslationConfiguration(Map<String, String> codonTable, boolean reverseComplement, int... frames) {
    this(codonTable, reverseComplement, false, frames);
  }

  public SequenceTranslationConfiguration(
    Map<String, String> codonTable, boolean reverseComplement, boolean joinFrames, int... frames
  ) {
    this.codonTable = codonTable;
    this.frames = frames;
    this.joinFrames = joinFrames;
    this.reverseComplement = reverseComplement;
  }

  public int[] getFrames() {
    return frames;
  }

  public Map<String, String> getCodonTable() {
    return codonTable;
  }

  public boolean isJoinFrames() {
    return joinFrames;
  }

  public boolean isReverseComplement() {
    return reverseComplement;
  }
}
