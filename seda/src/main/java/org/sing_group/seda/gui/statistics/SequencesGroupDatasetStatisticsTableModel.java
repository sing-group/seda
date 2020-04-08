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
package org.sing_group.seda.gui.statistics;

import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.statistics.SequencesGroupDatasetStatistics;
import org.sing_group.seda.datatype.statistics.SequencesGroupStatistics;

public class SequencesGroupDatasetStatisticsTableModel extends DefaultTableModel {
  private static final long serialVersionUID = 1L;
  private static final int COLUMN_COUNT = 5;

  private SequencesGroupDatasetStatistics statistics;

  public SequencesGroupDatasetStatisticsTableModel(Map<Path, SequencesGroup> sequenceGroupMap) {
    this(new SequencesGroupDatasetStatistics(sequenceGroupMap));
  }

  public SequencesGroupDatasetStatisticsTableModel(SequencesGroupDatasetStatistics statistics) {
    this.statistics = statistics;
  }

  @Override
  public int getRowCount() {
    return this.statistics == null ? 0 : this.statistics.size();
  }

  @Override
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  @Override
  public String getColumnName(int columnIndex) {
    switch (columnIndex) {
      case 0:
        return "Name";
      case 1:
        StringBuilder sequences = new StringBuilder("Sequence count");
        if (this.statistics != null) {
          sequences
            .append(" (")
            .append(this.statistics.getTotalSecuencesCount())
            .append(")");
        }
        return sequences.toString();
      case 2:
        return "Min. seq. length";
      case 3:
        return "Max. seq. length";
      case 4:
        StringBuilder fileSize = new StringBuilder("File size");
        if (this.statistics != null) {
          fileSize
            .append(" (")
            .append(getStringSizeLengthFile(this.statistics.getTotalFileSize()))
            .append(")");
        }
        return fileSize.toString();
    }
    throw new IllegalStateException();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
      case 0:
        return String.class;
      case 1:
        return Integer.class;
      case 2:
        return Integer.class;
      case 3:
        return Integer.class;
      case 4:
        return String.class;
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    SequencesGroupStatistics rowStatistics = this.statistics.get(rowIndex);
    switch (columnIndex) {
      case 0:
        return rowStatistics.getName();
      case 1:
        return rowStatistics.getSequenceCount();
      case 2:
        return rowStatistics.getMinSequenceLength();
      case 3:
        return rowStatistics.getMaxSequenceLength();
      case 4:
        return getStringSizeLengthFile(rowStatistics.getFileSize());
    }
    throw new IllegalStateException();
  }

  public SequencesGroup getSequencesGroupAt(int rowModel) {
    return this.statistics.get(rowModel).getSequences();
  }

  private static String getStringSizeLengthFile(Long size) {
    DecimalFormat df = new DecimalFormat("0.00");

    float sizeKb = 1024.0f;
    float sizeMb = sizeKb * sizeKb;
    float sizeGb = sizeMb * sizeKb;
    float sizeTb = sizeGb * sizeKb;

    if (size < sizeMb)
      return df.format(size / sizeKb) + " Kb";
    else if (size < sizeGb)
      return df.format(size / sizeMb) + " Mb";
    else if (size < sizeTb)
      return df.format(size / sizeGb) + " Gb";

    return df.format(size / sizeTb) + " Tb";
  }
}
