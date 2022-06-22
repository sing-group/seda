/*
 * #%L
 * SEquence DAtaset builder CGA Omega plugin
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.cga.gui;

import static java.awt.BorderLayout.CENTER;
import static java.util.Arrays.asList;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.event.RunnableDocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.cga.execution.CgaBinariesExecutor;
import org.sing_group.seda.cga.execution.DefaultDockerCgaBinariesExecutor;
import org.sing_group.seda.cga.transformation.dataset.CgaResults;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;

public class CgaPipelineTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_CGA_IMAGE =
    "<html>The CGA Docker image.<br/> By default, the official pegi3s/cga image is used.<br/>"
      + "It is not recommended changing it.</html>";
  private static final String HELP_REFERENCE_FASTA = "FASTA file containing the reference sequence.";
  private static final String HELP_CGA_RESULTS = "The CGA results to collect.";
  private static final String HELP_COMPI_TASKS = "The maximum number of parallell tasks that the Compi pipeline may execute.";

  private CgaPipelineTransformationProvider transformationProvider;
  private DockerExecutionConfigurationPanel cgaExecutionConfigurationPanel;
  private JFileChooserPanel referenceFasta;
  private CgaCompiPipelineConfigurationParameters cgaCompiPipelineConfigurationParameters;
  private JComboBox<CgaResults> cgaResults;
  private JIntegerTextField compiTasks;

  public CgaPipelineTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void initTransformationProvider() {
    this.transformationProvider = new CgaPipelineTransformationProvider();
    this.cgaExecutorChanged();
    this.cgaCompiPipelineConfigurationChanged();
    this.referenceFastaChanged();
    this.cgaResultsChanged();
    this.compiTasksChanged();
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
    parameters.add(getSplignCompartExecutableParameter());
    parameters.add(getGenomeQueryFileParameter());
    parameters.addAll(getCgaCompiPipelineConfigurationParameters());
    parameters.add(getCgaResultsParameter());
    parameters.add(getCompiTasksParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getSplignCompartExecutableParameter() {
    this.cgaExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    this.cgaExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this::cgaExecutorChanged);

    return new InputParameter(
      "CGA Docker image:", this.cgaExecutionConfigurationPanel, HELP_CGA_IMAGE
    );
  }

  private void cgaExecutorChanged(BinaryExecutionConfigurationPanel<CgaBinariesExecutor> source) {
    this.cgaExecutorChanged();
  }

  private void cgaExecutorChanged() {
    invokeLater(() -> {
      this.transformationProvider.setCgaBinariesExecutor(this.cgaExecutionConfigurationPanel.getBinariesExecutor());
    });
  }

  private InputParameter getGenomeQueryFileParameter() {
    this.referenceFasta = JFileChooserPanelBuilder.createOpenJFileChooserPanel()
      .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
      .withFileChooserSelectionMode(SelectionMode.FILES).withLabel("").withClearSelectedFileOnShow(true).build();
    this.referenceFasta.addFileChooserListener(e -> this.referenceFastaChanged());

    return new InputParameter("Reference FASTA file:", this.referenceFasta, HELP_REFERENCE_FASTA);
  }

  private void referenceFastaChanged() {
    invokeLater(() -> {
      if (this.referenceFasta.getSelectedFile() != null) {
        this.transformationProvider.setReferenceFasta(this.referenceFasta.getSelectedFile());
      } else {
        this.transformationProvider.clearQueryFile();
      }
    });
  }

  private List<InputParameter> getCgaCompiPipelineConfigurationParameters() {
    this.cgaCompiPipelineConfigurationParameters = new CgaCompiPipelineConfigurationParameters(
      this::cgaCompiPipelineConfigurationChanged
    );

    return asList(this.cgaCompiPipelineConfigurationParameters.getParameters());
  }

  private void cgaCompiPipelineConfigurationChanged() {
    invokeLater(() -> {
      this.transformationProvider
        .setCgaCompiPipelineConfiguration(this.cgaCompiPipelineConfigurationParameters.getConfiguration());
    });
  }

  private InputParameter getCgaResultsParameter() {
    this.cgaResults = new JComboBox<CgaResults>(CgaResults.values());
    this.cgaResults.addItemListener(this::cgaResultsChanged);

    return new InputParameter("Results:", this.cgaResults, HELP_CGA_RESULTS);
  }

  private void cgaResultsChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.cgaResultsChanged();
    }
  }

  private void cgaResultsChanged() {
    invokeLater(() -> {
      this.transformationProvider.setCgaResults((CgaResults) this.cgaResults.getSelectedItem());
    });
  }

  private InputParameter getCompiTasksParameter() {
    this.compiTasks = new JIntegerTextField(4);
    this.compiTasks.getDocument().addDocumentListener(new RunnableDocumentAdapter(this::compiTasksChanged));

    return new InputParameter("Parallell tasks:", this.compiTasks, HELP_COMPI_TASKS);
  }

  private void compiTasksChanged() {
    invokeLater(() -> {
      this.transformationProvider.setCompiTasks(this.compiTasks.getValue());
    });
  }

  public CgaPipelineTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(CgaPipelineTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    if (this.transformationProvider.getreferenceFasta() != null) {
      this.referenceFasta.setSelectedFile(this.transformationProvider.getreferenceFasta());
    } else {
      this.transformationProvider.clearQueryFile();
    }

    if (this.transformationProvider.getcgaBinariesExecutor() != null) {
      this.cgaExecutionConfigurationPanel.setSelectedDockerImage(
        ((DefaultDockerCgaBinariesExecutor) this.transformationProvider.getcgaBinariesExecutor()).getDockerImage()
      );
    }

    if (this.transformationProvider.getCgaResults() != null) {
      this.cgaResults.setSelectedItem(this.transformationProvider.getCgaResults());
    }

    this.compiTasks.setValue(this.transformationProvider.getCompiTasks());

    if (this.transformationProvider.getCgaCompiPipelineConfiguration() != null) {
      this.cgaCompiPipelineConfigurationParameters
        .setConfiguration(this.transformationProvider.getCgaCompiPipelineConfiguration());
    }
  }
}
