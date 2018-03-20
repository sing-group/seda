/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
