package org.sing_group.seda.gui.ncbi;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CenteredJPanel;

public class NcbiRenameConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private FileRenameConfigurationPanel fileRenamePanel;
  private SequenceHeaderRenameConfigurationPanel sequenceHeaderRenamePanel;
  private ReplaceOptionsConfigurationPanel replaceOptionsConfigurationPanel;
  private NcbiTaxonomyConfigurationPanel ncbiTaxonomyConfigurationPanel;
  private NcbiRenameTransformationProvider transformationProvider;

  public NcbiRenameConfigurationPanel() {
    this.init();
    this.transformationProvider =
      new NcbiRenameTransformationProvider(
        this.fileRenamePanel, this.sequenceHeaderRenamePanel,
        this.replaceOptionsConfigurationPanel, this.ncbiTaxonomyConfigurationPanel
      );
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    mainPanel.add(getFileRenameConfigurationPanel());
    mainPanel.add(getSequenceHeaderRenameConfigurationPanel());
    mainPanel.add(getReplaceOptionsConfigurationPanel());
    mainPanel.add(getNcbiTaxonomyConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private Component getFileRenameConfigurationPanel() {
    fileRenamePanel = new FileRenameConfigurationPanel();
    addTitledBorder(fileRenamePanel, "File name");

    return fileRenamePanel;
  }

  private Component getSequenceHeaderRenameConfigurationPanel() {
    sequenceHeaderRenamePanel = new SequenceHeaderRenameConfigurationPanel();
    addTitledBorder(sequenceHeaderRenamePanel, "Sequence headers");

    return sequenceHeaderRenamePanel;
  }

  private Component getReplaceOptionsConfigurationPanel() {
    replaceOptionsConfigurationPanel = new ReplaceOptionsConfigurationPanel();
    addTitledBorder(replaceOptionsConfigurationPanel, "Configuration");

    return replaceOptionsConfigurationPanel;
  }

  private Component getNcbiTaxonomyConfigurationPanel() {
    ncbiTaxonomyConfigurationPanel = new NcbiTaxonomyConfigurationPanel();
    addTitledBorder(ncbiTaxonomyConfigurationPanel, "NCBI Taxonomy information");

    return ncbiTaxonomyConfigurationPanel;
  }

  private static void addTitledBorder(JComponent component, String title) {
    component.setBorder(BorderFactory.createTitledBorder(title));
  }

  public NcbiRenameTransformationProvider getModel() {
    return this.transformationProvider;
  }
}
