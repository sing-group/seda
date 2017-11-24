package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;
import java.util.Optional;

import org.sing_group.seda.gui.ncbi.NcbiRenameConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class NcbiRenameGuiPlugin extends AbstractSedaGuiPlugin {
  private final NcbiRenameConfigurationPanel panel;

  public NcbiRenameGuiPlugin() {
    this.panel = new NcbiRenameConfigurationPanel();
  }

  @Override
  public String getName() {
    return "NCBI Rename";
  }

  @Override
  public Component getEditor() {
    return this.panel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.panel.getModel();
  }

  @Override
  public Optional<String> getProcessDatasetButtonTooltipMessage() {
    return this.panel.getModel().getGenerateButtonTooltipMessage();
  }
}
