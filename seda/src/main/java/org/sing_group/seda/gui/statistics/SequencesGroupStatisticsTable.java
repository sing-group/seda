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

import static java.util.Arrays.asList;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.sing_group.gc4s.visualization.table.ColumnSummaryTableCellRenderer;

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

    this.getTableHeader().setDefaultRenderer(
      new ColumnSummaryTableCellRenderer(
        this.getTableHeader().getDefaultRenderer(),
        asList(2),
        this.getModel()
      )
    );
  }
}
