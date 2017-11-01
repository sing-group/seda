package org.sing_group.seda.gui.ncbi;

import java.awt.Component;

import org.sing_group.seda.gui.TestGuiUtils;

public class NcbiTaxonomyConfigurationPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    NcbiTaxonomyConfigurationPanel panel = new NcbiTaxonomyConfigurationPanel();
    panel.addPropertyChangeListener(TestGuiUtils.PROPERTY_CHANGE_LISTENER);

    return panel;
  }
}