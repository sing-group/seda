package org.sing_group.seda.gui.filtering.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.sing_group.seda.gui.TestGuiUtils;

public class BasePresenceConfigurationPanelTest {

  public static void main(String[] args) {
    BasePresenceConfigurationPanel panel = new BasePresenceConfigurationPanel();
    panel.addPropertyChangeListener(BasePresenceConfigurationPanel.PROPERTY_BASE_PRESENCE, new PropertyChangeListener() {
      
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        System.err.println("New base configuration: " + panel.getBasePresence() + ". Is valid? " + panel.isValidValue());
      }
    });
    TestGuiUtils.showComponent(panel);
  }
}
