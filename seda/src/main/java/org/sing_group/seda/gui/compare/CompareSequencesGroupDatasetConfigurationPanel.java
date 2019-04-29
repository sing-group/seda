/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui.compare;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.Y_AXIS;
import static org.sing_group.seda.gui.compare.CompareSequencesGroupDatasetTransformationProvider.DEFAULT_SEQUENCE_TARGET;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class CompareSequencesGroupDatasetConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String SEQUENCE_TARGET_HELP = "The part of the sequences that must be compared in order to "
    + "perform the comparisons.";

  private RadioButtonsPanel<SequenceTarget> sequenceTargetSelection;
  private CompareSequencesGroupDatasetTransformationProvider transformationProvider;
  private ReformatFastaConfigurationPanel reformatPanel;

  public CompareSequencesGroupDatasetConfigurationPanel() {
    this.init();
    this.transformationProvider = new CompareSequencesGroupDatasetTransformationProvider(this.reformatPanel.getModel());
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, Y_AXIS));
    mainPanel.add(getParametersPanel());
    mainPanel.add(getReformatFastaConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private JPanel getParametersPanel() {
    return new InputParametersPanel(getInputParameters());
  }

  private InputParameter getInputParameters() {
    this.sequenceTargetSelection = new RadioButtonsPanel<>(SequenceTarget.values(), 1, 2);
    this.sequenceTargetSelection.setSelectedItem(DEFAULT_SEQUENCE_TARGET);
    this.sequenceTargetSelection.addItemListener(this::sequenceTargetChanged);

    return new InputParameter("Sequence target:", this.sequenceTargetSelection, SEQUENCE_TARGET_HELP);
  }

  private Component getReformatFastaConfigurationPanel() {
    this.reformatPanel = new ReformatFastaConfigurationPanel();
    this.reformatPanel.setBorder(createTitledBorder("Reformat output file"));

    return this.reformatPanel;
  }

  private void sequenceTargetChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.transformationProvider.setSequenceTarget(this.sequenceTargetSelection.getSelectedItem().get());
    }
  }

  public TransformationProvider getModel() {
    return this.transformationProvider;
  }
}
