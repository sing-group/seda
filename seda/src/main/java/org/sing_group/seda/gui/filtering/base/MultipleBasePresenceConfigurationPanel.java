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
package org.sing_group.seda.gui.filtering.base;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.gui.filtering.base.BasePresenceConfigurationPanel.PROPERTY_BASE_PRESENCE;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

import org.sing_group.gc4s.ui.ComponentsListPanel;
import org.sing_group.seda.core.operations.BasePresence;

public class MultipleBasePresenceConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_BASE_PRESENCES = "seda.base.presences";

  private CustomComponentsListPanel basePresencesPanel;
  private List<BasePresence> oldValue;

  public MultipleBasePresenceConfigurationPanel() {
    this.init();
  }

  private void init() {
    basePresencesPanel = new CustomComponentsListPanel(1);
    this.add(basePresencesPanel);
  }

  private void configurationChanged() {
    List<BasePresence> newValue = getBasePresences();
    firePropertyChange(PROPERTY_BASE_PRESENCES, oldValue, newValue);
    oldValue = newValue;
  }

  public List<BasePresence> getBasePresences() {
    if (!isValidValue()) {
      return emptyList();
    }

    if (this.basePresencesPanel != null) {
      return this.basePresencesPanel.getComponentsList().stream().map(BasePresenceConfigurationPanel::getBasePresence)
        .collect(toList());
    } else
      return emptyList();
  }

  public boolean isValidValue() {
    if (basePresencesPanel != null) {
      for (BasePresenceConfigurationPanel panel : this.basePresencesPanel.getComponentsList()) {
        if (!panel.isValidValue()) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  public void setBasePresences(List<BasePresence> basePresences) {
    this.basePresencesPanel.removeAllComponents();
    basePresences.forEach(this.basePresencesPanel::addNewBasePresence);
  }

  private class CustomComponentsListPanel extends ComponentsListPanel<BasePresenceConfigurationPanel> {
    private static final long serialVersionUID = 1L;

    public CustomComponentsListPanel(int initialComponents) {
      super(initialComponents);
    }

    @Override
    protected BasePresenceConfigurationPanel getGenericComponent() {
      BasePresenceConfigurationPanel component = new BasePresenceConfigurationPanel();
      addListener(component);

      return component;
    }

    private void addListener(BasePresenceConfigurationPanel component) {
      component.addPropertyChangeListener(PROPERTY_BASE_PRESENCE, new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          configurationChanged();
        }
      });
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
    public void removeAllComponents() {
      super.removeAllComponents();
      configurationChanged();
    }

    public void addNewBasePresence(BasePresence basePresence) {
      super.addComponentWrapPanelComponent(() -> {
        BasePresenceConfigurationPanel component = new BasePresenceConfigurationPanel(basePresence);
        addListener(component);

        return component;
      });
      configurationChanged();
    }
  }
}
