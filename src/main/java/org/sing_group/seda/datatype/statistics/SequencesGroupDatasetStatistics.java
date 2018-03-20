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
package org.sing_group.seda.datatype.statistics;

import java.util.List;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.SequencesGroupDataset;

public class SequencesGroupDatasetStatistics {

  private List<SequencesGroupStatistics> statistics;

  public SequencesGroupDatasetStatistics(SequencesGroupDataset dataset) {
    this.statistics = dataset.getSequencesGroups().map(SequencesGroupStatistics::new).collect(Collectors.toList());
  }

  public List<SequencesGroupStatistics> getStatistics() {
    return statistics;
  }
}
