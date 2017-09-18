package org.sing_group.seda.gui.redundant;

import java.util.EventListener;

import javax.swing.event.ChangeEvent;

public interface RemoveRedundantSequencesConfigurationPanelListener extends EventListener {
  void configurationChanged(ChangeEvent event);
}