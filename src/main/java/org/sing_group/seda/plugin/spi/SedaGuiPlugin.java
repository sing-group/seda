package org.sing_group.seda.plugin.spi;

import java.awt.Component;

public interface SedaGuiPlugin {
  public String getName();
  
  public Component getEditor();
  
  public TransformationProvider getTransformation();
}
