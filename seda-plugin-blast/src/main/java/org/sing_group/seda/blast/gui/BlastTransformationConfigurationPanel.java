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
package org.sing_group.seda.blast.gui;

import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
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
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.blast.datatype.DatabaseQueryMode;
import org.sing_group.seda.blast.datatype.SequenceType;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.transformation.dataset.BlastTransformation;
import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.core.SedaContextEvent;
import org.sing_group.seda.core.SedaContextEvent.SedaContextEventType;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class BlastTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String HELP_SEQ_TYPE =
    "The type of the sequences in the database. This is automatically selected based on the blast command to execute.";
  private static final String HELP_DATABASE_QUERY_MODE =
    "The mode in which the query should be performed.";
  private static final String HELP_QUERY_SOURCE = "The source of the query sequences.";
  private static final String HELP_BLAST_TYPE = "The blast command to execute.";
  private static final String HELP_DATABASES =
    html(
      "Whether blast databases must be stored or not. <br/>By choosing to store them, they can be reused for future analysis."
    );
  private static final String HELP_ALIAS = "Whether the database alias must be stored or not.";
  private static final String HELP_DATABASES_DIR = "The directory where databases must be stored.";
  private static final String HELP_ALIAS_FILE = "The directory where the alias must be stored.";
  private static final String HELP_FILE_QUERY_COMBO =
    html(
      "When <i>" + QueryType.INTERNAL
        + "</i> is selected, the file whose sequences must be used for the blast queries."
    );
  private static final String HELP_QUERY_FILE =
    html(
      "When <i>" + QueryType.EXTERNAL
        + "</i> is selected, the file that contains the sequences that must be used for the blast queries."
    );
  private static final String HELP_EVALUE = "The expectation value (E) threshold for saving hits.";
  private static final String HELP_MAX_TARGET_SEQS = "The maximum number of aligned sequences to keep.";
  private static final String HELP_ADDITIONAL_PARAMS = "Additional parameters for the blast command.";
  private static final String HELP_HIT_REGIONS =
    "Use this option to extract only the part of "
      + "the sequences where hits are produced instead of the entire subject sequences.";
  private static final String HELP_HIT_REGION_WS = "The window size to retrieve only hit regions.";

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

  private BlastTransformationProvider transformationProvider;
  private SedaContext context;
  private JCheckBox storeDatabases;
  private JCheckBox storeAlias;
  private JFileChooserPanel databasesDirectory;
  private JFileChooserPanel aliasFile;
  private RadioButtonsPanel<QueryType> queryTypeRadioButtonsPanel;
  private ExtendedJComboBox<String> fileQueryCombobox;
  private DefaultComboBoxModel<String> fileQueryComboboxModel;
  private JFileChooserPanel fileQuery;
  private JFileChooserPanel blastPath;
  private RadioButtonsPanel<SequenceType> sequenceTypeRbtnPanel;
  private JComboBox<BlastType> blastTypeCombobox;
  private RadioButtonsPanel<DatabaseQueryMode> databaseQueryModeRadioButtonsPanel;
  private DoubleTextField eValue;
  private JIntegerTextField maxTargetSeqs;
  private JXTextField additionalBlastParameters;
  private JCheckBox extractOnlyHitRegions;
  private JIntegerTextField hitRegionsWindowSize;
  private BlastExecutionConfigurationPanel blastExecutionConfigurationPanel;

  public BlastTransformationConfigurationPanel() {
    this.init();
    this.transformationProvider = new BlastTransformationProvider(this);
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

  private InputParameter getBlastExecutableParameter() {
    this.blastExecutionConfigurationPanel = new BlastExecutionConfigurationPanel(this::blastExecutorChanged);

    return new InputParameter("", blastExecutionConfigurationPanel, "The mode to execute Blast.");
  }
  
  private void blastExecutorChanged(BinaryExecutionConfigurationPanel<BlastBinariesExecutor> source) {
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
   return this.blastExecutionConfigurationPanel.getBlastBinariesExecutor();
  }

  private InputParameter[] getBlastParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getBlastExecutableParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
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
    dbParameters.add(getStoreAliasParameter());
    dbParameters.add(getAliasFileParameter());

    this.checkDatabaseFileChooser();
    this.checkAliasFileChooser();

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
    parameters.add(getDatabaseQueryModeParameter());
    parameters.add(getBlastTypeParameter());
    parameters.add(getQueryTypeParameter());
    parameters.add(getFileQueryParameter());
    parameters.add(getExternalFileQueryParameter());
    parameters.add(getEvalueParameter());
    parameters.add(getMaxTargetSeqsParameter());
    parameters.add(getAdditionalBlastParamsParameter());
    parameters.add(getExtractOnlyHitRegionsParamsParameter());
    parameters.add(getHitRegionsWindowSizeParamsParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getDatabaseQueryModeParameter() {
    this.databaseQueryModeRadioButtonsPanel =
      new RadioButtonsPanel<DatabaseQueryMode>(DatabaseQueryMode.values(), 1, 0);
    this.databaseQueryModeRadioButtonsPanel.addItemListener(this::databaseQueryModeChanged);

    return new InputParameter("Query against: ", this.databaseQueryModeRadioButtonsPanel, HELP_DATABASE_QUERY_MODE);
  }

  private void databaseQueryModeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.transformationProvider.databaseQueryModeChanged();
      SwingUtilities.invokeLater(this::checkDatabaseQueryMode);
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

  private void checkDatabaseQueryMode() {
    boolean enabled = this.getDatabaseQueryMode().equals(DatabaseQueryMode.ALL);
    this.storeAlias.setEnabled(enabled);
    this.checkAliasFileChooser();
  }

  private void checkQuerySelection() {
    BlastType selectedBlastType = getBlastType();
    boolean disabled = selectedBlastType.equals(BlastType.BLASTX) || selectedBlastType.equals(BlastType.TBLASTN);
    if (disabled) {
      this.queryTypeRadioButtonsPanel.setSelectedItem(QueryType.EXTERNAL);
    }
    this.fileQueryCombobox.setEnabled(!isExternalQueryFile());
  }

  public BlastType getBlastType() {
    return (BlastType) this.blastTypeCombobox.getSelectedItem();
  }

  public DatabaseQueryMode getDatabaseQueryMode() {
    return this.databaseQueryModeRadioButtonsPanel.getSelectedItem().get();
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

  private InputParameter getStoreAliasParameter() {
    this.storeAlias = new JCheckBox();
    this.storeAlias.addItemListener(this::storeAliasChanged);

    return new InputParameter("Store alias:", this.storeAlias, HELP_ALIAS);
  }

  private void storeAliasChanged(ItemEvent event) {
    this.transformationProvider.storeAliasChanged();
    SwingUtilities.invokeLater(this::checkAliasFileChooser);
  }

  private void checkAliasFileChooser() {
    boolean enabled = this.storeAlias.isSelected() && this.storeAlias.isEnabled();
    this.aliasFile.getBrowseAction().setEnabled(enabled);
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

  private InputParameter getAliasFileParameter() {
    this.aliasFile =
      JFileChooserPanelBuilder
        .createSaveJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withFileChooserSelectionMode(SelectionMode.FILES)
        .withLabel("")
        .build();
    this.aliasFile.addFileChooserListener(this::aliasFileChanged);

    return new InputParameter("Alias file:", this.aliasFile, HELP_ALIAS_FILE);
  }

  private void aliasFileChanged(ChangeEvent event) {
    this.transformationProvider.aliasFileChanged();
  }

  private InputParameter getFileQueryParameter() {
    this.fileQueryComboboxModel = new DefaultComboBoxModel<String>();
    this.fileQueryCombobox = new ExtendedJComboBox<>(this.fileQueryComboboxModel);
    this.fileQueryCombobox.addItemListener(this::queryChanged);
    this.fileQueryCombobox.setPrototypeDisplayValue("");
    this.updateFileQueryComboboxUi();

    return new InputParameter("File query:", fileQueryCombobox, HELP_FILE_QUERY_COMBO);
  }

  private void queryChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.transformationProvider.queryFileChanged();
      this.fileQueryCombobox.setToolTipText(this.fileQueryCombobox.getSelectedItem().toString());
    }
  }

  private InputParameter getExternalFileQueryParameter() {
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

  private InputParameter getMaxTargetSeqsParameter() {
    this.maxTargetSeqs = new JIntegerTextField(BlastTransformation.DEFAULT_MAX_TARGET_SEQS);
    this.maxTargetSeqs.getDocument()
      .addDocumentListener(new MyDocumentAdater(() -> transformationProvider.maxTargetSeqsChanged()));

    return new InputParameter("Max. target seqs.:", this.maxTargetSeqs, HELP_MAX_TARGET_SEQS);
  }

  private InputParameter getAdditionalBlastParamsParameter() {
    this.additionalBlastParameters = new JXTextField("Additional parameters for blast");
    this.additionalBlastParameters.getDocument()
      .addDocumentListener(new MyDocumentAdater(() -> transformationProvider.blastAdditionalParametersChanged()));

    return new InputParameter("Additional parameters:", this.additionalBlastParameters, HELP_ADDITIONAL_PARAMS);
  }

  private InputParameter getExtractOnlyHitRegionsParamsParameter() {
    this.extractOnlyHitRegions = new JCheckBox();
    this.extractOnlyHitRegions.addItemListener(this::extractOnlyHiyRegionsChanged);

    return new InputParameter("Extract only hit regions:", this.extractOnlyHitRegions, HELP_HIT_REGIONS);
  }

  private void extractOnlyHiyRegionsChanged(ItemEvent event) {
    this.hitRegionsWindowSize.setEnabled(isExtractOnlyHitRegions());
    this.transformationProvider.extractOnlyHitRegionsChanged();
  }

  private InputParameter getHitRegionsWindowSizeParamsParameter() {
    this.hitRegionsWindowSize = new JIntegerTextField(BlastTransformation.DEFAULT_HIT_REGIONS_WINDOW_SIZE);
    this.hitRegionsWindowSize.setEnabled(isExtractOnlyHitRegions());
    this.hitRegionsWindowSize.getDocument()
      .addDocumentListener(new MyDocumentAdater(() -> transformationProvider.hitRegionsWindowSizeChanged()));

    return new InputParameter(
      "Hit regions window:", this.hitRegionsWindowSize, HELP_HIT_REGION_WS
    );
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
    this.fileQueryComboboxModel.removeAllElements();
    if (!selectedPaths.isEmpty()) {
      selectedPaths.forEach(
        p -> {
          this.fileQueryComboboxModel.addElement(p);
        }
      );
    }
    this.updateFileQueryComboboxUi();
  }

  private void updateFileQueryComboboxUi() {
    this.fileQueryCombobox.setPreferredSize(new Dimension(200, 20));
    this.fileQueryCombobox.setAutoAdjustWidth(true);
    this.fileQueryCombobox.updateUI();
  }

  public Optional<File> getQueryFile() {
    if (isExternalQueryFile()) {
      return Optional.ofNullable(this.fileQuery.getSelectedFile());
    } else {
      if (this.fileQueryCombobox.getSelectedIndex() == -1) {
        return Optional.empty();
      }
      return Optional.of(new File(this.fileQueryCombobox.getSelectedItem().toString()));
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

  public boolean isStoreAlias() {
    return this.getDatabaseQueryMode().equals(DatabaseQueryMode.ALL) && this.storeAlias.isSelected();
  }

  public File getDatabasesDirectory() {
    return this.databasesDirectory.getSelectedFile();
  }

  public Optional<File> getAliasFile() {
    if (this.isStoreAlias()) {
      return Optional.ofNullable(this.aliasFile.getSelectedFile());
    } else {
      return Optional.empty();
    }
  }

  public int getMaxTargetSeqs() {
    return this.maxTargetSeqs.getValue();
  }

  public double getEvalue() {
    return this.eValue.getValue();
  }

  public String getBlastAditionalParameters() {
    return this.additionalBlastParameters.getText();
  }

  public boolean isExtractOnlyHitRegions() {
    return this.extractOnlyHitRegions.isSelected();
  }

  public int getHitRegionsWindowSize() {
    return this.hitRegionsWindowSize.getValue();
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
