package org.sing_group.seda.gui.redundant;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.filechooser.JFileChooserPanel.SelectionMode;
import org.sing_group.gc4s.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.Mode;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;

public class RemoveRedundantSequencesConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public enum PatternType { NUCLEOTIDE, PROTEIN };

  private RemoveRedundantSequencesTransformationProvider transformationProvider;
  private final JCheckBox removeContainedSequencesCb = new JCheckBox("Remove also subsequences", true);
  private final JCheckBox mergeHeadersCb = new JCheckBox("Merge headers", true);
  private final JCheckBox saveMergedHeadersCb = new JCheckBox("Save merged headers into a file", false);
  private final JFileChooserPanel mergedHeadersFileChooser = JFileChooserPanelBuilder
    .createSaveJFileChooserPanel().withLabel("Merge list directory: ")
    .withFileChooserSelectionMode(SelectionMode.DIRECTORIES).build();

  public RemoveRedundantSequencesConfigurationPanel() {
    this.init();
    this.transformationProvider = new RemoveRedundantSequencesTransformationProvider(this);
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel(new GridLayout(4, 1));
    mainPanel.add(removeContainedSequencesCb);
    mainPanel.add(mergeHeadersCb);
    mainPanel.add(saveMergedHeadersCb);
    mainPanel.add(mergedHeadersFileChooser);
    mergedHeadersFileChooser.getBrowseAction().setEnabled(false);

    addListeners();

    return new CenteredJPanel(mainPanel);
  }

  private void addListeners() {
    removeContainedSequencesCb.addItemListener(this::configurationChanged);
    mergeHeadersCb.addItemListener(this::configurationChanged);
    saveMergedHeadersCb.addItemListener(this::configurationChanged);
    mergedHeadersFileChooser.addFileChooserListener(this::selectedFileChanged);
  }

  private void configurationChanged(ItemEvent event) {
      this.notifyConfigurationChanged();
      this.mergedHeadersFileChooser.getBrowseAction().setEnabled(this.saveMergedHeadersCb.isSelected());
  }

  private void selectedFileChanged(ChangeEvent event) {
    this.notifyConfigurationChanged();
  }

  private void notifyConfigurationChanged() {
    ChangeEvent event = new ChangeEvent(this);
    for (
      RemoveRedundantSequencesConfigurationPanelListener l : this
        .getRemoveRedundantSequencesConfigurationPanelListeners()
    ) {
      l.configurationChanged(event);
    }
  }

  public RemoveRedundantSequencesTransformationProvider getPatternFilteringTransformationProvider() {
    return this.transformationProvider;
  }

  public synchronized void addRemoveRedundantSequencesConfigurationPanelListener(
    RemoveRedundantSequencesConfigurationPanelListener l
  ) {
    this.listenerList.add(RemoveRedundantSequencesConfigurationPanelListener.class, l);
  }

  public synchronized RemoveRedundantSequencesConfigurationPanelListener[] getRemoveRedundantSequencesConfigurationPanelListeners() {
    return this.listenerList.getListeners(RemoveRedundantSequencesConfigurationPanelListener.class);
  }

  public boolean isValidUserSelection() {
    return !this.saveMergedHeadersCb.isSelected() || this.getSaveMergedHeadersFile() != null;
  }

  public TransformationProvider getModel() {
    return this.transformationProvider;
  }

  public RemoveRedundantSequencesTransformationConfiguration getConfiguration() {
    if (this.saveMergedHeadersCb.isSelected()) {
      return new RemoveRedundantSequencesTransformationConfiguration(
        isRemoveContainedSequences(), isMergeHeaders(), getSaveMergedHeadersFile()
      );
    } else {
      return new RemoveRedundantSequencesTransformationConfiguration(isRemoveContainedSequences(), isMergeHeaders());
    }
  }

  private Mode isRemoveContainedSequences() {
    if (this.removeContainedSequencesCb.isSelected()) {
      return Mode.CONTAINED_SEQUENCES;
    } else {
      return Mode.EXACT_DUPLICATES;
    }
  }

  private boolean isMergeHeaders() {
    return this.mergeHeadersCb.isSelected();
  }

  private File getSaveMergedHeadersFile() {
    return this.mergedHeadersFileChooser.getSelectedFile();
  }
}
