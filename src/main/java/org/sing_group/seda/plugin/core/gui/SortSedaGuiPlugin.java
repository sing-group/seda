package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.sort.SortConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class SortSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final SortConfigurationPanel panel;

  public SortSedaGuiPlugin() {
    this.panel = new SortConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Sort";
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
