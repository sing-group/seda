package org.sing_group.seda.gui.filtering.header;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.sing_group.seda.gui.TestGuiUtils;

public class HeaderFilteringConfigurationPanelTest {

  public static void main(String[] args) {
    HeaderFilteringConfigurationPanel panel = new HeaderFilteringConfigurationPanel();
    panel.addPropertyChangeListener(HeaderFilteringConfigurationPanel.PROPERTY_FILTER_CONFIGURATION, 
      new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          System.err.println(
            "Changed. Is valid configuration? " + panel.getHeaderFilteringConfiguration().isValidConfiguration()
          );
        }
      }
    );
    TestGuiUtils.showComponent(panel);
  }
}
