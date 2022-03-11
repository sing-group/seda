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
package org.sing_group.seda.clustalomega.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_ADDITIONAL_PARAMETERS_DESCRIPTION;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_ADDITIONAL_PARAMETERS_HELP_GUI;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_NUM_THREADS_DESCRIPTION;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_NUM_THREADS_HELP_GUI;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.RunnableDocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;

public class ClustalOmegaAlignmentTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private JIntegerTextField numThreads;
  private JXTextField additionalParameters;
  private ClustalOmegaAlignmentTransformationProvider transformationProvider;
  private ClustalOmegaExecutionConfigurationPanel clustalOmegaExecutionConfigurationPanel;

  public ClustalOmegaAlignmentTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void initTransformationProvider() {
    this.transformationProvider = new ClustalOmegaAlignmentTransformationProvider();
    this.numThreadsChanged();
    this.additionalParametersChanged();
    this.clustalOmegaExecutorChanged();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, Y_AXIS));
    mainPanel.add(getQueryConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private InputParametersPanel getQueryConfigurationPanel() {
    return new InputParametersPanel(getParameters());
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<InputParameter>();
    parameters.add(getClustalOmegaExecutableParameter());
    parameters.add(getNumThreadsParameter());
    parameters.add(getAdditionalParametersParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getClustalOmegaExecutableParameter() {
    this.clustalOmegaExecutionConfigurationPanel =
      new ClustalOmegaExecutionConfigurationPanel(this::clustalOmegaExecutorChanged);

    return new InputParameter("", this.clustalOmegaExecutionConfigurationPanel, "The mode to execute Clustal Omega.");
  }

  private void clustalOmegaExecutorChanged(BinaryExecutionConfigurationPanel<ClustalOmegaBinariesExecutor> source) {
    this.clustalOmegaExecutorChanged();
  }

  private void clustalOmegaExecutorChanged() {
    invokeLater(() -> {
      this.transformationProvider.setBinariesExecutor(getClustalOmegaBinariesExecutor());
    });
  }

  private Optional<ClustalOmegaBinariesExecutor> getClustalOmegaBinariesExecutor() {
    return this.clustalOmegaExecutionConfigurationPanel.getBinariesExecutor();
  }

  private InputParameter getNumThreadsParameter() {
    this.numThreads = new JIntegerTextField(1);
    this.numThreads.setColumns(4);
    this.numThreads.getDocument()
      .addDocumentListener(new RunnableDocumentAdapter(this::numThreadsChanged));

    return new InputParameter(PARAM_NUM_THREADS_DESCRIPTION + ":", this.numThreads, PARAM_NUM_THREADS_HELP_GUI);
  }

  private void numThreadsChanged() {
    invokeLater(() -> {
      this.transformationProvider.setNumThreads(getNumThreads());
    });
  }

  private InputParameter getAdditionalParametersParameter() {
    this.additionalParameters = new JXTextField("Additional Clustal Omega parameters");
    this.additionalParameters.getDocument()
      .addDocumentListener(new RunnableDocumentAdapter(this::additionalParametersChanged));

    return new InputParameter(
      PARAM_ADDITIONAL_PARAMETERS_DESCRIPTION + ":", this.additionalParameters, PARAM_ADDITIONAL_PARAMETERS_HELP_GUI
    );
  }

  private void additionalParametersChanged() {
    invokeLater(() -> {
      this.transformationProvider.setAdditionalParameters(getAdditionalParameters());
    });
  }

  public ClustalOmegaAlignmentTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  private int getNumThreads() {
    return this.numThreads.getValue();
  }

  private String getAdditionalParameters() {
    return this.additionalParameters.getText();
  }

  public void setTransformationProvider(ClustalOmegaAlignmentTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    this.numThreads.setValue(this.transformationProvider.getNumThreads());
    if (this.transformationProvider.getAdditionalParameters() != null) {
      this.additionalParameters.setText(this.transformationProvider.getAdditionalParameters());
    }
    if (this.transformationProvider.getBinariesExecutor() != null) {
      this.clustalOmegaExecutionConfigurationPanel
        .setBinariesExecutor(this.transformationProvider.getBinariesExecutor());
    }
  }
}