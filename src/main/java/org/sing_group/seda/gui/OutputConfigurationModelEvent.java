package org.sing_group.seda.gui;

import static java.util.Objects.requireNonNull;

public class OutputConfigurationModelEvent {
  public static enum OutputConfigurationModelEventType {
    OUTPUT_DIRECTORY_CHANGED,
    SPLIT_INTO_SUBDIRECTORIES_CHANGED,
    SUBDIRECTORIES_SIZE_CHANGED;
  }

  private final OutputConfigurationModelEventType type;
  private final Object newValue;
  
  public static OutputConfigurationModelEvent of(OutputConfigurationModelEventType type, Object value) {
    return new OutputConfigurationModelEvent(type, value);
  }
  
  protected OutputConfigurationModelEvent(OutputConfigurationModelEventType type, Object value) {
    this.type = requireNonNull(type);
    this.newValue = requireNonNull(value);
  }
  
  public OutputConfigurationModelEventType getType() {
    return type;
  }

  public Object getNewValue() {
    return newValue;
  }
}
