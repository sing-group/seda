package org.sing_group.seda.gui.statistics;

import static org.sing_group.seda.gui.AbstractVisualizationDialog.visualize;

import java.util.stream.Collectors;

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
    showSequenceInfo = new ExtendedAbstractAction(
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
    final JDialog dialog = new WorkingDialog(
      (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this),
      "Statistics", "Reading file and calculating statistics"
    );

    new CustomSwingWorker(
      () -> {
        SequencesGroupStatisticsTable table = new SequencesGroupStatisticsTable(
          new SequencesGroupStatisticsTableModel(sequencesGroup.getSequences().collect(Collectors.toList()))
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
}
