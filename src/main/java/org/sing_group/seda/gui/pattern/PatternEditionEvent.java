package org.sing_group.seda.gui.pattern;

import javax.swing.event.ChangeEvent;

public class PatternEditionEvent extends ChangeEvent {
  private static final long serialVersionUID = 1L;

  public enum PatternEditionType {
    MODE, REGEX, REQUIRED_MATCHES, CASE_SENSITIVE
  }

  private PatternEditionType type;
  
  public PatternEditionEvent(Object source, PatternEditionType type) {
    super(source);
    this.type = type;
  }
  
  public PatternEditionType getType() {
    return type;
  }
}
