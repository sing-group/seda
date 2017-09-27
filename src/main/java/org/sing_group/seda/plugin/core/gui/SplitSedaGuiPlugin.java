package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.split.SplitConfigurationPanel;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class SplitSedaGuiPlugin implements SedaGuiPlugin {
  private final SplitConfigurationPanel panel;

  public SplitSedaGuiPlugin() {
    this.panel = new SplitConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Split";
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
