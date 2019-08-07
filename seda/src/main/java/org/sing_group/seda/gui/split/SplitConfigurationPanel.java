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
package org.sing_group.seda.gui.split;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.split.SequencesGroupSplitMode;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class SplitConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private SplitConfigurationModel splitModel;

  private RadioButtonsPanel<SequencesGroupSplitMode> splitModePanel;
  private JCheckBox randomize;
  private JIntegerTextField randomSeedTf;
  private JCheckBox independentExtractions;
  private JIntegerTextField numberOfFilesTf;
  private JIntegerTextField numberOfSequencesTf;

  public SplitConfigurationPanel() {
    this.init();
    this.splitModel =
      new SplitConfigurationModel(splitModePanel, randomize, randomSeedTf, independentExtractions, numberOfFilesTf, numberOfSequencesTf);
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);

    this.checkTextFieldsStatus();
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new InputParametersPanel(getParameters());

    return new CenteredJPanel(mainPanel);
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getRandomizeParameter());
    parameters.add(getRandomSeedParameter());
    parameters.add(getSplitModeParameter());
    parameters.add(getNumberOfFilesParameter());
    parameters.add(getNumberOfSequencesParameter());
    parameters.add(getIndependentExtractionsParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getSplitModeParameter() {
    splitModePanel =
      new RadioButtonsPanel<>(SequencesGroupSplitMode.values(), SequencesGroupSplitMode.values().length, 1);
    splitModePanel.addItemListener(this::splitModeChanged);

    return new InputParameter(
      "Split mode:", splitModePanel, "Split mode."
    );
  }

  private void splitModeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      checkTextFieldsStatus();
    }
  }

  private void checkTextFieldsStatus() {
    switch (this.splitModePanel.getSelectedItem().get()) {
      case FIXED_FILES:
        this.independentExtractions.setEnabled(false);
        this.numberOfFilesTf.setEditable(true);
        this.numberOfFilesTf.setEnabled(true);
        this.numberOfSequencesTf.setEditable(false);
        this.numberOfSequencesTf.setEnabled(false);
        break;
      case FIXED_SEQUENCES_PER_FILE:
        this.independentExtractions.setEnabled(false);
        this.numberOfFilesTf.setEditable(false);
        this.numberOfFilesTf.setEnabled(false);
        this.numberOfSequencesTf.setEditable(true);
        this.numberOfSequencesTf.setEnabled(true);
        break;
      case SEQUENCES_PER_FILE_AND_FILES:
        this.independentExtractions.setEnabled(true);
        this.numberOfFilesTf.setEditable(true);
        this.numberOfFilesTf.setEnabled(true);
        this.numberOfSequencesTf.setEditable(true);
        this.numberOfSequencesTf.setEnabled(true);
        break;
      default:
        break;
    }
  }

  private InputParameter getRandomizeParameter() {
    randomize = new JCheckBox("Randomize", false);
    randomize.addItemListener(this::randomizeChanged);

    return new InputParameter("", randomize, "Whether sequences must be randomized or not.");
  }

  private void randomizeChanged(ItemEvent event) {
    this.randomSeedTf.setEnabled(event.getStateChange() == ItemEvent.SELECTED);
  }

  private InputParameter getRandomSeedParameter() {
    randomSeedTf = new JIntegerTextField(1);
    randomSeedTf.setEnabled(this.randomize.isSelected());

    return new InputParameter("Seed:", randomSeedTf, "<html>The random seed to randomize the sequences. <br/>"
      + "This allows the same result to be reproduced in different runs and environments with same random seed.</html>");
  }

  private InputParameter getNumberOfFilesParameter() {
    numberOfFilesTf = new JIntegerTextField(1);

    return new InputParameter("Number of files", numberOfFilesTf, "The desired number of files.");
  }

  private InputParameter getNumberOfSequencesParameter() {
    numberOfSequencesTf = new JIntegerTextField(1);

    return new InputParameter("Number of sequences", numberOfSequencesTf, "The desired number of sequences.");
  }

  private InputParameter getIndependentExtractionsParameter() {
    independentExtractions = new JCheckBox("Independent extractions", false);
    
    return new InputParameter("", independentExtractions,
      "<html>Whether independent extractions should be made or not. <br>This option can only be used with the <b>" +
        SequencesGroupSplitMode.SEQUENCES_PER_FILE_AND_FILES.toString() + "</b>. <br/>It is useful in combination with "
        + "the <b>randomization</b> in order to obtain different random subsets from the same input file.</html>"
    );
  }

  public TransformationProvider getModel() {
    return this.splitModel;
  }
}
