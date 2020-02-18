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
import org.sing_group.seda.gui.translation.SequenceTranslationPanel;

public class PatternFilteringConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String TRANSLATION_LABEL = "Convert to amino acid sequence before pattern matching";
  private static final String TRANSLATION_INFO = "<html>If this option is selected, then input nucleic acid sequences are "
    + "translated into amino acid sequences before applying the pattern matching. <br/>If a translated sequence "
    + "matches the defined pattern, then the input nucleic acid sequence is reported.</html>";

  private MultipleSequencePatternGroupPanel patternsPanel;
  private SequenceTranslationPanel translationPanel;
  private RadioButtonsPanel<SequenceTarget> sequenceTargetPanel;

  public PatternFilteringConfigurationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getNorthPanel(), NORTH);
    this.add(getPatternsPanel(), CENTER);
  }

  private JPanel getNorthPanel() {
    JPanel northPanel = new JPanel(new BorderLayout());
    northPanel.add(getSequenceTargetPanel(), WEST);
    northPanel.add(getTranslationPanelComponent(), EAST);

    sequenceTargetPanel.setSelectedItem(SequenceTarget.SEQUENCE);

    return northPanel;
  }

  public RadioButtonsPanel<SequenceTarget> getSequenceTargetPanel() {
    if (sequenceTargetPanel == null) {
      sequenceTargetPanel = new RadioButtonsPanel<>(SequenceTarget.values());
      sequenceTargetPanel.addItemListener(this::sequenceTargetChanged);
    }

    return sequenceTargetPanel;
  }

  private void sequenceTargetChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.translationPanel.setConversionEnabled(this.sequenceTargetPanel.getSelectedItem().get().isSequence());
    }
  }

  private Component getTranslationPanelComponent() {
    return new CenteredJPanel(this.getTranslationPanel());
  }

  public SequenceTranslationPanel getTranslationPanel() {
    if (this.translationPanel == null) {
      this.translationPanel = new SequenceTranslationPanel(TRANSLATION_LABEL, TRANSLATION_INFO, true);
    }

    return this.translationPanel;
  }

  public MultipleSequencePatternGroupPanel getPatternsPanel() {
    if (this.patternsPanel == null) {
      this.patternsPanel = new MultipleSequencePatternGroupPanel();
    }
    return this.patternsPanel;
  }
}
