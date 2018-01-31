package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.concatenate.ConcatenateSequencesConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class ConcatenateSequencesSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final ConcatenateSequencesConfigurationPanel panel;

  public ConcatenateSequencesSedaGuiPlugin() {
    this.panel = new ConcatenateSequencesConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Concatenate sequences";
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
