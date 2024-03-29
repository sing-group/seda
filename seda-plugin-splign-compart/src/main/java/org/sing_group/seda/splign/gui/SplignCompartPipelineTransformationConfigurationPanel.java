/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_CONCATENATE_EXONS_DESCRIPTION;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_CONCATENATE_EXONS_HELP_GUI;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_EXTERNAL_QUERY_FILE_DESCRIPTION;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_EXTERNAL_QUERY_FILE_HELP_GUI;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.bedtools.gui.BedToolsExecutionConfigurationPanel;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.gui.BlastExecutionConfigurationPanel;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.splign.execution.SplignCompartBinariesExecutor;
import org.sing_group.seda.splign.transformation.provider.SplignCompartPipelineTransformationProvider;

public class SplignCompartPipelineTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private JFileChooserPanel fileQuery;
  private JCheckBox concatenateExons;
  private SplignCompartExecutionConfigurationPanel splignCompartExecutionConfigurationPanel;
  private BlastExecutionConfigurationPanel blastExecutionConfigurationPanel;
  private BedToolsExecutionConfigurationPanel bedToolsExecutionConfigurationPanel;
  private SplignCompartPipelineTransformationProvider transformationProvider;

  public SplignCompartPipelineTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void initTransformationProvider() {
    this.transformationProvider = new SplignCompartPipelineTransformationProvider();
    this.splignCompartExecutorChanged();
    this.blastExecutorChanged();
    this.bedToolsExecutorChanged();
    this.fileQueryChanged();
    this.concatenateExonsChanged();
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
    parameters.add(getBlastExecutableParameter());
    parameters.add(getBedToolsExecutableParameter());
    parameters.add(getGenomeQueryFileParameter());
    parameters.add(getConcatenateExonsParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getSplignCompartExecutableParameter() {
    this.splignCompartExecutionConfigurationPanel =
      new SplignCompartExecutionConfigurationPanel(this::splignCompartExecutorChanged);

    return new InputParameter("", this.splignCompartExecutionConfigurationPanel, "The mode to execute Splign/Compart.");
  }

  private void splignCompartExecutorChanged(BinaryExecutionConfigurationPanel<SplignCompartBinariesExecutor> source) {
    this.splignCompartExecutorChanged();
  }

  private void splignCompartExecutorChanged() {
    invokeLater(() -> {
      this.transformationProvider
        .setSplignCompartBinariesExecutor(this.splignCompartExecutionConfigurationPanel.getBinariesExecutor());
    });
  }

  private InputParameter getBlastExecutableParameter() {
    this.blastExecutionConfigurationPanel = new BlastExecutionConfigurationPanel(this::blastExecutorChanged);
    this.blastExecutionConfigurationPanel.setBorder(createTitledBorder("BLAST configuration"));

    return new InputParameter("", blastExecutionConfigurationPanel, "The mode to execute BLAST.");
  }
  
  private void blastExecutorChanged(BinaryExecutionConfigurationPanel<BlastBinariesExecutor> source) {
    this.blastExecutorChanged();
  }

  private void blastExecutorChanged() {
    invokeLater(() -> {
      this.transformationProvider
        .setBlastBinariesExecutor(this.blastExecutionConfigurationPanel.getBinariesExecutor());
    });
  }

  private InputParameter getBedToolsExecutableParameter() {
    this.bedToolsExecutionConfigurationPanel = new BedToolsExecutionConfigurationPanel(this::bedToolsExecutorChanged);
    this.bedToolsExecutionConfigurationPanel.setBorder(createTitledBorder("bedtools configuration"));
    
    return new InputParameter("", bedToolsExecutionConfigurationPanel, "The mode to execute bedtools.");
  }
  
  private void bedToolsExecutorChanged(BinaryExecutionConfigurationPanel<BedToolsBinariesExecutor> source) {
    this.bedToolsExecutorChanged();
  }
  
  private void bedToolsExecutorChanged() {
    invokeLater(() -> {
      this.transformationProvider
        .setBedToolsBinariesExecutor(this.bedToolsExecutionConfigurationPanel.getBinariesExecutor());
    });
  }

  private InputParameter getGenomeQueryFileParameter() {
    this.fileQuery =
      JFileChooserPanelBuilder
        .createOpenJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withFileChooserSelectionMode(SelectionMode.FILES)
        .withLabel("")
        .withClearSelectedFileOnShow(true)
        .build();
    this.fileQuery.addFileChooserListener(e -> this.fileQueryChanged());

    return new InputParameter(
      PARAM_EXTERNAL_QUERY_FILE_DESCRIPTION + ":", this.fileQuery, PARAM_EXTERNAL_QUERY_FILE_HELP_GUI
    );
  }

  private void fileQueryChanged() {
    invokeLater(() -> {
      if (this.fileQuery.getSelectedFile() != null) {
        this.transformationProvider.setQueryFile(this.fileQuery.getSelectedFile());
      } else {
        this.transformationProvider.clearQueryFile();
      }
    });
  }

  private InputParameter getConcatenateExonsParameter() {
    this.concatenateExons = new JCheckBox(PARAM_CONCATENATE_EXONS_DESCRIPTION, false);
    this.concatenateExons.addItemListener(e -> this.concatenateExonsChanged());

    return new InputParameter("", this.concatenateExons, PARAM_CONCATENATE_EXONS_HELP_GUI);
  }

  private void concatenateExonsChanged() {
    invokeLater(() -> {
      this.transformationProvider.setConcatenateExons(this.concatenateExons.isSelected());
    });
  }

  public SplignCompartPipelineTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(SplignCompartPipelineTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    if (this.transformationProvider.getQueryFile() != null) {
      this.fileQuery.setSelectedFile(this.transformationProvider.getQueryFile());
    } else {
      this.fileQuery.clearSelectedFile();
    }

    this.concatenateExons.setSelected(this.transformationProvider.isConcatenateExons());

    if (this.transformationProvider.getSplignCompartBinariesExecutor() != null) {
      this.splignCompartExecutionConfigurationPanel
        .setBinariesExecutor(this.transformationProvider.getSplignCompartBinariesExecutor());
    }

    if (this.transformationProvider.getBlastBinariesExecutor() != null) {
      this.blastExecutionConfigurationPanel
        .setBinariesExecutor(this.transformationProvider.getBlastBinariesExecutor());
    }

    if (this.transformationProvider.getBedToolsBinariesExecutor() != null) {
      this.bedToolsExecutionConfigurationPanel
        .setBinariesExecutor(this.transformationProvider.getBedToolsBinariesExecutor());
    }
  }
}