package org.sing_group.seda.gui.filtering.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.sing_group.seda.gui.TestGuiUtils;

public class MultipleBasePresenceConfigurationPanelTest {

  public static void main(String[] args) {
    MultipleBasePresenceConfigurationPanel panel = new MultipleBasePresenceConfigurationPanel();
    panel.addPropertyChangeListener(MultipleBasePresenceConfigurationPanel.PROPERTY_BASE_PRESENCES, new PropertyChangeListener() {
      
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        System.err.println("New base configuration . Is valid? " + panel.isValidValue());
        panel.getBasePresences().forEach(s -> {
          System.err.println("\t- " + s.toString());
        });
      }
    });
    TestGuiUtils.showComponent(panel);
  }
}
