/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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
package org.sing_group.seda.blast.uniprot.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_ALIGNMENT_CUTOFF;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_EXPECTATION_OPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_FILTER_OPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_MATRIX_OPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_OUTPUT_TYPE;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_DATABASE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_DATABASE_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_EXPECTATION_VALUE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_EXPECTATION_VALUE_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_FILTER_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_FILTER_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_GAPPED_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_GAPPED_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_HITS_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_HITS_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_MATRIX_OPTION_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_MATRIX_OPTION_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_OUTPUT_TYPE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_OUTPUT_TYPE_HELP_GUI;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;
import org.sing_group.seda.blast.transformation.provider.uniprot.UniProtBlastTransformationProvider;

import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.AlignmentCutoffOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.DatabaseOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.ExpectationOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.FilterOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.MatrixOption;

public class UniProtBlastTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private UniProtBlastTransformationProvider transformationProvider;
  private RadioButtonsPanel<OutputTypeParameter> outputTypeParameter;
  private JComboBox<DatabaseOption> databaseCombobox;
  private JComboBox<ExpectValueOption> expectValue;
  private JComboBox<MatrixOption> matrixCombobox;
  private RadioButtonsPanel<FilterOption> filterRadioButtonsPanel;
  private JCheckBox gapped;
  private JComboBox<HitOption> hitsCombobox;

  public UniProtBlastTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void initTransformationProvider() {
    this.transformationProvider =
      new UniProtBlastTransformationProvider(
        this.getDatabase(), this.getOutputType(), this.getExpectValue(),
        this.getMatrix(), this.getFilter(), this.isGapped(), this.getHits()
      );
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    return new CenteredJPanel(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    List<InputParameter> dbParameters = new LinkedList<InputParameter>();
    dbParameters.add(getDatabaseParameter());
    dbParameters.add(getOutputTypeParameter());
    dbParameters.add(getExpectValueParameter());
    dbParameters.add(getMatrixParameter());
    dbParameters.add(getFilterParameter());
    dbParameters.add(getIsGappedParameter());
    dbParameters.add(getHitListSizeParameter());

    return dbParameters.toArray(new InputParameter[dbParameters.size()]);
  }

  private InputParameter getDatabaseParameter() {
    this.databaseCombobox = new JComboBox<>(DatabaseOption.values());
    this.databaseCombobox.addItemListener(this::blastDatabaseChanged);

    return new InputParameter(PARAM_DATABASE_DESCRIPTION + ": ", databaseCombobox, PARAM_DATABASE_HELP_GUI);
  }

  private void blastDatabaseChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setDatabase(getDatabase());
      });
    }
  }

  private DatabaseOption getDatabase() {
    return (DatabaseOption) this.databaseCombobox.getSelectedItem();
  }

  private InputParameter getOutputTypeParameter() {
    this.outputTypeParameter = new RadioButtonsPanel<>(OutputTypeParameter.values(), 1, 2);
    this.outputTypeParameter.setSelectedItem(DEFAULT_OUTPUT_TYPE);
    this.outputTypeParameter.addItemListener(this::outputTypeChanged);

    return new InputParameter(
      PARAM_OUTPUT_TYPE_DESCRIPTION + ": ", this.outputTypeParameter, PARAM_OUTPUT_TYPE_HELP_GUI
    );
  }

  private void outputTypeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setOutputType(getOutputType());
      });
    }
  }

  private OutputTypeParameter getOutputType() {
    return this.outputTypeParameter.getSelectedItem().get();
  }

  private InputParameter getExpectValueParameter() {
    this.expectValue = new JComboBox<>(getExpectValues());
    this.expectValue.setSelectedItem(new ExpectValueOption(DEFAULT_EXPECTATION_OPTION));
    this.expectValue.addItemListener(this::expectValueChanged);

    return new InputParameter(
      PARAM_EXPECTATION_VALUE_DESCRIPTION + ":", this.expectValue, PARAM_EXPECTATION_VALUE_HELP_GUI
    );
  }

  private ExpectValueOption[] getExpectValues() {
    ExpectValueOption[] toret = new ExpectValueOption[ExpectationOption.values().length];
    for (int i = 0; i < toret.length; i++) {
      toret[i] = new ExpectValueOption(ExpectationOption.values()[i]);
    }

    return toret;
  }

  private void expectValueChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setExpectation(getExpectValue());
      });
    }
  }

  private ExpectationOption getExpectValue() {
    return ((ExpectValueOption) this.expectValue.getSelectedItem()).getExpectationOption();
  }

  private InputParameter getMatrixParameter() {
    this.matrixCombobox = new JComboBox<>(MatrixOption.values());
    this.matrixCombobox.setSelectedItem(DEFAULT_MATRIX_OPTION);
    this.matrixCombobox.addItemListener(this::matrixParameterChanged);

    return new InputParameter(
      PARAM_MATRIX_OPTION_DESCRIPTION + ": ", this.matrixCombobox, PARAM_MATRIX_OPTION_HELP_GUI
    );
  }

  private void matrixParameterChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setMatrix(getMatrix());
      });
    }
  }

  private MatrixOption getMatrix() {
    return (MatrixOption) this.matrixCombobox.getSelectedItem();
  }

  private InputParameter getFilterParameter() {
    this.filterRadioButtonsPanel = new RadioButtonsPanel<>(FilterOption.values(), 1, 2);
    this.filterRadioButtonsPanel.setSelectedItem(DEFAULT_FILTER_OPTION);
    this.filterRadioButtonsPanel.addItemListener(this::filtereParameterChanged);

    return new InputParameter(PARAM_FILTER_DESCRIPTION + ": ", this.filterRadioButtonsPanel, PARAM_FILTER_HELP_GUI);
  }

  private void filtereParameterChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setFilter(getFilter());
      });
    }
  }

  private FilterOption getFilter() {
    return this.filterRadioButtonsPanel.getSelectedItem().get();
  }

  private InputParameter getIsGappedParameter() {
    this.gapped = new JCheckBox();
    this.gapped.setSelected(true);
    this.gapped.addItemListener(this::gappedChanged);

    return new InputParameter(PARAM_GAPPED_DESCRIPTION + ": ", gapped, PARAM_GAPPED_HELP_GUI);
  }

  private void gappedChanged(ItemEvent event) {
    invokeLater(() -> {
      this.transformationProvider.setGapped(isGapped());
    });
  }

  private boolean isGapped() {
    return this.gapped.isSelected();
  }

  private InputParameter getHitListSizeParameter() {
    this.hitsCombobox = new JComboBox<>(getHitValues());
    this.hitsCombobox.setSelectedItem(new HitOption(DEFAULT_ALIGNMENT_CUTOFF));
    this.hitsCombobox.addItemListener(this::hitsChanged);

    return new InputParameter(PARAM_HITS_DESCRIPTION + ": ", this.hitsCombobox, PARAM_HITS_HELP_GUI);
  }

  public HitOption[] getHitValues() {
    HitOption[] toret = new HitOption[AlignmentCutoffOption.values().length];
    for (int i = 0; i < toret.length; i++) {
      toret[i] = new HitOption(AlignmentCutoffOption.values()[i]);
    }

    return toret;
  }

  private void hitsChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setHits(getHits());
      });
    }
  }

  private AlignmentCutoffOption getHits() {
    return ((HitOption) this.hitsCombobox.getSelectedItem()).getCutoff();
  }

  public UniProtBlastTransformationProvider getTransformationProvider() {
    return transformationProvider;
  }

  public void setTransformationProvider(UniProtBlastTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    DatabaseOption database = this.transformationProvider.getDatabase();
    if (database != null) {
      this.databaseCombobox.setSelectedItem(database);
    }

    OutputTypeParameter outputType = this.transformationProvider.getOutputType();
    if (outputType != null) {
      this.outputTypeParameter.setSelectedItem(outputType);
    }

    ExpectationOption expectValue = this.transformationProvider.getExpectValue();
    if (expectValue != null) {
      this.expectValue.setSelectedItem(new ExpectValueOption(expectValue));
    }

    FilterOption filter = this.transformationProvider.getFilter();
    if (filter != null) {
      this.filterRadioButtonsPanel.setSelectedItem(filter);
    }

    AlignmentCutoffOption hits = this.transformationProvider.getHits();
    if (hits != null) {
      this.hitsCombobox.setSelectedItem(new HitOption(hits));
    }

    MatrixOption matrix = this.transformationProvider.getMatrix();
    if (matrix != null) {
      this.matrixCombobox.setSelectedItem(matrix);
    }

    this.gapped.setSelected(this.transformationProvider.isGapped());
  }
}
