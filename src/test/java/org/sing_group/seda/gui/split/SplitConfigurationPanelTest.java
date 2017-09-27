package org.sing_group.seda.gui.split;

import org.sing_group.seda.gui.TestGuiUtils;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;

public class SplitConfigurationPanelTest {

  public static void main(String[] args) {
    SplitConfigurationPanel panel = new SplitConfigurationPanel();
    panel.getModel().addTransformationChangeListener(
      new TransformationChangeListener() {

        @Override
        public void onTransformationChange(TransformationChangeEvent event) {
          System.err.println("Transformation changed: " + event.getType());
        }
      }
    );
    TestGuiUtils.showComponent(panel);
  }
}
