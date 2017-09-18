package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.redundant.RemoveRedundantSequencesConfigurationPanel;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class RemoveRedundantSequencesSedaGuiPlugin implements SedaGuiPlugin {
  private final RemoveRedundantSequencesConfigurationPanel panel;

  public RemoveRedundantSequencesSedaGuiPlugin() {
    this.panel = new RemoveRedundantSequencesConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Remove redundant sequences";
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
