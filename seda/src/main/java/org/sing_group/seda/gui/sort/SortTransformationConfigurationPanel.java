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
package org.sing_group.seda.gui.sort;

import static java.awt.BorderLayout.CENTER;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class SortTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private SortTransformationProvider sortTransformationProvider;

  private RadioButtonsPanel<SequenceTarget> sequenceTargetRbtnPanel;
  private RadioButtonsPanel<SequenceComparator> sortCriteriaRbtnPanel;
  private JCheckBox descendingSort;

  public SortTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    return new CenteredJPanel(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    InputParameter[] parameters = new InputParameter[3];
    parameters[0] = getSequenceTargetParameter();
    parameters[1] = getSortModeParameter();
    parameters[2] = getSortCriteriaParameter();

    return parameters;
  }

  private InputParameter getSequenceTargetParameter() {
    this.sequenceTargetRbtnPanel = new RadioButtonsPanel<>(SequenceTarget.values(), 1, SequenceTarget.values().length);
    this.sequenceTargetRbtnPanel.addItemListener(this::sequenceTargetItemListener);

    return new InputParameter(
      "Sort on:", this.sequenceTargetRbtnPanel, "Whether sort must be applied on sequence headers or sequences themselves."
    );
  }

  private void sequenceTargetItemListener(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.sequenceTargetChanged();
    }
  }

  private void sequenceTargetChanged() {
    this.sortTransformationProvider.setSequenceTarget(sequenceTargetRbtnPanel.getSelectedItem().get());
  }

  private InputParameter getSortModeParameter() {
    this.descendingSort = new JCheckBox("Descending", false);
    this.descendingSort.addItemListener((e) -> descendingSortChanged());

    return new InputParameter("", this.descendingSort, "Use this option to sort in descending order.");
  }

  private void descendingSortChanged() {
    this.sortTransformationProvider.setDescendingSort(this.descendingSort.isSelected());
  }

  private InputParameter getSortCriteriaParameter() {
    this.sortCriteriaRbtnPanel =
      new RadioButtonsPanel<>(SequenceComparator.values(), 1, SequenceComparator.values().length);
    this.sortCriteriaRbtnPanel.addItemListener(this::sortCriteriaItemListener);

    return new InputParameter("Criteria:", this.sortCriteriaRbtnPanel, "The sort criteria to be applied.");
  }

  private void sortCriteriaItemListener(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.sortCriteriaChanged();
    }
  }

  private void sortCriteriaChanged() {
    this.sortTransformationProvider.setSequenceComparator(this.sortCriteriaRbtnPanel.getSelectedItem().get());
  }

  public TransformationProvider getTransformationProvider() {
    return this.sortTransformationProvider;
  }

  private void initTransformationProvider() {
    this.sortTransformationProvider = new SortTransformationProvider();
    this.sequenceTargetChanged();
    this.sortCriteriaChanged();
    this.descendingSortChanged();
  }
}
