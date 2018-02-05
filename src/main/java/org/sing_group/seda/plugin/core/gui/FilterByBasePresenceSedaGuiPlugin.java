package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.filtering.base.FilterByBasePresenceConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class FilterByBasePresenceSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final FilterByBasePresenceConfigurationPanel panel;

  public FilterByBasePresenceSedaGuiPlugin() {
    this.panel = new FilterByBasePresenceConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Base presence filtering";
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
