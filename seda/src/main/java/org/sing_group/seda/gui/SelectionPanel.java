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

import static javax.swing.BorderFactory.createEmptyBorder;
import static org.sing_group.seda.gui.AbstractVisualizationDialog.visualize;
import static org.sing_group.seda.gui.GuiUtils.bindToggleButton;

import java.io.File;
import java.util.stream.Stream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

import org.sing_group.gc4s.dialog.JOptionPaneMessage;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.gc4s.utilities.builder.JToggleButtonBuilder;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.gui.statistics.SequencesGroupDatasetStatisticsTable;
import org.sing_group.seda.gui.statistics.SequencesGroupDatasetStatisticsTableModel;

public class SelectionPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String CHARSET_SUPPORT = "property.charset.support";
  public static final boolean DEFAULT_CHARSET_SUPPORT = false;

  private static final ImageIcon ICON_TAB_FILES = new ImageIcon(SedaPanel.class.getResource("image/tab-files.png"));
  private static final ImageIcon ICON_TAB_PREVIEW = new ImageIcon(SedaPanel.class.getResource("image/tab-preview.png"));

  private static final String STATISTICS_WARNING =
    "This operation may take a while, depending on the number and size of selected files. Dou you want to continue?";

  private static final String TOOLTIP_CHARSET_SUPPORT_SELECTED =
    "<html>Auto-detect encoding is <b>enabled</b>."
      + "<br/>SEDA will try to automatically identify the charset used by each FASTA file before processing them."
      + "<br/>Note that this option results in a slower processing and should be used only if you have special characters in your files or if SEDA fails to load them.</html>";
  private static final String TOOLTIP_CHARSET_SUPPORT_UNSELECTED =
    "<html>Auto-detect encoding is <b>disabled</b>."
      + "<br/>SEDA will use a fast loading algorithm that works with most of the FASTA files without special characters."
      + "<br/>Enable this option only if you have special characters in your files or if SEDA fails to load your files.</html>";

  private DatatypeFactory factory;

  private JTabbedPane tabs;
  private JLabel filesLabel;
  private JButton statistics;
  private boolean isCharsertSupportSelected = DEFAULT_CHARSET_SUPPORT;
  private JToggleButton charsetSupportButton;
  private PathSelectionPanel panelPathSelection;
  private FilesPreviewPanel panelFilePreview;
  private JOptionPaneMessage statisticsMessage;

  public SelectionPanel(DatatypeFactory factory) {
    this.factory = factory;
    this.init();
  }

  private void init() {
    this.statisticsMessage = new JOptionPaneMessage(STATISTICS_WARNING);
    this.panelPathSelection = new PathSelectionPanel();
    this.panelPathSelection.setBorder(createEmptyBorder(5, 5, 5, 5));
    this.panelFilePreview =
      new FilesPreviewPanel(
        this.panelPathSelection.getModel()
      );
    this.panelPathSelection.getModel().addPathSelectionModelListener(event -> {
      if (event.getType().isSelectedEvent())
        pathSelectionChanged();
    });
    this.panelFilePreview.setBorder(createEmptyBorder(5, 5, 5, 5));

    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(Box.createHorizontalGlue());
    this.add(getFilesLabel());
    this.add(Box.createHorizontalStrut(10));
    this.add(getEditSelectionButton());
    this.add(Box.createHorizontalStrut(10));
    this.add(getStatisticsButton());
    this.add(Box.createHorizontalStrut(10));
    this.add(getCharsetSupportButton());
    this.add(Box.createHorizontalGlue());
  }

  private JLabel getFilesLabel() {
    this.filesLabel = new JLabel();
    this.updateFilesLabelText();

    return this.filesLabel;
  }

  private void updateFilesLabelText() {
    StringBuilder sb = new StringBuilder();
    sb
      .append(panelPathSelection.getModel().countSelectedPaths())
      .append(" files selected (")
      .append(panelPathSelection.getModel().countAvailablePaths())
      .append(" available)");
    this.filesLabel.setText(sb.toString());
  }

  private JButton getStatisticsButton() {
    if (this.statistics == null) {
      this.statistics = new JButton("Statistics", Icons.ICON_TABLE_16);
      this.statistics.addActionListener(event -> this.showStatistics());
      this.statistics.setEnabled(false);
    }

    return statistics;
  }

  private JToggleButton getCharsetSupportButton() {
    if (this.charsetSupportButton == null) {
      this.charsetSupportButton =
        JToggleButtonBuilder.newJToggleButton()
          .withSelectedIcon(Icons.ICON_CHARACTER_MAP_16)
          .withUnselectedIcon(Icons.ICON_CHARACTER_MAP_16)
          .setSelected(DEFAULT_CHARSET_SUPPORT)
          .setEnabled(true)
          .withTooltip(selected -> selected ? TOOLTIP_CHARSET_SUPPORT_SELECTED : TOOLTIP_CHARSET_SUPPORT_UNSELECTED)
          .build();

       bindToggleButton(this.charsetSupportButton, (selected) -> {
         boolean newValue = selected;
         this.firePropertyChange(CHARSET_SUPPORT, this.isCharsertSupportSelected, newValue);
         this.isCharsertSupportSelected = newValue;
       });
    }

    return this.charsetSupportButton;
  }

  private void showStatistics() {
    if (this.statisticsMessage.shouldBeShown()) {
      int option =
        JOptionPane.showConfirmDialog(
          this, this.statisticsMessage.getMessage(), "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );
      if (option == JOptionPane.NO_OPTION) {
        return;
      }
    }

    this.createAndShowStatistics();
  }

  private void createAndShowStatistics() {
    final JDialog dialog =
      new WorkingDialog(
        (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this),
        "Statistics", "Reading files and calculating statistics"
      );

    new CustomSwingWorker(
      () -> {
        try (final Stream<String> sequenceFiles = this.panelPathSelection.getModel().getSelectedPaths()) {
          final SequencesGroup[] sequences =
            sequenceFiles
              .map(s -> new File(s).toPath())
              .map(this.factory::newSequencesGroup)
              .toArray(SequencesGroup[]::new);

          SequencesGroupDatasetStatisticsTable table =
            new SequencesGroupDatasetStatisticsTable(
              new SequencesGroupDatasetStatisticsTableModel(this.factory.newSequencesGroupDataset(sequences))
            );

          dialog.dispose();

          visualize(
            SwingUtilities.getWindowAncestor(this), new CenteredJPanel(new JScrollPane(table)),
            "Selected files statistics"
          );
        }
      }
    ).execute();

    dialog.setVisible(true);
  }

  private JButton getEditSelectionButton() {
    JButton editSelectionBtn = new JButton("Edit selection", Icons.ICON_EDIT_16);
    editSelectionBtn.addActionListener(event -> this.editFilesSelection());

    return editSelectionBtn;
  }

  private void editFilesSelection() {
    visualize(
      SwingUtilities.getWindowAncestor(this), new CenteredJPanel(getFilesSelectionPanel()), "Edit file selection"
    );

    this.updateFilesLabelText();
  }

  private JTabbedPane getFilesSelectionPanel() {
    if (this.tabs == null) {
      this.tabs = new JTabbedPane();
      tabs.setUI(new MetalTabbedPaneUI() {
        @Override
        protected int calculateTabHeight(
          int tabPlacement, int tabIndex,
          int fontHeight
        ) {
          return 32;
        }
      });
      this.tabs.addTab("File selection", ICON_TAB_FILES, this.panelPathSelection);
      this.tabs.addTab("File preview", ICON_TAB_PREVIEW, this.panelFilePreview);
    }
    return this.tabs;
  }

  private void pathSelectionChanged() {
    this.getStatisticsButton().setEnabled(getModel().countSelectedPaths() > 0);
  }

  public PathSelectionModel getModel() {
    return this.panelPathSelection.getModel();
  }

  public void setDatasetFactory(DatatypeFactory datatypeFactory) {
    this.factory = datatypeFactory;
  }

  public boolean isCharsetSupportEnabled() {
    return this.charsetSupportButton.isSelected();
  }
}
