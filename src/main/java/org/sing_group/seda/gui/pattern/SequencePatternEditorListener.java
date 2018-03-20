package org.sing_group.seda.gui.pattern;

import java.util.EventListener;

import javax.swing.event.ChangeEvent;

public interface SequencePatternEditorListener extends EventListener {

  void patternEdited(PatternEditionEvent event);

  void patternAdded(ChangeEvent event);

  void patternRemoved(ChangeEvent event);
}