package org.sing_group.seda.gui.components;

import java.awt.Component;

import org.sing_group.seda.gui.TestGuiUtils;

public class SequenceTranslationPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    SequenceTranslationPanel testPanel = new SequenceTranslationPanel();
    testPanel.addPropertyChangeListener(TestGuiUtils.PROPERTY_CHANGE_LISTENER);

    return testPanel;
  }
}
