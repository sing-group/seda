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
package org.sing_group.seda.clustalomega.gui;

import static javax.swing.JOptionPane.showMessageDialog;
import static org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesChecker.checkClustalOmegaBinary;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.gc4s.utilities.ExtendedAbstractAction;
import org.sing_group.gc4s.utilities.builder.JButtonBuilder;
import org.sing_group.seda.clustalomega.execution.BinaryCheckException;
import org.sing_group.seda.clustalomega.execution.ClustalOmegaEnvironment;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class ClustalOmegaAlignmentTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_NUM_THREADS = "Number of threads to use.";
  private static final String HELP_CLUSTAL_OMEGA_PATH = "<html>The Clustal Omega binary file.<br/> If the Clustal "
    + "Omega binary is in the path (<b>clustalo</b> in Unix systems and <b>clustalo.exe</b> in Windows systems), then "
    + "this can be empty and the <i>Check binary</i> would say that it is right.</html>";
  private static final String HELP_ADDITIONAL_PARAMETERS = "Additional parameters for the Clustal Omega command.";

  private JIntegerTextField numThreads;
  private JButton clustalOmegaPathButton;
  private JXTextField additionalParameters;
  private JFileChooserPanel clustalOmegaPath;
  private ClustalOmegaAlignmentTransformationProvider transformationProvider;

  public ClustalOmegaAlignmentTransformationConfigurationPanel() {
    this.init();
    this.transformationProvider = new ClustalOmegaAlignmentTransformationProvider(this);
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    mainPanel.add(getQueryConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private InputParametersPanel getQueryConfigurationPanel() {
    InputParametersPanel queryConfigurationPanel = new InputParametersPanel(getParameters());

    return queryConfigurationPanel;
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<InputParameter>();
    parameters.add(getClustalOmegaPathParameter());
    parameters.add(getNumThreadsParameter());
    parameters.add(getAdditionalParametersParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getClustalOmegaPathParameter() {
    this.clustalOmegaPath = JFileChooserPanelBuilder
      .createOpenJFileChooserPanel()
      .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
      .withFileChooserSelectionMode(SelectionMode.FILES)
      .withLabel("Custal Omega executable path: ")
      .build();

    this.clustalOmegaPath.addFileChooserListener(this::clustalOmegaPathChanged);

    this.clustalOmegaPathButton = JButtonBuilder.newJButtonBuilder().thatDoes(getCheckClustalOmegaAction()).build();

    JPanel clustalOmegaPathPanel = new JPanel(new BorderLayout());
    clustalOmegaPathPanel.add(this.clustalOmegaPath, BorderLayout.CENTER);
    clustalOmegaPathPanel.add(this.clustalOmegaPathButton, BorderLayout.EAST);

    return new InputParameter("", clustalOmegaPathPanel, HELP_CLUSTAL_OMEGA_PATH);
  }

  private Action getCheckClustalOmegaAction() {
    return new ExtendedAbstractAction("Check binary", this::checkClustalOmegaButton);
  }

  private void checkClustalOmegaButton() {
    clustalOmegaPathChanged(new ChangeEvent(this));
  }

  private void clustalOmegaPathChanged(ChangeEvent event) {
    checkClustalOmegaPath();
    this.transformationProvider.clustalOmegaPathChanged();
  }

  private void checkClustalOmegaPath() {
    try {
      checkClustalOmegaBinary(getClustalOmegaPath());
      showMessageDialog(
        getParentForDialogs(),
        "Clustal Omega checked successfully.",
        "Check Clustal Omega",
        JOptionPane.INFORMATION_MESSAGE
      );
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking Clustal Omega: " + e.getCommand() + ".",
        "Error checking Clustal Omega",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  private Component getParentForDialogs() {
    return SwingUtilities.getRootPane(this);
  }

  private InputParameter getNumThreadsParameter() {
    this.numThreads = new JIntegerTextField(1);
    this.numThreads.setColumns(4);
    this.numThreads.getDocument()
      .addDocumentListener(new MyDocumentAdater(() -> transformationProvider.numThreadsChanged()));

    return new InputParameter(
      "Num. threads:", this.numThreads, HELP_NUM_THREADS
    );
  }

  private InputParameter getAdditionalParametersParameter() {
    this.additionalParameters = new JXTextField("Additional Clustal Omega parameters");
    this.additionalParameters.getDocument()
      .addDocumentListener(new MyDocumentAdater(() -> transformationProvider.additionalParametersChanged()));

    return new InputParameter(
      "Additional parameters:", this.additionalParameters, HELP_ADDITIONAL_PARAMETERS
    );
  }

  public TransformationProvider getModel() {
    return this.transformationProvider;
  }

  public int getNumThreads() {
    return this.numThreads.getValue();
  }

  private class MyDocumentAdater extends DocumentAdapter {

    private Runnable runnable;

    public MyDocumentAdater(Runnable runnable) {
      this.runnable = runnable;
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      valueChanged();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      valueChanged();
    }

    private void valueChanged() {
      runnable.run();
    }
  }

  public File getClustalOmegaPath() {
    if (this.clustalOmegaPath.getSelectedFile() != null) {
      return this.clustalOmegaPath.getSelectedFile();
    } else {
      return Paths.get(ClustalOmegaEnvironment.getInstance().getClustalOmegaCommand()).toFile();
    }
  }

  public String getAdditionalParameters() {
    return this.additionalParameters.getText();
  }
}