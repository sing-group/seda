package org.sing_group.seda.gui.pattern;

import java.awt.Component;

import org.sing_group.seda.gui.TestGuiUtils;

public class PatternFilteringPluginPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    PatternFilteringPluginPanel testPanel = new PatternFilteringPluginPanel();
    PatternFilteringTransformationProvider transformationProvider =
      testPanel.getPatternFilteringTransformationProvider();
    transformationProvider.addTransformationChangeListener(TestGuiUtils.TRANSFORMATION_CHANGE_LISTENER);

    return testPanel;
  }
}
