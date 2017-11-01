package org.sing_group.seda.gui.ncbi;

import java.awt.Component;

import org.sing_group.seda.gui.TestGuiUtils;

public class ReplaceOptionsConfigurationPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    ReplaceOptionsConfigurationPanel panel = new ReplaceOptionsConfigurationPanel();
    panel.addPropertyChangeListener(TestGuiUtils.PROPERTY_CHANGE_LISTENER);

    return panel;
  }
}
