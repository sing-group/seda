package org.sing_group.seda.gui;

import static java.awt.BorderLayout.*;
import static org.sing_group.seda.gui.AbstractVisualizationDialog.visualize;
import static org.sing_group.seda.gui.GuiUtils.bindCheckBox;
import static org.sing_group.seda.gui.GuiUtils.bindSpinner;
import static org.sing_group.seda.gui.GuiUtils.bindToggleButton;
import static org.sing_group.seda.gui.OutputConfigurationModel.DEFAULT_IN_MEMORY_PROCESSING;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.filechooser.FileChooserListener;
import org.sing_group.gc4s.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.filechooser.JFileChooserPanel.SelectionMode;
import org.sing_group.gc4s.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.gc4s.utilities.builder.JToggleButtonBuilder;

public class OutputConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String TOOLTIP_IN_MEMORY_UNSELECTED =
    "<html>"
    + "In memory processing: <b>disabled</b>. This means that all files will be procesed in hard disk, loading in memory "
    + "only the neccessary parts for each operation. <br/>This option is slower but allows processing big batchs of files "
    + "with thousands of sequences.</html>";
  private static final String TOOLTIP_IN_MEMORY_SELECTED =
    "<html>"
    + "In memory processing: <b>enabled</b>. This means that all files will be processed in memory. This option is faster."
    + "<br/>However, if you are trying to process a very high number of files, unselect this option to process them in "
    + "hard disk.</html>";

  private final OutputConfigurationModel model;
  private JButton btnOutputSettings;
  private JCheckBox chkSplitInSubdirectories;
  private JSpinner spnSubdirectoriesSize;
  private JToggleButton toggleInMemoryProcessing;
  private JFileChooser fileChooser;
  private JFileChooserPanel fileChooserPanel;
  private InputParametersPanel settingsPanel;

  private static final Icon ICON_WARNING = Icons.ICON_WARNING_COLOR_24;
  private static final Icon ICON_OK = Icons.ICON_OK_COLOR_24;
  public static final String TOOLTIP_WARNING = "Warning: the selected output directory contains some of the "
    + "selected files. This means the corresponding original files will be overwritten.";
  private JLabel outputDirectoryStatusLabel = new JLabel(ICON_OK);

  public OutputConfigurationPanel() {
    super(new BorderLayout(20, 0));

    this.model = new OutputConfigurationModel();

    this.add(getWestComponent(), WEST);
    this.add(getDirectoryComponent(), CENTER);
    this.add(getEastComponent(), EAST);

    this.getSettingsPanel();
  }

  private Component getWestComponent() {
    this.toggleInMemoryProcessing = JToggleButtonBuilder.newJToggleButton()
        .withLabel("In memory processing")
        .withSelectedIcon(Icons.ICON_RAM_16)
        .withUnselectedIcon(Icons.ICON_HARD_DISK_16)
        .setSelected(DEFAULT_IN_MEMORY_PROCESSING)
        .setEnabled(true)
        .withTooltip(selected -> selected ? TOOLTIP_IN_MEMORY_SELECTED : TOOLTIP_IN_MEMORY_UNSELECTED)
      .build();

    bindToggleButton(this.toggleInMemoryProcessing, this.model::setInMemoryProcessingEnabled);

    return this.toggleInMemoryProcessing;
  }


  private Component getDirectoryComponent() {
    this.fileChooser = new JFileChooser(this.model.getOutputDirectory().toFile());
    this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    this.fileChooser.setMultiSelectionEnabled(false);
    this.fileChooserPanel = JFileChooserPanelBuilder.createSaveJFileChooserPanel()
      .withFileChooser(this.fileChooser)
      .withLabel("Output directory: ")
      .withFileChooserSelectionMode(SelectionMode.DIRECTORIES)
      .build();
    this.fileChooserPanel.setSelectedFile(this.model.getOutputDirectory().toFile());

    this.fileChooserPanel.addFileChooserListener(
      new FileChooserListener() {

        @Override
        public void onFileChoosed(ChangeEvent event) {
          model.setOutputDirectory(fileChooserPanel.getSelectedFile().toPath());
        }
      }
    );

    JPanel fileChooserPanelContainer = new JPanel(new FlowLayout());
    fileChooserPanelContainer.add(this.fileChooserPanel);
    fileChooserPanelContainer.add(this.outputDirectoryStatusLabel);

    return fileChooserPanelContainer;
  }

  private Component getEastComponent() {
    this.btnOutputSettings = new JButton("Settings", Icons.ICON_EDIT_16);
    this.btnOutputSettings.addActionListener(event -> this.showOutputSettings());

    return this.btnOutputSettings;
  }

  private void showOutputSettings() {
    visualize(
      SwingUtilities.getWindowAncestor(this), new CenteredJPanel(this.getSettingsPanel()),
      "Output settings"
    );
  }

  private JPanel getSettingsPanel() {
    if (this.settingsPanel == null) {
      this.chkSplitInSubdirectories = new JCheckBox("Split in subdirectories", this.model.isSplitInSubdirectories());
      this.spnSubdirectoriesSize = new JSpinner(
        new SpinnerNumberModel(this.model.getSubdirectorySize(), 1, Integer.MAX_VALUE, 1)
      );

      bindCheckBox(this.chkSplitInSubdirectories, this.model::setSplitInSubdirectories);
      bindSpinner(this.spnSubdirectoriesSize, this.model::setSubdirectorySize);

      InputParameter[] settings = new InputParameter[2];
      settings[0] = new InputParameter("", this.chkSplitInSubdirectories, "Split in subdirectories");
      settings[1] = new InputParameter("Files by subdirectory: ", this.spnSubdirectoriesSize, "Files by subdirectory");

      this.settingsPanel = new InputParametersPanel(settings);

      this.model.addOutputConfigurationModelListener(
        event -> {
          switch (event.getType()) {
            case OUTPUT_DIRECTORY_CHANGED:
              updateOutputDirectory();
              break;
            case SPLIT_INTO_SUBDIRECTORIES_CHANGED:
              updateSplitIntoSubdirectories();
              break;
            case SUBDIRECTORIES_SIZE_CHANGED:
              updateSubdirectoriesSize();
              break;
            case IN_MEMORY_PROCESSING_ENABLED:
              updateInMemoryProcessing();
              break;
            default:
              break;
          }
        }
      );

      this.toggleSubdirectoriesOptions();
    }
    return this.settingsPanel;
  }

  public OutputConfigurationModel getModel() {
    return this.model;
  }

  private void updateOutputDirectory() {
    this.fileChooserPanel.setSelectedFile(this.model.getOutputDirectory().toFile());
  }

  private void updateSplitIntoSubdirectories() {
    this.chkSplitInSubdirectories.setSelected(this.model.isSplitInSubdirectories());
    this.toggleSubdirectoriesOptions();
  }

  private void updateInMemoryProcessing() {
    this.toggleInMemoryProcessing.setSelected(this.model.isInMemoryProcessingEnabled());
  }

  private void updateSubdirectoriesSize() {
    this.spnSubdirectoriesSize.setValue(this.model.getSubdirectorySize());
  }

  private void toggleSubdirectoriesOptions() {
    final boolean splitInSubdirectories = this.model.isSplitInSubdirectories();

    this.spnSubdirectoriesSize.setEnabled(splitInSubdirectories);
  }

  public void showOutputDirectoryWarning(boolean show) {
    this.outputDirectoryStatusLabel.setIcon(show ? ICON_WARNING : ICON_OK);
    this.outputDirectoryStatusLabel.setToolTipText(show ? TOOLTIP_WARNING : "");
  }
}
