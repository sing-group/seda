package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.consensus.GenerateConsensusSequenceConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class GenerateConsensusSequencesSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final GenerateConsensusSequenceConfigurationPanel panel;

  public GenerateConsensusSequencesSedaGuiPlugin() {
    this.panel = new GenerateConsensusSequenceConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Consensus sequence";
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
