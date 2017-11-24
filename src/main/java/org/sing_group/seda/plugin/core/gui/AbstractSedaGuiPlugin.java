package org.sing_group.seda.plugin.core.gui;

import java.util.Optional;

import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;

public abstract class AbstractSedaGuiPlugin implements SedaGuiPlugin {

  private SedaContext context;

  @Override
  public void setSedaContext(SedaContext context) {
    this.context = context;
  }

  protected SedaContext getSedaContext() {
    return context;
  }

  @Override
  public Optional<String> getProcessDatasetButtonTooltipMessage() {
    return Optional.empty();
  }
}
