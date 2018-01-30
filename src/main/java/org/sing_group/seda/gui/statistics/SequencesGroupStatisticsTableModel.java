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
