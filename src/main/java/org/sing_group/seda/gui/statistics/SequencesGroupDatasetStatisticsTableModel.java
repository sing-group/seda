package org.sing_group.seda.gui.statistics;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.datatype.statistics.SequencesGroupDatasetStatistics;
import org.sing_group.seda.datatype.statistics.SequencesGroupStatistics;

public class SequencesGroupDatasetStatisticsTableModel extends DefaultTableModel {
  private static final long serialVersionUID = 1L;
  private static final int COLUMN_COUNT = 4;

  private List<SequencesGroupStatistics> statistics;

  public SequencesGroupDatasetStatisticsTableModel(SequencesGroupDataset dataset) {
    this(new SequencesGroupDatasetStatistics(dataset));
  }

  public SequencesGroupDatasetStatisticsTableModel(SequencesGroupDatasetStatistics statistics) {
    this.statistics = statistics.getStatistics();
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
        return "Sequence count";
      case 2:
        return "Max. seq. length";
      case 3:
        return "Min. seq. length";
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
        return rowStatistics.getMaxSequenceLength();
      case 3:
        return rowStatistics.getMinSequenceLength();
    }
    throw new IllegalStateException();
  }

  public SequencesGroup getSequencesGroupAt(int rowModel) {
    return this.statistics.get(rowModel).getSequences();
  }
}
