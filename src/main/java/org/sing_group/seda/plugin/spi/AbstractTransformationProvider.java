package org.sing_group.seda.plugin.spi;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractTransformationProvider implements TransformationProvider {
  private final List<TransformationChangeListener> listeners;
  
  public AbstractTransformationProvider() {
    this.listeners = new CopyOnWriteArrayList<>();
  }
  
  @Override
  public boolean addTransformationChangeListener(TransformationChangeListener listener) {
    if (this.listeners.contains(listener)) {
      return false;
    } else {
      return this.listeners.add(listener);
    }
  }

  @Override
  public boolean removeTranformationChangeListener(TransformationChangeListener listener) {
    return this.listeners.remove(listener);
  }

  @Override
  public boolean containsTransformationChangeListener(TransformationChangeListener listener) {
    return this.listeners.contains(listener);
  }
  
  protected void fireTransformationsConfigurationModelEvent(
    TransformationChangeType type,
    Object newValue
  ) {
    this.fireTransformationsConfigurationModelEvent(type, null, newValue);
  }
  
  protected void fireTransformationsConfigurationModelEvent(
    TransformationChangeType type,
    Object oldValue,
    Object newValue
  ) {
    final TransformationChangeEvent event = TransformationChangeEvent.of(this, type, oldValue, newValue);
    
    this.listeners.forEach(listener -> listener.onTransformationChange(event));
  }
}
