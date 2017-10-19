package org.sing_group.seda.plugin.spi;

import java.awt.Component;

import org.sing_group.seda.core.SedaContext;

public interface SedaGuiPlugin {
  public String getName();

  public Component getEditor();

  public TransformationProvider getTransformation();

  public void setSedaContext(SedaContext context);
}
