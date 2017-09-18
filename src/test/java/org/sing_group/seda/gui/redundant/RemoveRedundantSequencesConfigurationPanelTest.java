package org.sing_group.seda.gui.redundant;

import org.sing_group.seda.gui.TestGuiUtils;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;

public class RemoveRedundantSequencesConfigurationPanelTest {

  public static void main(String[] args) {
    RemoveRedundantSequencesConfigurationPanel panel = new RemoveRedundantSequencesConfigurationPanel();
    panel.getPatternFilteringTransformationProvider().addTransformationChangeListener(
      new TransformationChangeListener() {

      @Override
      public void onTransformationChange(TransformationChangeEvent event) {
        System.err.println("Transformation changed: " + event.getType());
      }
    });
    TestGuiUtils.showComponent(panel);
  }
}
