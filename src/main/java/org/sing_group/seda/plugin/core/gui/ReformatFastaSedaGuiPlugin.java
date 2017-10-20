package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class ReformatFastaSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final ReformatFastaConfigurationPanel panel;

  public ReformatFastaSedaGuiPlugin() {
    this.panel = new ReformatFastaConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Reformat file";
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
