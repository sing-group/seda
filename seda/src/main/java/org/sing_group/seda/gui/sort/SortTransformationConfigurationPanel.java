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
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_CRITERIA_DESCRIPTION;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_CRITERIA_HELP_GUI;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_DESCENDING_DESCRIPTION;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_DESCENDING_HELP_GUI;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_SORT_ON_DESCRIPTION;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_SORT_ON_HELP_GUI;

import java.awt.*;
import java.awt.event.ItemEvent;

import javax.swing.*;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.SequenceTarget;

public class SortTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private SortTransformationProvider transformationProvider;

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
      PARAM_SORT_ON_DESCRIPTION + ":", this.sequenceTargetRbtnPanel,
      PARAM_SORT_ON_HELP_GUI
    );
  }

  private void sequenceTargetItemListener(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.sequenceTargetChanged();
    }
  }

  private void sequenceTargetChanged() {
    this.transformationProvider.setSequenceTarget(sequenceTargetRbtnPanel.getSelectedItem().get());
  }

  private InputParameter getSortModeParameter() {
    this.descendingSort = new JCheckBox(PARAM_DESCENDING_DESCRIPTION, false);
    this.descendingSort.addItemListener((e) -> descendingSortChanged());

    return new InputParameter("", this.descendingSort, PARAM_DESCENDING_HELP_GUI);
  }

  private void descendingSortChanged() {
    this.transformationProvider.setDescendingSort(this.descendingSort.isSelected());
  }

  private InputParameter getSortCriteriaParameter() {
    this.sortCriteriaRbtnPanel =
      new RadioButtonsPanel<>(SequenceComparator.values(), 1, SequenceComparator.values().length);
    this.sortCriteriaRbtnPanel.addItemListener(this::sortCriteriaItemListener);

    return new InputParameter(
      PARAM_CRITERIA_DESCRIPTION + ":", this.sortCriteriaRbtnPanel, PARAM_CRITERIA_HELP_GUI
    );
  }

  private void sortCriteriaItemListener(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.sortCriteriaChanged();
    }
  }

  private void sortCriteriaChanged() {
    this.transformationProvider.setSequenceComparator(this.sortCriteriaRbtnPanel.getSelectedItem().get());
  }

  public SortTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  private void initTransformationProvider() {
    this.transformationProvider = new SortTransformationProvider();
    this.sequenceTargetChanged();
    this.sortCriteriaChanged();
    this.descendingSortChanged();
  }

  public void setTransformationProvider(SortTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    if (transformationProvider.getSequenceComparator() != null) {
      this.sortCriteriaRbtnPanel.setSelectedItem(this.transformationProvider.getSequenceComparator());
    }
    if (transformationProvider.getSequenceTarget() != null) {
      this.sequenceTargetRbtnPanel.setSelectedItem(this.transformationProvider.getSequenceTarget());
    }
    this.descendingSort.setSelected(this.transformationProvider.isDescendingSort());
  }
}
