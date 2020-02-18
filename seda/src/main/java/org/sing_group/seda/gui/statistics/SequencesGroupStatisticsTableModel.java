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

import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.sing_group.seda.datatype.Sequence;

public class SequencesGroupStatisticsTableModel extends DefaultTableModel {
  private static final long serialVersionUID = 1L;

  private static final int COLUMN_COUNT = 3;

  private List<Sequence> sequences;

  public SequencesGroupStatisticsTableModel(List<Sequence> sequences) {
    this.sequences = sequences;
  }

  @Override
  public int getRowCount() {
    return this.sequences == null ? 0 : this.sequences.size();
  }

  @Override
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  @Override
  public String getColumnName(int columnIndex) {
    switch (columnIndex) {
      case 0:
        return "Identifier";
      case 1:
        return "Description";
      case 2:
        return "Length";
    }
    throw new IllegalStateException();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
      case 0:
        return String.class;
      case 1:
        return String.class;
      case 2:
        return Integer.class;
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        return this.sequences.get(rowIndex).getName();
      case 1:
        return this.sequences.get(rowIndex).getDescription();
      case 2:
        return this.sequences.get(rowIndex).getLength();
    }
    throw new IllegalStateException();
  }
}
