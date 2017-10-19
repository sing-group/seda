package org.sing_group.seda.core;

public class SedaContextEvent {

  public static enum SedaContextEventType {
    SELECTED_PATHS_CHANGED
  }

  private SedaContextEventType type;

  public SedaContextEvent(SedaContextEventType type) {
    this.type = type;
  }

  public SedaContextEventType getType() {
    return type;
  }
}
