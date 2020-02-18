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
package org.sing_group.seda.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.BorderFactory.createTitledBorder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FilesPreviewPanel extends JPanel
  implements PathSelectionModelListener, ListSelectionListener {
  private static final long serialVersionUID = 1L;

  private static final Border DEFAULT_BORDER = createTitledBorder("File preview");

  private PathSelectionModel pathSelectionModel;
  private FilePreviewPanel filePreviewPanel;
  private FilesPreviewTableModel filesPreviewTableModel;
  private JTable filesPreviewTable;
  private JButton previewNextFileBtn;
  private JButton previewPreviousFileBtn;
  private JButton unselectFileBtn;

  public FilesPreviewPanel(PathSelectionModel model) {
    this.pathSelectionModel = model;
    this.pathSelectionModel.addPathSelectionModelListener(this);
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getCenterComponent(), CENTER);
    this.add(getSouthComponent(), SOUTH);
  }

  private Component getCenterComponent() {
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(getFilesPreviewTable(), CENTER);

    return centerPanel;
  }

  private Component getFilesPreviewTable() {
    this.filesPreviewTableModel = new FilesPreviewTableModel();
    this.filesPreviewTable = new JTable(this.filesPreviewTableModel);
    this.filesPreviewTable.setRowSelectionAllowed(true);
    this.filesPreviewTable.setColumnSelectionAllowed(false);
    this.filesPreviewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.filesPreviewTable.getSelectionModel().addListSelectionListener(this);
    this.filesPreviewTable.setDefaultRenderer(Object.class, new FilesPreviewTableRenderer());

    return new JScrollPane(this.filesPreviewTable);
  }

  private static class FilesPreviewTableRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
    ) {
      Component component =
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      if(component instanceof JComponent) {
        ((JComponent) component).setToolTipText(value.toString());
      }

      return component;
    }
  }

  private Component getSouthComponent() {
    JPanel southPanel = new JPanel();
    southPanel.setLayout(new BorderLayout());
    southPanel.add(getFileActions(), NORTH);
    southPanel.add(getFilePreviewPanel(), CENTER);

    return southPanel;
  }

  private Component getFileActions() {
    JPanel buttonsPanel = new JPanel(new FlowLayout());
    buttonsPanel.add(Box.createHorizontalGlue());
    buttonsPanel.add(getPreviewPreviousButton());
    buttonsPanel.add(getPreviewNextButton());
    buttonsPanel.add(Box.createHorizontalStrut(10));
    buttonsPanel.add(getUnselectButton());
    buttonsPanel.add(Box.createHorizontalGlue());

    return buttonsPanel;
  }

  private Component getPreviewPreviousButton() {
    this.previewPreviousFileBtn = new JButton(
      new AbstractAction("Previous file") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
          previewPreviousFile();
        }
      });
    this.previewPreviousFileBtn.setEnabled(false);

    return this.previewPreviousFileBtn;
  }

  private Component getPreviewNextButton() {
    this.previewNextFileBtn = new JButton(
      new AbstractAction("Next file") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
          previewNextFile();
        }
      });
    this.previewNextFileBtn.setEnabled(false);

    return this.previewNextFileBtn;
  }

  private Component getUnselectButton() {
    this.unselectFileBtn = new JButton(new AbstractAction("Unselect file") {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        unselectFile();
      }
    });
    this.unselectFileBtn.setEnabled(false);
    return this.unselectFileBtn;
  }

  protected void previewPreviousFile() {
    int selectedRow = this.filesPreviewTable.getSelectedRow();
    this.filesPreviewTable.getSelectionModel()
    .setSelectionInterval(selectedRow + -1, selectedRow + -1);
  }

  protected void previewNextFile() {
    int selectedRow = this.filesPreviewTable.getSelectedRow();
    this.filesPreviewTable.getSelectionModel()
      .setSelectionInterval(selectedRow + 1, selectedRow + 1);
  }

  protected void unselectFile() {
    int selectedRowModel = getSelectedRowModel();
    String fileToUnselect = this.filesPreviewTableModel
      .getPath(selectedRowModel);
    this.pathSelectionModel.removeSelectedPath(fileToUnselect);
    this.setSelectedRow(selectedRowModel);
  }

  private void setSelectedRow(int row) {
    if (row >= this.filesPreviewTableModel.getRowCount()) {
      row = this.filesPreviewTableModel.getRowCount() - 1;
    }
    if(this.filesPreviewTableModel.getRowCount() != 0) {
      this.filesPreviewTable.setRowSelectionInterval(row, row);
    }
  }

  private Component getFilePreviewPanel() {
    this.filePreviewPanel = new FilePreviewPanel();
    this.filePreviewPanel.setBorder(DEFAULT_BORDER);

    return this.filePreviewPanel;
  }

  @Override
  public void modelChanged(PathSelectionModelEvent event) {
    switch (event.getType()) {
    case CLEAR_SELECTED:
      this.filesPreviewTableModel.clearPaths();
      break;
    case ADD_SELECTED:
      if (event.getPath().isPresent()) {
        this.filesPreviewTableModel.addPath(event.getPath().get());
      }
      break;
    case REMOVE_SELECTED:
      if (event.getPath().isPresent()) {
        this.filesPreviewTableModel.removePath(event.getPath().get());
      }
      break;

    default:
      break;
    }
  }

   private static class FilesPreviewTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;

    private List<String> previewPaths = new ArrayList<>();;

    @Override
    public int getRowCount() {
      return previewPaths == null ? 0 : previewPaths.size();
    }

    @Override
    public int getColumnCount() {
      return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
      switch (columnIndex) {
      case 0:
        return "File";
      }
      throw new IllegalStateException();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      switch (columnIndex) {
      case 0:
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
      switch (columnIndex) {
      case 0:
        return this.previewPaths.get(rowIndex);
      }
      throw new IllegalStateException();
    }

    public void addPath(String path) {
      this.previewPaths.add(path);
      this.fireTableDataChanged();
    }

    public void removePath(String path) {
      this.previewPaths.remove(path);
      this.fireTableDataChanged();
    }

    public void clearPaths() {
      this.previewPaths.clear();
      this.fireTableDataChanged();
    }

    public String getPath(int row) {
      return this.previewPaths.get(row);
    }

    public boolean isFirstRow(int row) {
      return row == 0;
    }

    public boolean isLastRow(int row) {
      return this.previewPaths.size() - 1 == row;
    }
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      int selectedRowModel = getSelectedRowModel();
      if (selectedRowModel != -1) {
        String previewPath = this.filesPreviewTableModel
          .getPath(selectedRowModel);
        this.filePreviewPanel.setBorder(titledBorder(previewPath));
        this.filePreviewPanel.previewPath(previewPath);
      } else {
        this.filePreviewPanel.setBorder(DEFAULT_BORDER);
        this.filePreviewPanel.clearPreviewPath();
      }
    }
    checkButtonsState();
  }

  private Border titledBorder(String selectedPath) {
    return createTitledBorder(
      "File preview (" + new File(selectedPath).getName() + ")");
  }

  private void checkButtonsState() {
    int selectedRow = this.getSelectedRowModel();
    this.previewNextFileBtn.setEnabled(selectedRow != -1
      && !this.filesPreviewTableModel.isLastRow(selectedRow));
    this.previewPreviousFileBtn.setEnabled(selectedRow != -1
      && !this.filesPreviewTableModel.isFirstRow(selectedRow));
    this.unselectFileBtn.setEnabled(selectedRow != -1);
  }

  private int getSelectedRowModel() {
    return this.filesPreviewTable
      .convertRowIndexToModel(this.filesPreviewTable.getSelectedRow());
  }
}
