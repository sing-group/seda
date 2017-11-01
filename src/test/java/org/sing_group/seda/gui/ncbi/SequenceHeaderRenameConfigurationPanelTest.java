package org.sing_group.seda.gui.ncbi;

import java.awt.Component;

import org.sing_group.seda.gui.TestGuiUtils;

public class SequenceHeaderRenameConfigurationPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    SequenceHeaderRenameConfigurationPanel panel = new SequenceHeaderRenameConfigurationPanel();
    panel.addPropertyChangeListener(TestGuiUtils.PROPERTY_CHANGE_LISTENER);

    return panel;
  }
}
