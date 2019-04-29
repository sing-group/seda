/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.clustalomega.gui;

import static java.awt.BorderLayout.CENTER;
import static java.lang.System.getProperty;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class ClustalOmegaAlignmentTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_NUM_THREADS = "Number of threads to use.";
  private static final String HELP_ADDITIONAL_PARAMETERS = "Additional parameters for the Clustal Omega command.";

  private JIntegerTextField numThreads;
  private JXTextField additionalParameters;
  private CardsPanel clustalOmegaExecutableCardsPanel;
  private ClustalOmegaAlignmentTransformationProvider transformationProvider;

  public ClustalOmegaAlignmentTransformationConfigurationPanel() {
    this.init();
    this.transformationProvider = new ClustalOmegaAlignmentTransformationProvider(this);
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
    parameters.add(getClustalOmegaExecutableParameter());
    parameters.add(getNumThreadsParameter());
    parameters.add(getAdditionalParametersParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getClustalOmegaExecutableParameter() {
    SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel =
      new SystemBinaryExecutionConfigurationPanel();
    systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this::clustalOmegaExecutorChanged);

    DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this::clustalOmegaExecutorChanged);

    CardsPanelBuilder builder =
      CardsPanelBuilder.newBuilder()
        .withCard("Docker image", dockerExecutionConfigurationPanel);

    if (!getProperty(GuiUtils.PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")) {
      builder = builder.withCard("System binary", systemBinaryExecutionConfigurationPanel);
    }

    this.clustalOmegaExecutableCardsPanel =
      builder
        .withSelectionLabel("Execution mode")
        .build();

    this.clustalOmegaExecutableCardsPanel.setBorder(BorderFactory.createTitledBorder("Clustal Omega configuration"));

    this.clustalOmegaExecutableCardsPanel
      .addPropertyChangeListener(PROPERTY_VISIBLE_CARD, this::clustalOmegaBinaryExecutorCardChanged);

    return new InputParameter("", clustalOmegaExecutableCardsPanel, "The mode to execute Clustal Omega.");
  }

  private void clustalOmegaExecutorChanged(BinaryExecutionConfigurationPanel<ClustalOmegaBinariesExecutor> source) {
    this.clustalOmegaExecutorChanged();
  }

  private void clustalOmegaBinaryExecutorCardChanged(PropertyChangeEvent event) {
    this.clustalOmegaExecutorChanged();
  }

  private void clustalOmegaExecutorChanged() {
    invokeLater(() -> {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      this.transformationProvider.clustalOmegaExecutorChanged();
      this.setCursor(Cursor.getDefaultCursor());
    });
  }

  public Optional<ClustalOmegaBinariesExecutor> getClustalOmegaBinariesExecutor() {
    @SuppressWarnings("unchecked")
    BinaryExecutionConfigurationPanel<ClustalOmegaBinariesExecutor> selectedCard =
      ((BinaryExecutionConfigurationPanel<ClustalOmegaBinariesExecutor>) this.clustalOmegaExecutableCardsPanel
        .getSelectedCard());

    return selectedCard.getBinariesExecutor();
  }

  private InputParameter getNumThreadsParameter() {
    this.numThreads = new JIntegerTextField(1);
    this.numThreads.setColumns(4);
    this.numThreads.getDocument()
      .addDocumentListener(new MyDocumentAdater(() -> transformationProvider.numThreadsChanged()));

    return new InputParameter("Num. threads:", this.numThreads, HELP_NUM_THREADS);
  }

  private InputParameter getAdditionalParametersParameter() {
    this.additionalParameters = new JXTextField("Additional Clustal Omega parameters");
    this.additionalParameters.getDocument()
      .addDocumentListener(new MyDocumentAdater(() -> transformationProvider.additionalParametersChanged()));

    return new InputParameter("Additional parameters:", this.additionalParameters, HELP_ADDITIONAL_PARAMETERS);
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

  public String getAdditionalParameters() {
    return this.additionalParameters.getText();
  }
}