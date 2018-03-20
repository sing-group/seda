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
