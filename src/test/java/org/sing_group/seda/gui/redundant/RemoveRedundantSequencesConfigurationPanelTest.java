package org.sing_group.seda.gui.redundant;

import static org.sing_group.seda.gui.TestGuiUtils.TRANSFORMATION_CHANGE_LISTENER;
import static org.sing_group.seda.gui.TestGuiUtils.showComponent;

public class RemoveRedundantSequencesConfigurationPanelTest {

  public static void main(String[] args) {
    RemoveRedundantSequencesConfigurationPanel panel = new RemoveRedundantSequencesConfigurationPanel();
    panel.getModel().addTransformationChangeListener(TRANSFORMATION_CHANGE_LISTENER);
    showComponent(panel);
  }
}
