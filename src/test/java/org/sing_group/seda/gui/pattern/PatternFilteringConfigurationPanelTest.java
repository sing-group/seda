package org.sing_group.seda.gui.pattern;

import java.awt.Component;

import org.sing_group.seda.gui.TestGuiUtils;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;

public class PatternFilteringConfigurationPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    PatternFilteringConfigurationPanel testPanel = new PatternFilteringConfigurationPanel();
    PatternFilteringTransformationProvider transformationProvider =
      testPanel.getPatternFilteringTransformationProvider();
    transformationProvider.addTransformationChangeListener(
      new TransformationChangeListener() {

        @Override
        public void onTransformationChange(TransformationChangeEvent event) {
          System.err.println("Transformation changed. Event type: " + 
            event.getType() + ". Is valid transformation? " + 
            transformationProvider.isValidTransformation());
        }
      }
    );

    return testPanel;
  }
}
