package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.pattern.PatternFilteringPluginPanel;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class PatternFilteringSedaGuiPlugin implements SedaGuiPlugin {
  private final PatternFilteringPluginPanel panel;

  public PatternFilteringSedaGuiPlugin() {
    this.panel = new PatternFilteringPluginPanel();
  }

  @Override
  public String getName() {
    return "Pattern filtering";
  }

  @Override
  public Component getEditor() {
    return this.panel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.panel.getPatternFilteringTransformationProvider();
  }
}
