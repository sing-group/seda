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
