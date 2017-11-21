package org.sing_group.seda.gui.merge;

import org.sing_group.seda.gui.TestGuiUtils;

public class MergeConfigurationPanelTest {

  public static void main(String[] args) {
    MergeConfigurationPanel panel = new MergeConfigurationPanel();
    panel.getModel().addTransformationChangeListener(TestGuiUtils.TRANSFORMATION_CHANGE_LISTENER);
    TestGuiUtils.showComponent(panel);
  }
}
