package org.sing_group.seda.gui;

@FunctionalInterface
public interface OutputConfigurationModelListener {
  
  public void configurationChanged(OutputConfigurationModelEvent event);
  
}
