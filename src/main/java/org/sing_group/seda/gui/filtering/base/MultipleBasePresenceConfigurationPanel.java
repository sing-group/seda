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
