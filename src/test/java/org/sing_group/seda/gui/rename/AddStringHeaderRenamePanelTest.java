package org.sing_group.seda.gui.rename;

import java.awt.Component;

import org.sing_group.seda.gui.TestGuiUtils;

public class AddStringHeaderRenamePanelTest {
  
  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());    
  }

  private static Component getTestPanel() {
    AddStringHeaderRenamePanel panel = new AddStringHeaderRenamePanel();
    panel.addRenamePanelEventListener(
      new RenamePanelEventListener() {

        @Override
        public void onRenameConfigurationChanged(Object source) {
          System.err.println("Configuration changed. Valid = " + panel.isValidConfiguration());
        }
      }
    );
    return panel;
  }
}
