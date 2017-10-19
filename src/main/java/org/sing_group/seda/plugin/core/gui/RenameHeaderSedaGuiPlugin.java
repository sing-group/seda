package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.gui.rename.RenameHeaderConfigurationModel;
import org.sing_group.seda.gui.rename.RenameTransformationConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class RenameHeaderSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final RenameTransformationConfigurationPanel panel;
  private final RenameHeaderConfigurationModel model;

  public RenameHeaderSedaGuiPlugin() {
    this.panel = new RenameTransformationConfigurationPanel();
    this.model = new RenameHeaderConfigurationModel(panel);
  }

  @Override
  public String getName() {
    return "Rename header";
  }

  @Override
  public Component getEditor() {
    return new CenteredJPanel(this.panel);
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.model;
  }

  @Override
  public void setSedaContext(SedaContext context) {
    super.setSedaContext(context);
    this.panel.setSedaContext(context);
  }
}
