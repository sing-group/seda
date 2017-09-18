package org.sing_group.seda.gui.pattern;

import java.awt.Component;

import javax.swing.event.ChangeEvent;

import org.sing_group.seda.gui.TestGuiUtils;

public class SequenceTranslationPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    SequenceTranslationPanel testPanel = new SequenceTranslationPanel();

    testPanel.addSequenceTranslationPanelListener(
      new SequenceTranslationPanelListener() {

        @Override
        public void configurationChanged(ChangeEvent event) {
          System.err.println("Configuration changed. Is it valid? " + testPanel.isValidUserSelection());
        }
      }
    );

    return testPanel;
  }
}
