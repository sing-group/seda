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
package org.sing_group.seda.datatype;

import static java.util.Arrays.stream;

import java.io.Serializable;
import java.util.stream.Stream;

public class DefaultSequencesGroupDataset implements SequencesGroupDataset, Serializable {
  private static final long serialVersionUID = 1L;

  private final SequencesGroup[] sequencesGroups;

  public DefaultSequencesGroupDataset(SequencesGroup... sequencesGroups) {
    this.sequencesGroups = sequencesGroups;
  }

  @Override
  public Stream<SequencesGroup> getSequencesGroups() {
    return stream(this.sequencesGroups);
  }

  @Override
  public int getSequencesGroupsCount() {
    return this.sequencesGroups.length;
  }
}
