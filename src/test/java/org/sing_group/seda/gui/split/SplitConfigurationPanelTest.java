package org.sing_group.seda.gui.split;

import org.sing_group.seda.gui.TestGuiUtils;

public class SplitConfigurationPanelTest {

  public static void main(String[] args) {
    SplitConfigurationPanel panel = new SplitConfigurationPanel();
    panel.getModel().addTransformationChangeListener(TestGuiUtils.TRANSFORMATION_CHANGE_LISTENER);
    TestGuiUtils.showComponent(panel);
  }
}
