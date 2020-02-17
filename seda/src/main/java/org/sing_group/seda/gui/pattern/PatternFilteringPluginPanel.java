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
package org.sing_group.seda.gui.pattern;

import java.awt.Component;
import java.awt.event.ItemEvent;

import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.gui.translation.SequenceTranslationPanelPropertyChangeAdapter;

public class PatternFilteringPluginPanel extends CenteredJPanel {
  private static final long serialVersionUID = 1L;
  private PatternFilteringConfigurationPanel transformationPanel;
  private PatternFilteringTransformationProvider transformationProvider;

  public PatternFilteringPluginPanel() {
    this.transformationProvider = new PatternFilteringTransformationProvider();
    this.init();
  }

  private void init() {
    this.add(getConfigurationPanel());
  }

  private Component getConfigurationPanel() {
    this.transformationPanel = new PatternFilteringConfigurationPanel();
    this.updateProviderTarget();
    this.addListeners();

    return this.transformationPanel;
  }

  private void addListeners() {
    this.transformationPanel.getPatternsPanel().addSequencePatternEditorListener(
      new SequencePatternEditorListener() {

      @Override
      public void patternRemoved(ChangeEvent event) {
        patternsChanged();
      }

      @Override
      public void patternEdited(PatternEditionEvent event) {
        patternsChanged();
      }

      @Override
      public void patternAdded(ChangeEvent event) {
        patternsChanged();
      }
    });

    this.transformationPanel.getTranslationPanel().addPropertyChangeListener(
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
        };
      }
    );

    this.transformationPanel.getSequenceTargetPanel().addItemListener(this::sequenceTargetChanged);
  }

  public void sequenceTargetChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      this.updateProviderTarget();
    }
  }

  private void updateProviderTarget() {
    this.transformationProvider.setTarget(this.transformationPanel.getSequenceTargetPanel().getSelectedItem().get());
  }

  private void translationConfigurationChanged() {
    if (
      this.transformationPanel.getTranslationPanel().isTranslationSelected()
        && this.transformationPanel.getTranslationPanel().isValidUserSelection()
    ) {
      this.transformationProvider.setTranslationConfiguration(
        this.transformationPanel.getTranslationPanel().getSequenceTranslationConfiguration()
      );
    } else {
      this.transformationProvider.clearTranslationConfiguration();
    }
  }

  private void patternsChanged() {
    if (this.transformationPanel.getPatternsPanel().isValidUserSelection()) {
      this.transformationProvider.setPattern(this.transformationPanel.getPatternsPanel().getEvaluableSequencePattern());
    } else {
      this.transformationProvider.clearPattern();
    }
  }

  public PatternFilteringTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }
}
