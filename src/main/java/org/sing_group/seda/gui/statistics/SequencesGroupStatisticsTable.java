package org.sing_group.seda.gui.statistics;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;

public class SequencesGroupStatisticsTable extends JXTable {
  private static final long serialVersionUID = 1L;

  public SequencesGroupStatisticsTable(SequencesGroupStatisticsTableModel tm) {
    super(tm);
    this.packAll();
    this.getColumn(1).setPreferredWidth(350);
    this.getColumn(1).setCellRenderer(
      new DefaultTableCellRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(
          JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
        ) {
          Component toret = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

          if (toret instanceof JComponent) {
            ((JComponent) toret).setToolTipText(value.toString());
          }
          return toret;
        }
      }
    );
  }
}
