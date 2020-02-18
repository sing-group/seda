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
package org.sing_group.seda.gui.grow;

import static org.sing_group.seda.gui.GuiUtils.bindIntegerTextField;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class GrowSequencesConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private GrowSequencesConfigurationModel model;

  private JIntegerTextField minimumOverlappingTf;

  public GrowSequencesConfigurationPanel() {
    this.model = new GrowSequencesConfigurationModel();
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
    this.model.addTransformationChangeListener(this::modelChanged);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new InputParametersPanel(getParameters());

    return new CenteredJPanel(mainPanel);
  }

  private InputParameter[] getParameters() {
    InputParameter[] parameters = new InputParameter[1];
    parameters[0] = getNumberOfSequencesParameter();

    return parameters;
  }

  private InputParameter getNumberOfSequencesParameter() {
    minimumOverlappingTf = new JIntegerTextField(this.model.getMinimumOverlapping());
    minimumOverlappingTf.setColumns(10);
    bindIntegerTextField(minimumOverlappingTf, this.model::setMinimumOverlapping);

    return new InputParameter(
      "Minimum overlapping: ", minimumOverlappingTf, "The minimum overlapping to merge two sequences."
    );
  }

  private void modelChanged(TransformationChangeEvent event) {
    SwingUtilities.invokeLater(
      () -> {
        switch ((GrowSequencesConfigurationChangeType) event.getType()) {
          case MINIMUM_OVERLAPPING_CHANGED:
            updateMinimumOverlapping();
            break;
        }
      }
    );
  }

  private void updateMinimumOverlapping() {
    this.minimumOverlappingTf.setValue(this.model.getMinimumOverlapping());
  }

  public TransformationProvider getModel() {
    return this.model;
  }
}