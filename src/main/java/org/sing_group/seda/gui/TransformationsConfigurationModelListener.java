package org.sing_group.seda.gui;

@FunctionalInterface
public interface TransformationsConfigurationModelListener {
	
	public void configurationChanged(TransformationsConfigurationEvent event);
	
}
