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

public class DatasetProcessorConfiguration {

  private int groupSize;
  private boolean gzipOutput;

  public DatasetProcessorConfiguration(int groupSize, boolean gzipOutput) {
    this.groupSize = groupSize;
    this.gzipOutput = gzipOutput;
  }

  public int getGroupSize() {
    return groupSize;
  }

  public boolean isGzipOutput() {
    return gzipOutput;
  }
}
