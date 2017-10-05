package org.sing_group.seda.gui.redundant;

import javax.swing.event.ChangeEvent;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeType;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation;

public class RemoveRedundantSequencesTransformationProvider extends AbstractTransformationProvider
  implements RemoveRedundantSequencesConfigurationPanelListener {
  public enum RemoveRedundantSequencesConfiguratioEventType implements TransformationChangeType {
    CONFIGURATION_CHANGED
  }

  private RemoveRedundantSequencesConfigurationPanel configurationPanel;

  public RemoveRedundantSequencesTransformationProvider(RemoveRedundantSequencesConfigurationPanel configurationPanel
  ) {
    this.configurationPanel = configurationPanel;
    this.configurationPanel.addRemoveRedundantSequencesConfigurationPanelListener(this);
  }


  @Override
  public boolean isValidTransformation() {
    return configurationPanel.isValidUserSelection();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation
      .concat(
        new ComposedSequencesGroupDatasetTransformation(
          factory, new RemoveRedundantSequencesTransformation(this.configurationPanel.getConfiguration(), factory)
        )
      );
  }

  @Override
  public void configurationChanged(ChangeEvent event) {
    this.fireTransformationsConfigurationModelEvent(RemoveRedundantSequencesConfiguratioEventType.CONFIGURATION_CHANGED, null);
  }
}
