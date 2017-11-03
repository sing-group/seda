package org.sing_group.seda.gui.redundant;

import java.util.Map;

import javax.swing.event.ChangeEvent;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.gui.components.SequenceTranslationPanel;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeType;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

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
          factory, getRemoveRedundantSequencesTransformation(factory)
        )
      );
  }

  private SequencesGroupTransformation getRemoveRedundantSequencesTransformation(DatatypeFactory factory) {
    if (isTranslationSelected()) {
      return new RemoveRedundantSequencesTransformation(
        this.configurationPanel.getConfiguration(), getSequenceTranslationConfiguration(), factory
      );
    } else {
      return new RemoveRedundantSequencesTransformation(this.configurationPanel.getConfiguration(), factory);
    }
  }

  @Override
  public void configurationChanged(ChangeEvent event) {
    this.fireTransformationsConfigurationModelEvent(RemoveRedundantSequencesConfiguratioEventType.CONFIGURATION_CHANGED, null);
  }

  private SequenceTranslationPanel getTranslationPanel() {
    return this.configurationPanel.getTranslationPanel();
  }

  protected boolean isTranslationSelected() {
    return getTranslationPanel().isTranslationSelected();
  }

  protected SequenceTranslationConfiguration getSequenceTranslationConfiguration() {
    return new SequenceTranslationConfiguration(getCodonTable(), isJoinFrames(), getTranslationFrames());
  }

  protected int[] getTranslationFrames() {
    return getTranslationPanel().getTranslationFrames();
  }

  protected boolean isJoinFrames() {
    return getTranslationPanel().isJoinFrames();
  }

  protected Map<String, String> getCodonTable() {
    return getTranslationPanel().getCodonTable();
  }
}
