package org.sing_group.seda.gui.rename;

import java.util.Arrays;

import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.gui.TestGuiUtils;

public class RenameTransformationConfigurationPanelTest {

  private static final SedaContext TEST_CONTEXT = new SedaContext();
  
  public static void main(String[] args) {
    RenameTransformationConfigurationPanel panel = new RenameTransformationConfigurationPanel();
    
    panel.setSedaContext(TEST_CONTEXT);
    panel.addRenamePanelEventListener(
      new RenamePanelEventListener() {

        @Override
        public void onRenameConfigurationChanged(Object source) {
          System.err.println("Configuration changed. Valid = " + panel.isValidConfiguration());
        }
      }
    );
    TEST_CONTEXT.setSelectedPaths(Arrays.asList("src/test/resources/fasta/write-test.fa"));
    TestGuiUtils.showComponent(panel);
  }
}
