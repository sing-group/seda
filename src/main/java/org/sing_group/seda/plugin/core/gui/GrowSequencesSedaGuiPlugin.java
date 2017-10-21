package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.grow.GrowSequencesConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class GrowSequencesSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final GrowSequencesConfigurationPanel panel;

  public GrowSequencesSedaGuiPlugin() {
    this.panel = new GrowSequencesConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Grow sequences";
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
