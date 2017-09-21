package org.sing_group.seda.gui.pattern;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeType;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ComposedSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.PatternFilteringSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.PatternFilteringSequencesGroupTransformation.PatternTarget;
import org.sing_group.seda.transformation.sequencesgroup.PatternFilteringSequencesGroupTransformation.SequenceTranslationConfiguration;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class PatternFilteringTransformationProvider extends AbstractTransformationProvider
  implements SequencePatternEditorListener, SequenceTranslationPanelListener, ItemListener {
  public enum PatternFilteringEventType implements TransformationChangeType {
    PATTERN_EDITED, PATTERN_ADDED, PATTERN_REMOVED, PATTERN_TYPE_CHANGED;
  }

  private MultipleSequencePatternGroupPanel patternsPanel;
  private SequenceTranslationPanel translationPanel;
  private RadioButtonsPanel<PatternTarget> patternTargetPanel;

  public PatternFilteringTransformationProvider(
    MultipleSequencePatternGroupPanel patternsPanel, SequenceTranslationPanel translationPanel,
    RadioButtonsPanel<PatternTarget> patternTargetPanel
  ) {
    this.patternsPanel = patternsPanel;
    this.patternsPanel.addSequencePatternEditorListener(this);
    this.translationPanel = translationPanel;
    this.translationPanel.addSequenceTranslationPanelListener(this);
    this.patternTargetPanel = patternTargetPanel;
    this.patternTargetPanel.addItemListener(this);
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    final List<SequenceTransformation> seqTransformations = new LinkedList<>();
    final List<SequencesGroupTransformation> sequencesGroupTransformations = new LinkedList<>();

    if (!seqTransformations.isEmpty()) {
      sequencesGroupTransformations.add(new ComposedSequencesGroupTransformation(factory, seqTransformations));
    }

    SequencesGroupTransformation patternTransformation;
    EvaluableSequencePattern pattern = this.patternsPanel.getEvaluableSequencePattern();
    if (this.translationPanel.isTranslationSelected()) {
      SequenceTranslationConfiguration configuration =
        new SequenceTranslationConfiguration(
          this.translationPanel.getCodonTable(), this.translationPanel.getTranslationFrames()
        );
      patternTransformation = new PatternFilteringSequencesGroupTransformation(pattern, configuration, factory);
    } else {
      patternTransformation = new PatternFilteringSequencesGroupTransformation(pattern, getSelectedPatternTarget(), factory);
    }

    sequencesGroupTransformations.add(patternTransformation);

    SequencesGroupDatasetTransformation datasetTransformation =
      new ComposedSequencesGroupDatasetTransformation(factory, sequencesGroupTransformations);

    return datasetTransformation;
  }

  @Override
  public void patternEdited(ChangeEvent event) {
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

  @Override
  public void configurationChanged(ChangeEvent event) {
    this.fireTransformationsConfigurationModelEvent(PatternFilteringEventType.PATTERN_TYPE_CHANGED, null);
  }

  @Override
  public boolean isValidTransformation() {
    return this.patternsPanel.isValidUserSelection() &&
      (!getSelectedPatternTarget().isSequence() || this.translationPanel.isValidUserSelection());
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      this.fireTransformationsConfigurationModelEvent(PatternFilteringEventType.PATTERN_TYPE_CHANGED, null);
    }
  }

  private PatternTarget getSelectedPatternTarget() {
    return this.patternTargetPanel.getSelectedItem().get();
  }
}
