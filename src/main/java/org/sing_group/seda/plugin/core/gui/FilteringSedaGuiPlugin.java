package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.filtering.FilteringConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class FilteringSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final FilteringConfigurationPanel panel;

  public FilteringSedaGuiPlugin() {
    this.panel = new FilteringConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Filtering";
  }

  @Override
  public Component getEditor() {
    return this.panel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.panel.getModel();
  }
}
