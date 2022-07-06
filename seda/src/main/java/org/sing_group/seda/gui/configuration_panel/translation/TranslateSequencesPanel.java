/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui.configuration_panel.translation;

import java.awt.*;

import javax.swing.*;

import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.transformation.provider.translation.TranslateSequencesTransformationProvider;

public class TranslateSequencesPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private SequenceTranslationConfigurationPanel sequenceTranslationConfigurationPanel;
  private TranslateSequencesTransformationProvider transformationProvider;

  public TranslateSequencesPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.sequenceTranslationConfigurationPanel = new SequenceTranslationConfigurationPanel(false);
    this.sequenceTranslationConfigurationPanel.addPropertyChangeListener(
      new SequenceTranslationPanelPropertyChangeAdapter() {
        @Override
        protected void translationPropertyChanged() {
          translationConfigurationChanged();
        }

        @Override
        protected void joinFramesPropertyChanged() {
          translationConfigurationChanged();
        }

        @Override
        protected void framesPropertyChanged() {
          translationConfigurationChanged();
        }

        @Override
        protected void codonTablePropertyChanged() {
          translationConfigurationChanged();
        }

        @Override
        protected void reverseSequencesPropertyChanged() {
          translationConfigurationChanged();
        }
      }
    );
    this.add(new CenteredJPanel(sequenceTranslationConfigurationPanel), BorderLayout.CENTER);
  }

  private void translationConfigurationChanged() {
    if (this.sequenceTranslationConfigurationPanel.isValidUserSelection()) {
      this.transformationProvider.setTranslationConfiguration(
        this.sequenceTranslationConfigurationPanel.getSequenceTranslationConfiguration()
      );
    } else {
      this.transformationProvider.clearTranslationConfiguration();
    }
  }

  private void initTransformationProvider() {
    this.transformationProvider = new TranslateSequencesTransformationProvider();
    this.translationConfigurationChanged();
  }

  public TranslateSequencesTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(TranslateSequencesTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;
    if (transformationProvider.getTranslationConfiguration() != null) {
      this.sequenceTranslationConfigurationPanel
        .setSequenceTranslationConfiguration(transformationProvider.getTranslationConfiguration());
    }
  }
}
