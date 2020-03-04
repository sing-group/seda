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
package org.sing_group.seda.gui.split;

import static java.awt.BorderLayout.CENTER;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.event.RunnableDocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.split.SequencesGroupSplitMode;

public class SplitConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private SplitConfigurationTransformationProvider transformationProvider;

  private RadioButtonsPanel<SequencesGroupSplitMode> splitModePanel;
  private JCheckBox randomize;
  private JIntegerTextField randomSeedTf;
  private JCheckBox independentExtractions;
  private JIntegerTextField numFilesTf;
  private JIntegerTextField numSequencesTf;

  public SplitConfigurationPanel() {
    this.init();
    this.transformationProvider =
      new SplitConfigurationTransformationProvider(
        this.splitModePanel.getSelectedItem().get(), this.randomize.isSelected(), this.randomSeedTf.getValue(),
        this.independentExtractions.isSelected(), this.numFilesTf.getValue(), this.numSequencesTf.getValue()
      );
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);

    this.checkTextFieldsStatus();
  }

  private JPanel getMainPanel() {
    return new CenteredJPanel(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getRandomizeParameter());
    parameters.add(getRandomSeedParameter());
    parameters.add(getSplitModeParameter());
    parameters.add(getNumFilesParameter());
    parameters.add(getNumSequencesParameter());
    parameters.add(getIndependentExtractionsParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getSplitModeParameter() {
    this.splitModePanel =
      new RadioButtonsPanel<>(SequencesGroupSplitMode.values(), SequencesGroupSplitMode.values().length, 1);
    this.splitModePanel.addItemListener(this::splitModeChanged);

    return new InputParameter("Split mode:", this.splitModePanel, "Split mode.");
  }

  private void splitModeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.transformationProvider.setSplitMode(this.splitModePanel.getSelectedItem().get());
      checkTextFieldsStatus();
    }
  }

  private void checkTextFieldsStatus() {
    switch (this.splitModePanel.getSelectedItem().get()) {
      case FIXED_FILES:
        this.independentExtractions.setEnabled(false);
        this.numFilesTf.setEditable(true);
        this.numFilesTf.setEnabled(true);
        this.numSequencesTf.setEditable(false);
        this.numSequencesTf.setEnabled(false);
        break;
      case FIXED_SEQUENCES_PER_FILE:
        this.independentExtractions.setEnabled(false);
        this.numFilesTf.setEditable(false);
        this.numFilesTf.setEnabled(false);
        this.numSequencesTf.setEditable(true);
        this.numSequencesTf.setEnabled(true);
        break;
      case SEQUENCES_PER_FILE_AND_FILES:
        this.independentExtractions.setEnabled(true);
        this.numFilesTf.setEditable(true);
        this.numFilesTf.setEnabled(true);
        this.numSequencesTf.setEditable(true);
        this.numSequencesTf.setEnabled(true);
        break;
      default:
        break;
    }
  }

  private InputParameter getRandomizeParameter() {
    this.randomize = new JCheckBox("Randomize", false);
    this.randomize.addItemListener(this::randomizeChanged);

    return new InputParameter("", randomize, "Whether sequences must be randomized or not.");
  }

  private void randomizeChanged(ItemEvent event) {
    this.transformationProvider.setRandomize(this.randomize.isSelected());
    this.randomSeedTf.setEnabled(event.getStateChange() == ItemEvent.SELECTED);
  }

  private InputParameter getRandomSeedParameter() {
    this.randomSeedTf = new JIntegerTextField(1);
    this.randomSeedTf.setEnabled(this.randomize.isSelected());
    this.randomSeedTf.getDocument().addDocumentListener(new RunnableDocumentAdapter(this::randomSeedChanged));

    return new InputParameter(
      "Seed:", this.randomSeedTf, "<html>The random seed to randomize the sequences. <br/>"
        + "This allows the same result to be reproduced in different runs and environments with same random seed.</html>"
    );
  }

  private void randomSeedChanged() {
    this.transformationProvider.setRandomSeed(this.randomSeedTf.getValue());
  }

  private InputParameter getNumFilesParameter() {
    this.numFilesTf = new JIntegerTextField(1);
    this.numFilesTf.getDocument().addDocumentListener(new RunnableDocumentAdapter(this::numFilesChanged));

    return new InputParameter("Number of files", this.numFilesTf, "The desired number of files.");
  }

  private void numFilesChanged() {
    this.transformationProvider.numFiles(this.numFilesTf.getValue());
  }

  private InputParameter getNumSequencesParameter() {
    this.numSequencesTf = new JIntegerTextField(1);
    this.numSequencesTf.getDocument()
      .addDocumentListener(new RunnableDocumentAdapter(this::numSequencesChanged));

    return new InputParameter("Number of sequences", this.numSequencesTf, "The desired number of sequences.");
  }

  private void numSequencesChanged() {
    this.transformationProvider.setNumSequences(this.numSequencesTf.getValue());
  }

  private InputParameter getIndependentExtractionsParameter() {
    this.independentExtractions = new JCheckBox("Independent extractions", false);
    this.independentExtractions.addItemListener(this::independentExtractionsChanged);

    return new InputParameter(
      "", this.independentExtractions,
      "<html>Whether independent extractions should be made or not. <br>This option can only be used with the <b>" +
        SequencesGroupSplitMode.SEQUENCES_PER_FILE_AND_FILES.toString() + "</b>. <br/>It is useful in combination with "
        + "the <b>randomization</b> in order to obtain different random subsets from the same input file.</html>"
    );
  }

  private void independentExtractionsChanged(ItemEvent event) {
    this.transformationProvider.setIndependentExtractions(this.independentExtractions.isSelected());
  }

  public SplitConfigurationTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(SplitConfigurationTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    this.randomize.setSelected(this.transformationProvider.isRandomize());
    this.randomSeedTf.setValue(this.transformationProvider.getRandomSeed());
    if (this.transformationProvider.getSplitMode() != null) {
      this.splitModePanel.setSelectedItem(this.transformationProvider.getSplitMode());
    }
    this.numFilesTf.setValue(this.transformationProvider.getNumFiles());
    this.numSequencesTf.setValue(this.transformationProvider.getNumSequences());
    this.independentExtractions.setSelected(this.transformationProvider.isIndependentExtractions());
  }
}
