package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.transformation.TransformationsConfigurationPanel;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class TransformationsSedaGuiPlugin implements SedaGuiPlugin {
  private final TransformationsConfigurationPanel panel;

  public TransformationsSedaGuiPlugin() {
    this.panel = new TransformationsConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Transformations";
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
