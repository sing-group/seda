/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui.filtering.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.ComponentsListPanel;
import org.sing_group.seda.transformation.sequencesgroup.FilterByBasePresenceTransformation;

public class MultipleBasePresenceConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_BASE_PRESENCES = "seda.base.presences";

  private ComponentsListPanel<BasePresenceConfigurationPanel> basePresences;
  private List<FilterByBasePresenceTransformation.BasePresence> oldValue;

  public MultipleBasePresenceConfigurationPanel() {
    this.init();
  }

  private void init() {
    basePresences = new ComponentsListPanel<BasePresenceConfigurationPanel>(1) {
      private static final long serialVersionUID = 1L;

      @Override
      protected BasePresenceConfigurationPanel getGenericComponent() {
        BasePresenceConfigurationPanel component = new BasePresenceConfigurationPanel();
        component.addPropertyChangeListener(BasePresenceConfigurationPanel.PROPERTY_BASE_PRESENCE, new PropertyChangeListener() {

          @Override
          public void propertyChange(PropertyChangeEvent evt) {
            configurationChanged();
          }
        });
        return component;
      }

      @Override
      protected String getAddComponentButtonLabel() {
        return "Add new base filter";
      }

      @Override
      protected void addComponentWrapPanelComponent() {
        super.addComponentWrapPanelComponent();
        configurationChanged();
      }

      @Override
      protected void removeComponentWrapPanel(
        ComponentsListPanel<BasePresenceConfigurationPanel>.ComponentWrapPanel component
      ) {
        super.removeComponentWrapPanel(component);
        configurationChanged();
      }

      @Override
      protected void removeAllComponents() {
        super.removeAllComponents();
        configurationChanged();
      }
    };
    this.add(basePresences);
  }

  private void configurationChanged() {
    List<FilterByBasePresenceTransformation.BasePresence> newValue = getBasePresences();
    firePropertyChange(PROPERTY_BASE_PRESENCES, oldValue, newValue);
    oldValue = newValue;
  }

  public List<FilterByBasePresenceTransformation.BasePresence> getBasePresences() {
    if (this.basePresences != null) {
      return this.basePresences.getComponentsList().stream().map(BasePresenceConfigurationPanel::getBasePresence)
        .collect(Collectors.toList());
    } else
      return Collections.emptyList();
  }

  public boolean isValidValue() {
    for (BasePresenceConfigurationPanel panel : this.basePresences.getComponentsList()) {
      if (!panel.isValidValue()) {
        return false;
      }
    }
    return true;
  }
}
