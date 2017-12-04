package org.sing_group.seda.gui.components;

import java.awt.Component;

import org.sing_group.seda.gui.TestGuiUtils;
import org.sing_group.seda.gui.translation.SequenceTranslationConfigurationPanel;

public class SequenceTranslationConfigurationPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    SequenceTranslationConfigurationPanel testPanel = new SequenceTranslationConfigurationPanel(true);
    testPanel.addPropertyChangeListener(TestGuiUtils.PROPERTY_CHANGE_LISTENER);

    return testPanel;
  }
}
