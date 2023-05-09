/*
 * #%L
 * SEquence DAtaset builder EMBOSS plugin
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
package org.sing_group.seda.emboss.getorf.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.DEFAULT_MAX_SIZE;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.DEFAULT_MIN_SIZE;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_DESCRIPTION;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_HELP_GUI;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_FIND_DESCRIPTION;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_FIND_HELP_GUI;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MAX_SIZE_DESCRIPTION;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MAX_SIZE_HELP_GUI;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MIN_SIZE_DESCRIPTION;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MIN_SIZE_HELP_GUI;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_TABLE_DESCRIPTION;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_TABLE_HELP_GUI;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.RunnableDocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.emboss.execution.EmbossBinariesExecutor;
import org.sing_group.seda.emboss.getorf.datatype.FindParam;
import org.sing_group.seda.emboss.getorf.datatype.TableParam;
import org.sing_group.seda.emboss.gui.EmbossExecutionConfigurationPanel;
import org.sing_group.seda.emboss.transformation.provider.GetOrfTransformationProvider;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;

public class GetOrfTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private GetOrfTransformationProvider transformationProvider;
  private JComboBox<TableParam> tableCombobox;
  private JComboBox<FindParam> findCombobox;
  private JIntegerTextField minSize;
  private JIntegerTextField maxSize;
  private JXTextField additionalParameters;
  private EmbossExecutionConfigurationPanel embossExecutionConfigurationPanel;

  public GetOrfTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void initTransformationProvider() {
    this.transformationProvider =
      new GetOrfTransformationProvider(
        (TableParam) this.tableCombobox.getSelectedItem(),
        (FindParam) this.findCombobox.getSelectedItem(),
        this.minSize.getValue(),
        this.maxSize.getValue(),
        this.additionalParameters.getText()
      );
    this.embossExecutorChanged();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, Y_AXIS));

    mainPanel.add(getEmbossConfigurationPanel());
    mainPanel.add(getOperationConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private InputParametersPanel getEmbossConfigurationPanel() {
    InputParametersPanel embossConfigurationPanel = new InputParametersPanel(getEmbossConfigurationParameters());
    embossConfigurationPanel.setBorder(createTitledBorder("EMBOSS configuration"));

    return embossConfigurationPanel;
  }

  private InputParameter[] getEmbossConfigurationParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getEmbossExecutableParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getEmbossExecutableParameter() {
    this.embossExecutionConfigurationPanel = new EmbossExecutionConfigurationPanel(this::embossExecutorChanged);

    return new InputParameter("", embossExecutionConfigurationPanel, "The mode to execute EMBOSS.");
  }

  private void embossExecutorChanged(BinaryExecutionConfigurationPanel<EmbossBinariesExecutor> source) {
    this.embossExecutorChanged();
  }

  private void embossExecutorChanged() {
    invokeLater(() -> {
      this.transformationProvider
        .setEmbossBinariesExecutor(this.embossExecutionConfigurationPanel.getBinariesExecutor());
    });
  }

  private InputParametersPanel getOperationConfigurationPanel() {
    return new InputParametersPanel(getParameters());
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<InputParameter>();
    parameters.add(getTableParameter());
    parameters.add(getFindParameter());
    parameters.add(getMinSizeParameter());
    parameters.add(getMaxSizeParameter());
    parameters.add(getAdditionalParamsParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getTableParameter() {
    this.tableCombobox = new JComboBox<>(TableParam.values());
    this.tableCombobox.addItemListener(this::tableChanged);

    return new InputParameter(PARAM_TABLE_DESCRIPTION + ": ", this.tableCombobox, PARAM_TABLE_HELP_GUI);
  }

  private void tableChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.tableChanged();
    }
  }

  private void tableChanged() {
    invokeLater(() -> {
      this.transformationProvider.setTable((TableParam) this.tableCombobox.getSelectedItem());
    });
  }

  private InputParameter getFindParameter() {
    this.findCombobox = new JComboBox<>(FindParam.values());
    this.findCombobox.addItemListener(this::findChanged);

    return new InputParameter(PARAM_FIND_DESCRIPTION + ": ", this.findCombobox, PARAM_FIND_HELP_GUI);
  }

  private void findChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.findChanged();
    }
  }

  private void findChanged() {
    invokeLater(() -> {
      this.transformationProvider.setFind((FindParam) this.findCombobox.getSelectedItem());
    });
  }

  private InputParameter getMinSizeParameter() {
    this.minSize = new JIntegerTextField(DEFAULT_MIN_SIZE);
    this.minSize.getDocument()
      .addDocumentListener(new RunnableDocumentAdapter(() -> this.minSizeChanged()));

    return new InputParameter(PARAM_MIN_SIZE_DESCRIPTION + ": ", this.minSize, PARAM_MIN_SIZE_HELP_GUI);
  }

  private void minSizeChanged() {
    invokeLater(() -> {
      this.transformationProvider.setMinSize(this.minSize.getValue());
    });
  }

  private InputParameter getMaxSizeParameter() {
    this.maxSize = new JIntegerTextField(DEFAULT_MAX_SIZE);
    this.maxSize.getDocument()
      .addDocumentListener(new RunnableDocumentAdapter(() -> this.maxSizeChanged()));

    return new InputParameter(PARAM_MAX_SIZE_DESCRIPTION + ": ", this.maxSize, PARAM_MAX_SIZE_HELP_GUI);
  }

  private void maxSizeChanged() {
    invokeLater(() -> {
      this.transformationProvider.setMaxSize(this.maxSize.getValue());
    });
  }

  private InputParameter getAdditionalParamsParameter() {
    this.additionalParameters = new JXTextField("Additional parameters for getorf");
    this.additionalParameters.getDocument()
      .addDocumentListener(new RunnableDocumentAdapter(() -> this.getOrfAdditionalParametersChanged()));

    return new InputParameter(PARAM_ADDITIONAL_PARAMS_DESCRIPTION + ": ", this.additionalParameters, PARAM_ADDITIONAL_PARAMS_HELP_GUI);
  }

  private void getOrfAdditionalParametersChanged() {
    invokeLater(() -> {
      this.transformationProvider.setAdditionalParameters(this.additionalParameters.getText());
    });
  }

  public GetOrfTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(GetOrfTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    if (this.transformationProvider.getTable() != null) {
      this.tableCombobox.setSelectedItem(this.transformationProvider.getTable());
    }
    if (this.transformationProvider.getFind() != null) {
      this.tableCombobox.setSelectedItem(this.transformationProvider.getFind());
    }
    this.minSize.setValue(this.transformationProvider.getMinSize());
    this.maxSize.setValue(this.transformationProvider.getMaxSize());
    this.additionalParameters.setText(this.transformationProvider.getAdditionalParameters());

    if (this.transformationProvider.getEmbossBinariesExecutor() != null) {
      this.embossExecutionConfigurationPanel
        .setBinariesExecutor(this.transformationProvider.getEmbossBinariesExecutor());
    }
  }
}
