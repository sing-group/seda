package org.sing_group.seda.gui.pattern.reallocate;

import java.awt.Component;

import org.sing_group.gc4s.ui.CenteredJPanel;

public class ReallocateReferenceSequencesPluginPanel extends CenteredJPanel {
  private static final long serialVersionUID = 1L;
  private ReallocateReferenceSequencesConfigurationPanel transformationPanel;
  private ReallocateReferenceSequencesTransformationProvider transformationProvider;

  public ReallocateReferenceSequencesPluginPanel() {
    this.init();
    this.transformationProvider = new ReallocateReferenceSequencesTransformationProvider(this.transformationPanel);
  }

  private void init() {
    this.add(this.getConfigurationPanel());
  }

  private Component getConfigurationPanel() {
    this.transformationPanel = new ReallocateReferenceSequencesConfigurationPanel();

    return this.transformationPanel;
  }

  public ReallocateReferenceSequencesTransformationProvider getReallocateSequencesTransformationProvider() {
    return this.transformationProvider;
  }
}
