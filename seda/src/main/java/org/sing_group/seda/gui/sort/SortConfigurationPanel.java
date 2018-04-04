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
package org.sing_group.seda.gui.sort;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class SortConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private final SortConfigurationModel sortModel;

  private RadioButtonsPanel<SequenceTarget> sequenceTargetRbtnPanel;
  private RadioButtonsPanel<SequenceComparator> sortCriteriaRbtnPanel;
  private JCheckBox descendingSort;

  public SortConfigurationPanel() {
    this.init();
    this.sortModel = new SortConfigurationModel(sequenceTargetRbtnPanel, descendingSort, sortCriteriaRbtnPanel);
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
    InputParameter[] parameters = new InputParameter[3];
    parameters[0] = getSequenceTargetParameter();
    parameters[1] = getSortModeParameter();
    parameters[2] = getSortCriteriaParameter();

    return parameters;
  }

  private InputParameter getSequenceTargetParameter() {
    sequenceTargetRbtnPanel = new RadioButtonsPanel<>(SequenceTarget.values(), 1, SequenceTarget.values().length);

    return new InputParameter(
      "Sort on:", sequenceTargetRbtnPanel, "Whether sort must be applied on sequence headers or sequences themselves."
    );
  }

  private InputParameter getSortModeParameter() {
    descendingSort = new JCheckBox("Descending", false);

    return new InputParameter("", descendingSort, "Use this option to sort in descending order.");
  }

  private InputParameter getSortCriteriaParameter() {
    sortCriteriaRbtnPanel = new RadioButtonsPanel<>(SequenceComparator.values(), 1, SequenceComparator.values().length);

    return new InputParameter("Criteria:", sortCriteriaRbtnPanel, "The sort criteria to be applied.");
  }

  public TransformationProvider getModel() {
    return this.sortModel;
  }
}
