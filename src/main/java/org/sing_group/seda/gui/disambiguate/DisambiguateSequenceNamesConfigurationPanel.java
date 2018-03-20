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
package org.sing_group.seda.gui.disambiguate;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;

import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.DisambiguateSequenceNamesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.DisambiguateSequenceNamesTransformation.Mode;

public class DisambiguateSequenceNamesConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String HELP_MODE = "<html>The method to disambiguate sequences with duplicated identifiers."
    + "<ul><li>Rename: add a numeric prefix to disambiguate duplicate identifiers.</li>"
    + "<li>Remove: remove sequences with duplicate identifiers, keeping the first occurrence.</li>"
    + "</ul></html>";
  
  private DisambiguateSequenceNamesConfigurationModel model;
  private RadioButtonsPanel<DisambiguateSequenceNamesTransformation.Mode> modeRadioButtons;

  public DisambiguateSequenceNamesConfigurationPanel() {
    this.init();
    this.model = new DisambiguateSequenceNamesConfigurationModel();
    this.model.setMode(getSelectedMode());
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new InputParametersPanel(getParameters());

    return new CenteredJPanel(mainPanel);
  }

  private InputParameter[] getParameters() {
    InputParameter[] parameters = new InputParameter[1];
    parameters[0] = getModeParameter();

    return parameters;
  }

  private InputParameter getModeParameter() {
    this.modeRadioButtons = new RadioButtonsPanel<>(DisambiguateSequenceNamesTransformation.Mode.values(), 1, 2);
    this.modeRadioButtons.addItemListener(this::modeChanged);

    return new InputParameter("Disambiguation mode: ", this.modeRadioButtons, HELP_MODE);
  }

  private void modeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.model.setMode(getSelectedMode());
    }
  }

  private Mode getSelectedMode() {
    return this.modeRadioButtons.getSelectedItem().get();
  }

  public TransformationProvider getModel() {
    return this.model;
  }
}