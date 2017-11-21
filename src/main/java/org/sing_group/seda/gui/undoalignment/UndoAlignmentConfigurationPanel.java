package org.sing_group.seda.gui.undoalignment;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class UndoAlignmentConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private UndoAlignmentTransformationProvider transformationProvider;
  private ReformatFastaConfigurationPanel reformatPanel;

  public UndoAlignmentConfigurationPanel() {
    this.init();
    this.transformationProvider = new UndoAlignmentTransformationProvider(this.reformatPanel.getModel());
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    return new CenteredJPanel(getReformatFastaConfigurationPanel());
  }

  private Component getReformatFastaConfigurationPanel() {
    this.reformatPanel = new ReformatFastaConfigurationPanel();
    this.reformatPanel.setBorder(BorderFactory.createTitledBorder("Reformat output files"));

    return this.reformatPanel;
  }

  public TransformationProvider getModel() {
    return this.transformationProvider;
  }
}
