package org.sing_group.seda.gui.pattern;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CenteredJPanel;

public class PatternFilteringConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  PatternFilteringTransformationProvider transformationProvider;
  private MultipleSequencePatternGroupPanel patternsPanel;
  private SequenceTranslationPanel translationPanel;


  public PatternFilteringConfigurationPanel() {
    this.init();
    this.transformationProvider = new PatternFilteringTransformationProvider(this.patternsPanel, this.translationPanel);
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getTranslationPanel(), NORTH);
    this.add(getPatternsPanel(), CENTER);
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
