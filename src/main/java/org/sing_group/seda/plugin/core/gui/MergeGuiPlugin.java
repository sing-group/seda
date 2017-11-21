package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.merge.MergeConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class MergeGuiPlugin extends AbstractSedaGuiPlugin {
  private final MergeConfigurationPanel panel;

  public MergeGuiPlugin() {
    this.panel = new MergeConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Merge";
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
