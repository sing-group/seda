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
package org.sing_group.seda.blast.gui.twowayblast;

import static java.lang.System.getProperty;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.combobox.ExtendedJComboBox;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.input.text.DoubleTextField;
import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.blast.datatype.SequenceType;
import org.sing_group.seda.blast.datatype.TwoWayBlastMode;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.gui.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.blast.gui.DockerExecutionConfigurationPanel;
import org.sing_group.seda.blast.gui.SystemBinaryExecutionConfigurationPanel;
import org.sing_group.seda.blast.transformation.dataset.BlastTransformation;
import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.core.SedaContextEvent;
import org.sing_group.seda.core.SedaContextEvent.SedaContextEventType;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class TwoWayBlastTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String HELP_SEQ_TYPE =
    "The type of the sequences in the database. This is automatically selected based on the blast command to execute.";
  private static final String HELP_QUERY_MODE =
    "The mode in which the query should be performed.";
  private static final String HELP_QUERY_SOURCE = "The source of the query sequences.";
  private static final String HELP_BLAST_TYPE = "The blast command to execute.";
  private static final String HELP_DATABASES =
    html(
      "Whether blast databases must be stored or not. <br/>By choosing to store them, they can be reused for future analysis."
    );
  private static final String HELP_DATABASES_DIR = "The directory where databases must be stored.";
  private static final String HELP_GENOME_QUERY_COMBO =
    html(
      "When <i>" + QueryType.INTERNAL
        + "</i> is selected, the genome whose sequences must be used for the blast queries."
    );
  private static final String HELP_QUERY_FILE =
    html(
      "When <i>" + QueryType.EXTERNAL
        + "</i> is selected, the file that contains the sequences must be used for the blast queries."
    );
  private static final String HELP_EVALUE = "The expectation value (E) threshold for saving hits.";
  private static final String HELP_ADDITIONAL_PARAMS = "Additional parameters for the blast command.";

  private static final String html(String string) {
    return "<html>" + string + "</html>";
  }

  private enum QueryType {
    INTERNAL("From selected file"), EXTERNAL("From external file");

    private String description;

    QueryType(String description) {
      this.description = description;
    }

    @Override
    public String toString() {
      return this.description;
    }
  };

  private TwoWayBlastTransformationProvider transformationProvider;
  private SedaContext context;
  private JCheckBox storeDatabases;
  private JFileChooserPanel databasesDirectory;
  private RadioButtonsPanel<QueryType> queryTypeRadioButtonsPanel;
  private ExtendedJComboBox<String> genomeQueryCombobox;
  private DefaultComboBoxModel<String> genomeQueryComboboxModel;
  private JFileChooserPanel fileQuery;
  private JFileChooserPanel blastPath;
  private RadioButtonsPanel<SequenceType> sequenceTypeRbtnPanel;
  private JComboBox<BlastType> blastTypeCombobox;
  private RadioButtonsPanel<TwoWayBlastMode> queryModeRadioButtonsPanel;
  private DoubleTextField eValue;
  private JXTextField additionalBlastParameters;
  private CardsPanel blastExecutableCardsPanel;

  public TwoWayBlastTransformationConfigurationPanel() {
    this.init();
    this.transformationProvider = new TwoWayBlastTransformationProvider(this);
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    mainPanel.add(getBlastConfigurationPanel());
    mainPanel.add(getDatabaseConfigurationPanel());
    mainPanel.add(getQueryConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private InputParametersPanel getBlastConfigurationPanel() {
    InputParametersPanel blastConfigurationPanel = new InputParametersPanel(getBlastParameters());
    blastConfigurationPanel.setBorder(BorderFactory.createTitledBorder("Blast configuration"));

    return blastConfigurationPanel;
  }

  private InputParameter[] getBlastParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getBlastExecutableParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getBlastExecutableParameter() {
    SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel =
      new SystemBinaryExecutionConfigurationPanel();
    systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this::blastExecutorChanged);

    DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this::blastExecutorChanged);

    CardsPanelBuilder builder =
      CardsPanelBuilder.newBuilder()
        .withCard("System binary", systemBinaryExecutionConfigurationPanel);

    if (!getProperty(GuiUtils.PROPERTY_ENABLE_DOCKER_EXECUTION, "true").equals("false")) {
      builder = builder.withCard("Docker image", dockerExecutionConfigurationPanel);
    }

    this.blastExecutableCardsPanel =
      builder
        .withSelectionLabel("Execution mode")
        .build();

    this.blastExecutableCardsPanel
      .addPropertyChangeListener(PROPERTY_VISIBLE_CARD, this::blastBinaryExecutorCardChanged);

    return new InputParameter("", blastExecutableCardsPanel, "The mode to execute Blast.");
  }

  private void blastExecutorChanged(BinaryExecutionConfigurationPanel source) {
    this.blastExecutorChanged();
  }

  private void blastBinaryExecutorCardChanged(PropertyChangeEvent event) {
   this.blastExecutorChanged();
  }
  
  private void blastExecutorChanged() {
    invokeLater(() -> {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      this.transformationProvider.blastExecutorChanged();
      this.setCursor(Cursor.getDefaultCursor());
    });
  }

  public Optional<BlastBinariesExecutor> getBlastBinariesExecutor() {
    return ((BinaryExecutionConfigurationPanel) this.blastExecutableCardsPanel.getSelectedCard())
      .getBlastBinariesExecutor();
  }

  private InputParametersPanel getDatabaseConfigurationPanel() {
    InputParametersPanel dbConfigurationPanel = new InputParametersPanel(getDbParameters());
    dbConfigurationPanel.setBorder(BorderFactory.createTitledBorder("DB configuration"));

    return dbConfigurationPanel;
  }

  private InputParameter[] getDbParameters() {
    List<InputParameter> dbParameters = new LinkedList<InputParameter>();
    dbParameters.add(getSequenceTypeParameter());
    dbParameters.add(getStoreDatabasesParameter());
    dbParameters.add(getDatabasesDirectoryParameter());

    this.checkDatabaseFileChooser();

    return dbParameters.toArray(new InputParameter[dbParameters.size()]);
  }

  private InputParameter getSequenceTypeParameter() {
    this.sequenceTypeRbtnPanel = new RadioButtonsPanel<SequenceType>(SequenceType.values(), 1, 0);
    this.sequenceTypeRbtnPanel.setEnabled(false);

    return new InputParameter("Sequence type: ", this.sequenceTypeRbtnPanel, HELP_SEQ_TYPE);
  }

  private InputParametersPanel getQueryConfigurationPanel() {
    InputParametersPanel queryConfigurationPanel = new InputParametersPanel(getParameters());
    queryConfigurationPanel.setBorder(BorderFactory.createTitledBorder("Query configuration"));

    return queryConfigurationPanel;
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<InputParameter>();
    parameters.add(getQueryModeParameter());
    parameters.add(getBlastTypeParameter());
    parameters.add(getQueryTypeParameter());
    parameters.add(getGenomeQueryParameter());
    parameters.add(getFileQueryParameter());
    parameters.add(getEvalueParameter());
    parameters.add(getAdditionalBlastParamsParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getQueryModeParameter() {
    this.queryModeRadioButtonsPanel = new RadioButtonsPanel<TwoWayBlastMode>(TwoWayBlastMode.values(), 1, 0);
    this.queryModeRadioButtonsPanel.addItemListener(this::queryModeChanged);

    return new InputParameter("Mode: ", this.queryModeRadioButtonsPanel, HELP_QUERY_MODE);
  }

  private void queryModeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.transformationProvider.queryModeChanged();
    }
  }

  private InputParameter getQueryTypeParameter() {
    this.queryTypeRadioButtonsPanel = new RadioButtonsPanel<QueryType>(QueryType.values(), 1, 0);
    this.queryTypeRadioButtonsPanel.addItemListener(this::queryTypeChanged);

    return new InputParameter("Query source: ", this.queryTypeRadioButtonsPanel, HELP_QUERY_SOURCE);
  }

  private void queryTypeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.transformationProvider.queryFileChanged();
    }
    SwingUtilities.invokeLater(this::checkQuerySelection);
  }

  private InputParameter getBlastTypeParameter() {
    this.blastTypeCombobox = new JComboBox<>(BlastType.values());
    this.blastTypeCombobox.addItemListener(this::blastTypeChanged);

    return new InputParameter("Blast type: ", this.blastTypeCombobox, HELP_BLAST_TYPE);
  }

  private void blastTypeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.sequenceTypeRbtnPanel.setSelectedItem(getBlastType().getDatabaseType());
      this.checkQuerySelection();
      this.transformationProvider.blastTypeChanged();
    }
  }

  private void checkQuerySelection() {
    BlastType selectedBlastType = getBlastType();
    boolean disabled = selectedBlastType.equals(BlastType.BLASTX) || selectedBlastType.equals(BlastType.TBLASTN);
    if (disabled) {
      this.queryTypeRadioButtonsPanel.setSelectedItem(QueryType.EXTERNAL);
    }
    this.genomeQueryCombobox.setEnabled(!isExternalQueryFile());
  }

  public BlastType getBlastType() {
    return (BlastType) this.blastTypeCombobox.getSelectedItem();
  }

  public TwoWayBlastMode getQueryMode() {
    return this.queryModeRadioButtonsPanel.getSelectedItem().get();
  }

  private InputParameter getStoreDatabasesParameter() {
    this.storeDatabases = new JCheckBox();
    this.storeDatabases.addItemListener(this::storeDatabasesChanged);

    return new InputParameter("Store databases:", this.storeDatabases, HELP_DATABASES);
  }

  private void storeDatabasesChanged(ItemEvent event) {
    this.transformationProvider.storeDatabasesChanged();
    SwingUtilities.invokeLater(this::checkDatabaseFileChooser);
  }

  private void checkDatabaseFileChooser() {
    boolean enabled = this.storeDatabases.isEnabled() && this.storeDatabases.isSelected();
    this.databasesDirectory.getBrowseAction().setEnabled(enabled);
  }

  private InputParameter getDatabasesDirectoryParameter() {
    this.databasesDirectory =
      JFileChooserPanelBuilder
        .createSaveJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withFileChooserSelectionMode(SelectionMode.DIRECTORIES)
        .withLabel("")
        .build();
    this.databasesDirectory.addFileChooserListener(this::databasesDirectoryChanged);

    return new InputParameter("Databases directory:", this.databasesDirectory, HELP_DATABASES_DIR);
  }

  private void databasesDirectoryChanged(ChangeEvent event) {
    this.transformationProvider.databasesDirectoryChanged();
  }

  private InputParameter getGenomeQueryParameter() {
    this.genomeQueryComboboxModel = new DefaultComboBoxModel<String>();
    this.genomeQueryCombobox = new ExtendedJComboBox<>(this.genomeQueryComboboxModel);
    this.genomeQueryCombobox.addItemListener(this::queryChanged);
    this.genomeQueryCombobox.setPrototypeDisplayValue("");
    this.updateGenomeComboboxUi();

    return new InputParameter("Genome query:", genomeQueryCombobox, HELP_GENOME_QUERY_COMBO);
  }

  private void queryChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.transformationProvider.queryFileChanged();
      this.genomeQueryCombobox.setToolTipText(this.genomeQueryCombobox.getSelectedItem().toString());
    }
  }

  private InputParameter getFileQueryParameter() {
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
    this.transformationProvider.queryFileChanged();
  }

  private InputParameter getEvalueParameter() {
    this.eValue = new DoubleTextField(BlastTransformation.DEFAULT_EVALUE);
    this.eValue.getDocument().addDocumentListener(new MyDocumentAdater(() -> transformationProvider.eValueChanged()));

    return new InputParameter("Expectation value:", this.eValue, HELP_EVALUE);
  }

  private InputParameter getAdditionalBlastParamsParameter() {
    this.additionalBlastParameters = new JXTextField("Additional parameters for blast");
    this.additionalBlastParameters.getDocument()
      .addDocumentListener(new MyDocumentAdater(() -> transformationProvider.blastAdditionalParametersChanged()));

    return new InputParameter("Additional parameters:", this.additionalBlastParameters, HELP_ADDITIONAL_PARAMS);
  }

  public TransformationProvider getModel() {
    return this.transformationProvider;
  }

  public void setSedaContext(SedaContext context) {
    this.context = context;
    this.context.addSedaContextListener(this::contextChanged);
  }

  private void contextChanged(SedaContextEvent event) {
    if (event.getType().equals(SedaContextEventType.SELECTED_PATHS_CHANGED)) {
      selectedPathsChanged();
    }
  }

  private void selectedPathsChanged() {
    List<String> selectedPaths = this.context.getSelectedPaths();
    if (!selectedPaths.isEmpty()) {
      selectedPaths.forEach(
        p -> {
          this.genomeQueryComboboxModel.addElement(p);
        }
      );
    } else {
      this.genomeQueryComboboxModel.removeAllElements();
    }
    this.updateGenomeComboboxUi();
  }

  private void updateGenomeComboboxUi() {
    this.genomeQueryCombobox.setPreferredSize(new Dimension(200, 20));
    this.genomeQueryCombobox.setAutoAdjustWidth(true);
    this.genomeQueryCombobox.updateUI();
  }

  public Optional<File> getQueryFile() {
    if (isExternalQueryFile()) {
      return Optional.ofNullable(this.fileQuery.getSelectedFile());
    } else {
      if (this.genomeQueryCombobox.getSelectedIndex() == -1) {
        return Optional.empty();
      }
      return Optional.of(new File(this.genomeQueryCombobox.getSelectedItem().toString()));
    }
  }

  private boolean isExternalQueryFile() {
    return this.queryTypeRadioButtonsPanel.getSelectedItem().get().equals(QueryType.EXTERNAL);
  }

  public File getBlastPath() {
    return this.blastPath.getSelectedFile();
  }

  public boolean isStoreDatabases() {
    return this.storeDatabases.isSelected();
  }

  public File getDatabasesDirectory() {
    return this.databasesDirectory.getSelectedFile();
  }

  public double getEvalue() {
    return this.eValue.getValue();
  }

  public String getBlastAditionalParameters() {
    return this.additionalBlastParameters.getText();
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
}
