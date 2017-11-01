package org.sing_group.seda.gui.ncbi;

import java.awt.Component;

import org.sing_group.seda.gui.TestGuiUtils;

public class NcbiRenameConfigurationPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    NcbiRenameConfigurationPanel panel = new NcbiRenameConfigurationPanel();
    panel.getModel().addTransformationChangeListener(TestGuiUtils.TRANSFORMATION_CHANGE_LISTENER);

    return panel;
  }
}
