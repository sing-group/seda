/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
