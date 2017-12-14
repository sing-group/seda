package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.disambiguate.DisambiguateSequenceNamesConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class DisambiguateSequenceNamesSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final DisambiguateSequenceNamesConfigurationPanel panel;

  public DisambiguateSequenceNamesSedaGuiPlugin() {
    this.panel = new DisambiguateSequenceNamesConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Disambiguate sequence names";
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
