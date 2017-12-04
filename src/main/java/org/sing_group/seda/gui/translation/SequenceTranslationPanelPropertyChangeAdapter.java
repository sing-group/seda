package org.sing_group.seda.gui.translation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SequenceTranslationPanelPropertyChangeAdapter implements PropertyChangeListener {

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case SequenceTranslationPanel.PROPERTY_TRANSLATION:
        this.translationPropertyChanged();
        break;
      case SequenceTranslationPanel.PROPERTY_JOIN_FRAMES:
        this.joinFramesPropertyChanged();
        break;
      case SequenceTranslationPanel.PROPERTY_FRAMES:
        this.framesPropertyChanged();
        break;
      case SequenceTranslationPanel.PROPERTY_CODON_TABLE:
        this.codonTablePropertyChanged();
        break;
      case SequenceTranslationPanel.PROPERTY_REVERSE_SEQUENCES:
        this.reverseSequencesPropertyChanged();
        break;
    }
  }

  protected void translationPropertyChanged() {}

  protected void joinFramesPropertyChanged() {}

  protected void framesPropertyChanged() {}

  protected void codonTablePropertyChanged() {}

  protected void reverseSequencesPropertyChanged() {}
}
