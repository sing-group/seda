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
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.gui.AbstractVisualizationDialog.visualize;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;

import org.jdesktop.swingx.JXTable;
import org.sing_group.gc4s.event.PopupMenuAdapter;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.gc4s.utilities.ExtendedAbstractAction;
import org.sing_group.gc4s.visualization.table.ColumnSummaryTableCellRenderer;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.gui.CustomSwingWorker;
import org.sing_group.seda.gui.WorkingDialog;

public class SequencesGroupDatasetStatisticsTable extends JXTable {
  private static final long serialVersionUID = 1L;

  private ExtendedAbstractAction showSequenceInfo;
  private SequencesGroupDatasetStatisticsTableModel model;

  public SequencesGroupDatasetStatisticsTable(SequencesGroupDatasetStatisticsTableModel tm) {
    super(tm);

    this.model = tm;
    this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.setComponentPopupMenu(getPopupMenu());
    this.setTableHeader();
    this.packAll();
  }

  private JPopupMenu getPopupMenu() {
    JPopupMenu menu = new JPopupMenu();
    menu.add(getRemoveSelectedRowsAction());
    menu.addPopupMenuListener(
      new PopupMenuAdapter() {

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
          showSequenceInfo.setEnabled(getSelectedRowCount() > 0);
        }
      }
    );

    return menu;
  }

  private Action getRemoveSelectedRowsAction() {
    showSequenceInfo =
      new ExtendedAbstractAction(
        "Show file statistics", this::showStatistics
      );
    showSequenceInfo.setEnabled(false);

    return showSequenceInfo;
  }

  private void showStatistics() {
    if (this.getSelectedRowCount() > 0) {
      this.showSelectedRowStatistics();
    }
  }

  private void showSelectedRowStatistics() {
    int rowModel = this.convertRowIndexToModel(this.getSelectedRow());
    SequencesGroup sequencesGroup = this.model.getSequencesGroupAt(rowModel);
    createAndShowStatistics(sequencesGroup);
  }
  
  private void createAndShowStatistics(SequencesGroup sequencesGroup) {
    final JDialog dialog =
      new WorkingDialog(
        (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this),
        "Statistics", "Reading file and calculating statistics"
      );

    new CustomSwingWorker(
      () -> {
        SequencesGroupStatisticsTable table =
          new SequencesGroupStatisticsTable(
            new SequencesGroupStatisticsTableModel(sequencesGroup.getSequences().collect(toList()))
          );

        dialog.dispose();

        visualize(
          SwingUtilities.getWindowAncestor(this), new CenteredJPanel(new JScrollPane(table)),
          "Statistics for file: " + sequencesGroup.getName()
        );
      }
    ).execute();

    dialog.setVisible(true);
  }

  private void setTableHeader() {
    this.getTableHeader().setDefaultRenderer(
      new ColumnSummaryTableCellRenderer(
        this.getTableHeader().getDefaultRenderer(),
        asList(1, 2, 3),
        this.getModel()
      )
    );
  }
}
