package org.sing_group.seda.core;

import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.core.SedaContextEvent.SedaContextEventType;

public class SedaContext {

  private final List<SedaContextListener> listeners;
  private List<String> selectedPaths;

  public SedaContext() {
    this.listeners = new LinkedList<>();
  }

  public void setSelectedPaths(List<String> selectedPaths) {
    this.selectedPaths = selectedPaths;
    this.fireSelectedPathsChanged();
  }

  private void fireSelectedPathsChanged() {
    SedaContextEvent event = new SedaContextEvent(SedaContextEventType.SELECTED_PATHS_CHANGED);
    this.listeners.forEach(listener -> listener.contextChanged(event));
  }

  public List<String> getSelectedPaths() {
    return selectedPaths;
  }

  public void addSedaContextListener(SedaContextListener listener) {
    if (!this.listeners.contains(listener))
      this.listeners.add(listener);
  }

  public boolean removeSedaContextListener(SedaContextListener listener) {
    return this.listeners.remove(listener);
  }

  public boolean containsSedaContextListener(SedaContextListener listener) {
    return this.listeners.contains(listener);
  }
}
