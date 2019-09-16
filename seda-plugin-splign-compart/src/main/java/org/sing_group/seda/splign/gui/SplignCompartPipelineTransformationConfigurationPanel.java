/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

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
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.splign.execution.SplignCompartBinariesExecutor;

public class SplignCompartPipelineTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_QUERY_FILE = "The CDS query file (nucleotides).";
  private static final String HELP_CONCATENATE_EXONS =
    "<html>If the <b>concatenate exons</b> option is checked, "
      + "then adjacent exons will be concatenated.<br>Therefore, if an annotation is obtained for every exon "
      + "of a given gene, the resulting sequence will be the complete CDS.</html>";

  private JFileChooserPanel fileQuery;
  private JCheckBox concatenateExons;
  private SplignCompartExecutionConfigurationPanel splignCompartExecutionConfigurationPanel;
  private BlastExecutionConfigurationPanel blastExecutionConfigurationPanel;
  private BedToolsExecutionConfigurationPanel bedToolsExecutionConfigurationPanel;
  private SplignCompartPipelineTransformationProvider transformationProvider;

  public SplignCompartPipelineTransformationConfigurationPanel() {
    this.init();
    this.transformationProvider = new SplignCompartPipelineTransformationProvider(this);
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
    InputParametersPanel queryConfigurationPanel = new InputParametersPanel(getParameters());

    return queryConfigurationPanel;
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
    notifyTransformationProvider(this.transformationProvider::splignCompartExecutorChanged);
  }

  private void notifyTransformationProvider(Runnable r) {
    invokeLater(() -> {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      r.run();
      this.setCursor(Cursor.getDefaultCursor());
    });
  }

  public Optional<SplignCompartBinariesExecutor> getSplignCompartBinariesExecutor() {
    return this.splignCompartExecutionConfigurationPanel.getBinariesExecutor();
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
    notifyTransformationProvider(this.transformationProvider::blastExecutorChanged);
  }

  public Optional<BlastBinariesExecutor> getBlastBinariesExecutor() {
   return this.blastExecutionConfigurationPanel.getBlastBinariesExecutor();
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
    notifyTransformationProvider(this.transformationProvider::bedToolsExecutorChanged);
  }
  
  public Optional<BedToolsBinariesExecutor> getBedToolsBinariesExecutor() {
    return this.bedToolsExecutionConfigurationPanel.getBedToolsBinariesExecutor();
  }
  
  private InputParameter getGenomeQueryFileParameter() {
    this.fileQuery =
      JFileChooserPanelBuilder
        .createOpenJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withFileChooserSelectionMode(SelectionMode.FILES)
        .withLabel("")
        .build();
    this.fileQuery.addFileChooserListener(this::fileQueryChanged);

    return new InputParameter("External file query:", this.fileQuery, HELP_QUERY_FILE);
  }

  private void fileQueryChanged(ChangeEvent event) {
    notifyTransformationProvider(this.transformationProvider::queryFileChanged);
  }

  private InputParameter getConcatenateExonsParameter() {
    this.concatenateExons = new JCheckBox("Concatenate exons?", false);
    this.concatenateExons.addItemListener(this::concatenateExonsItemListener);

    return new InputParameter("", this.concatenateExons, HELP_CONCATENATE_EXONS);
  }
  
  private void concatenateExonsItemListener(ItemEvent event) {
    this.transformationProvider.concatenateExonsChanged();
  }

  public boolean isConcatenateExons() {
    return this.concatenateExons.isSelected();
  }

  public TransformationProvider getModel() {
    return this.transformationProvider;
  }

  public File getQueryFile() {
    return this.fileQuery.getSelectedFile();
  }
}