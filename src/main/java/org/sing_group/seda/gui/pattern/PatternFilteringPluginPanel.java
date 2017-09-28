package org.sing_group.seda.gui.pattern;

import java.awt.Component;

import org.sing_group.gc4s.ui.CenteredJPanel;

public class PatternFilteringPluginPanel extends CenteredJPanel {
  private static final long serialVersionUID = 1L;
  private PatternFilteringConfigurationPanel transformationPanel;
  private PatternFilteringTransformationProvider transformationProvider;

  public PatternFilteringPluginPanel() {
    this.init();
    this.transformationProvider = new PatternFilteringTransformationProvider(this.transformationPanel);
  }

  private void init() {
    this.add(getConfigurationPanel());
  }

  private Component getConfigurationPanel() {
    this.transformationPanel = new PatternFilteringConfigurationPanel();

    return this.transformationPanel;
  }

  public PatternFilteringTransformationProvider getPatternFilteringTransformationProvider() {
    return this.transformationProvider;
  }
}
