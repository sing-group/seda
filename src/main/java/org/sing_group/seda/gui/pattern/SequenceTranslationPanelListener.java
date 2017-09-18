package org.sing_group.seda.gui.pattern;

import java.util.EventListener;

import javax.swing.event.ChangeEvent;

public interface SequenceTranslationPanelListener extends EventListener {
  void configurationChanged(ChangeEvent event);
}