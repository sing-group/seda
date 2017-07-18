package org.sing_group.seda.plugin.spi;

import java.io.Serializable;

public class TransformationChangeEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  private final TransformationProvider provider;
  private final TransformationChangeType type;
  private final Object oldValue;
  private final Object newValue;
  
  public static TransformationChangeEvent of(
    TransformationProvider provider,
    TransformationChangeType type,
    Object oldValue,
    Object newValue
  ) {
    return new TransformationChangeEvent(provider, type, oldValue, newValue);
  }

  protected TransformationChangeEvent(
    TransformationProvider provider,
    TransformationChangeType type,
    Object oldValue,
    Object newValue
  ) {
    this.provider = provider;
    this.type = type;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  public TransformationProvider getProvider() {
    return this.provider;
  }

  public TransformationChangeType getType() {
    return this.type;
  }

  public Object getOldValue() {
    return this.oldValue;
  }

  public Object getNewValue() {
    return this.newValue;
  }
}
