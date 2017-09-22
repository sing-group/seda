package org.sing_group.seda.gui.pattern;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.WEST;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;

import javax.swing.JPanel;

import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.datatype.SequenceTarget;

public class PatternFilteringConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private PatternFilteringTransformationProvider transformationProvider;
  private MultipleSequencePatternGroupPanel patternsPanel;
  private SequenceTranslationPanel translationPanel;
  private RadioButtonsPanel<SequenceTarget> sequenceTargetPanel;

  public PatternFilteringConfigurationPanel() {
    this.init();
    this.transformationProvider = new PatternFilteringTransformationProvider(
      this.patternsPanel, this.translationPanel, this.sequenceTargetPanel
    );
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getNorthPanel(), NORTH);
    this.add(getPatternsPanel(), CENTER);
  }

  private JPanel getNorthPanel() {
    JPanel northPanel = new JPanel(new BorderLayout());
    northPanel.add(getPatternModePanel(), WEST);
    northPanel.add(getTranslationPanel(), EAST);

    sequenceTargetPanel.setSelectedItem(SequenceTarget.SEQUENCE);

    return northPanel;
  }

  private Component getPatternModePanel() {
    sequenceTargetPanel = new RadioButtonsPanel<>(SequenceTarget.values());
    sequenceTargetPanel.addItemListener(this::sequenceTargetChanged);

    return sequenceTargetPanel;
  }

  private void sequenceTargetChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.translationPanel.setConversionEnabled(this.sequenceTargetPanel.getSelectedItem().get().isSequence());
    }
  }

  private Component getTranslationPanel() {
    this.translationPanel = new SequenceTranslationPanel();

    return new CenteredJPanel(this.translationPanel);
  }

  public MultipleSequencePatternGroupPanel getPatternsPanel() {
    if (this.patternsPanel == null) {
      this.patternsPanel = new MultipleSequencePatternGroupPanel();
    }
    return this.patternsPanel;
  }

  public PatternFilteringTransformationProvider getPatternFilteringTransformationProvider() {
    return this.transformationProvider;
  }
}
