package org.sing_group.seda.gui.components;

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
    }
  }

  protected void translationPropertyChanged() {}

  protected void joinFramesPropertyChanged() {}

  protected void framesPropertyChanged() {}

  protected void codonTablePropertyChanged() {}
}