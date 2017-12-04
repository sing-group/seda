package org.sing_group.seda.gui.translation;

import java.beans.PropertyChangeEvent;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.TranslateSequencesGroupDatasetTransformation;

public class TranslateSequencesTransformationProvider extends AbstractTransformationProvider {

  private SequenceTranslationConfigurationPanel sequenceTranslationPanel;

  public TranslateSequencesTransformationProvider(SequenceTranslationConfigurationPanel sequenceTranslationPanel) {
    this.sequenceTranslationPanel = sequenceTranslationPanel;
    this.sequenceTranslationPanel.addPropertyChangeListener(this::sequenceTranslationPropertyChanged);
  }

  public void sequenceTranslationPropertyChanged(PropertyChangeEvent evt) {
    switch(evt.getPropertyName()) {
      case SequenceTranslationPanel.PROPERTY_CODON_TABLE:
        fireTransformationsConfigurationModelEvent(TranslateSequencesTransformationChangeType.CODON_TABLE_CHANGED, this.sequenceTranslationPanel.getCodonTable());
        break;
      case SequenceTranslationPanel.PROPERTY_FRAMES:
        fireTransformationsConfigurationModelEvent(TranslateSequencesTransformationChangeType.FRAMES, this.sequenceTranslationPanel.getTranslationFrames());
        break;
    }
  }

  @Override
  public boolean isValidTransformation() {
    return this.sequenceTranslationPanel.isValidUserSelection();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return new TranslateSequencesGroupDatasetTransformation(getTranslationConfiguration(), factory);
  }

  private SequenceTranslationConfiguration getTranslationConfiguration() {
    return new SequenceTranslationConfiguration(
      this.sequenceTranslationPanel.getCodonTable(), this.sequenceTranslationPanel.isReverseSequences(), this.sequenceTranslationPanel.getTranslationFrames()
    );
  }
}
