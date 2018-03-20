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
package org.sing_group.seda.gui.pattern;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.gui.translation.SequenceTranslationPanel;
import org.sing_group.seda.gui.translation.SequenceTranslationPanelPropertyChangeAdapter;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeType;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.PatternFilteringSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class PatternFilteringTransformationProvider extends AbstractTransformationProvider
  implements SequencePatternEditorListener, ItemListener {
  public enum PatternFilteringEventType implements TransformationChangeType {
    PATTERN_EDITED, PATTERN_ADDED, PATTERN_REMOVED, PATTERN_TYPE_CHANGED;
  }

  private MultipleSequencePatternGroupPanel patternsPanel;
  private SequenceTranslationPanel translationPanel;
  private RadioButtonsPanel<SequenceTarget> sequenceTargetPanel;

  public PatternFilteringTransformationProvider(
    PatternFilteringConfigurationPanel patternFilteringConfigurationPanel
  ) {
    this(
      patternFilteringConfigurationPanel.getPatternsPanel(),
      patternFilteringConfigurationPanel.getTranslationPanel(),
      patternFilteringConfigurationPanel.getSequenceTargetPanel()
    );
  }

  protected PatternFilteringTransformationProvider(
    MultipleSequencePatternGroupPanel patternsPanel, SequenceTranslationPanel translationPanel,
    RadioButtonsPanel<SequenceTarget> sequenceTargetPanel
  ) {
    this.patternsPanel = patternsPanel;
    this.patternsPanel.addSequencePatternEditorListener(this);
    this.translationPanel = translationPanel;
    this.translationPanel.addPropertyChangeListener(
      new SequenceTranslationPanelPropertyChangeAdapter() {
        @Override
        protected void translationPropertyChanged() {
          notifyTranslationConfigurationChanged();
        }

        @Override
        protected void joinFramesPropertyChanged() {
          notifyTranslationConfigurationChanged();
        }

        @Override
        protected void framesPropertyChanged() {
          notifyTranslationConfigurationChanged();
        }

        @Override
        protected void codonTablePropertyChanged() {
          notifyTranslationConfigurationChanged();
        }

        @Override
        protected void reverseSequencesPropertyChanged() {
          notifyTranslationConfigurationChanged();
        };
      }
    );
    this.sequenceTargetPanel = sequenceTargetPanel;
    this.sequenceTargetPanel.addItemListener(this);
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequencesGroupTransformation patternTransformation;

    EvaluableSequencePattern pattern = getEvaluableSequencePattern();

    if (getSelectedSequenceTarget().isSequence() && isTranslationSelected()) {
      SequenceTranslationConfiguration configuration = getSequenceTranslationConfiguration();

      patternTransformation = new PatternFilteringSequencesGroupTransformation(pattern, configuration, factory);
    } else {
      patternTransformation =
        new PatternFilteringSequencesGroupTransformation(pattern, getSelectedSequenceTarget(), factory);
    }

    SequencesGroupDatasetTransformation datasetTransformation =
      new ComposedSequencesGroupDatasetTransformation(factory, patternTransformation);

    return datasetTransformation;
  }

  protected EvaluableSequencePattern getEvaluableSequencePattern() {
    return this.patternsPanel.getEvaluableSequencePattern();
  }

  protected boolean isTranslationSelected() {
    return this.translationPanel.isTranslationSelected();
  }

  protected SequenceTranslationConfiguration getSequenceTranslationConfiguration() {
    return new SequenceTranslationConfiguration(getCodonTable(), isReverseSequences(), isJoinFrames(), getTranslationFrames());
  }

  protected int[] getTranslationFrames() {
    return this.translationPanel.getTranslationFrames();
  }

  protected boolean isJoinFrames() {
    return this.translationPanel.isJoinFrames();
  }

  protected boolean isReverseSequences() {
    return this.translationPanel.isReverseSequences();
  }

  protected Map<String, String> getCodonTable() {
    return this.translationPanel.getCodonTable();
  }

  @Override
  public void patternEdited(PatternEditionEvent event) {
    this.fireTransformationsConfigurationModelEvent(PatternFilteringEventType.PATTERN_EDITED, null);
  }

  @Override
  public void patternAdded(ChangeEvent event) {
    this.fireTransformationsConfigurationModelEvent(PatternFilteringEventType.PATTERN_ADDED, null);
  }

  @Override
  public void patternRemoved(ChangeEvent event) {
    this.fireTransformationsConfigurationModelEvent(PatternFilteringEventType.PATTERN_REMOVED, null);
  }

  public void notifyTranslationConfigurationChanged() {
    this.fireTransformationsConfigurationModelEvent(PatternFilteringEventType.PATTERN_TYPE_CHANGED, null);
  }

  @Override
  public boolean isValidTransformation() {
    return this.patternsPanel.isValidUserSelection() &&
      (!getSelectedSequenceTarget().isSequence() || this.translationPanel.isValidUserSelection());
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      this.fireTransformationsConfigurationModelEvent(PatternFilteringEventType.PATTERN_TYPE_CHANGED, null);
    }
  }

  protected SequenceTarget getSelectedSequenceTarget() {
    return this.sequenceTargetPanel.getSelectedItem().get();
  }
}
