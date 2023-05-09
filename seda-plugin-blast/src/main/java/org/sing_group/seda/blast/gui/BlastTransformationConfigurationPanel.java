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
package org.sing_group.seda.blast.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.DEFAULT_EVALUE;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.DEFAULT_HIT_REGIONS_WINDOW_SIZE;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.DEFAULT_MAX_TARGET_SEQS;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_DATABASE_QUERY_MODE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_DATABASE_QUERY_MODE_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EVALUE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EVALUE_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EXTRACT_HIT_REGIONS_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EXTRACT_HIT_REGIONS_SIZE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_HIT_REGION_WINDOW_SIZE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_HIT_REGION_WINDOW_SIZE_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_MAX_TARGET_SEQS_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_MAX_TARGET_SEQS_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_BLAST_TYPE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_BLAST_TYPE_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_SOURCE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_SOURCE_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_ALIAS_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_ALIAS_FILE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_ALIAS_FILE_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_ALIAS_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_DIRECTORY_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_DIRECTORY_HELP_GUI;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_HELP_GUI;

import java.awt.BorderLayout;
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

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.RunnableDocumentAdapter;
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
import org.sing_group.seda.blast.transformation.provider.blast.BlastTransformationProvider;
import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.core.SedaContextEvent;
import org.sing_group.seda.core.SedaContextEvent.SedaContextEventType;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;

public class BlastTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_SEQ_TYPE =
    "The type of the sequences in the database. This is automatically selected based on the BLAST command to execute.";
  private static final String HELP_FILE_QUERY_COMBO =
    html(
      "When <i>" + QueryType.INTERNAL
        + "</i> is selected, the input file whose sequences must be used for the BLAST queries."
    );
  private static final String HELP_QUERY_FILE =
    html(
      "When <i>" + QueryType.EXTERNAL
        + "</i> is selected, the file that contains the sequences that must be used for the BLAST queries."
    );

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
    this.initTransformationProvider();
  }

  private void initTransformationProvider() {
    this.transformationProvider =
      new BlastTransformationProvider(
        this.databaseQueryModeRadioButtonsPanel.getSelectedItem().get(),
        (BlastType) this.blastTypeCombobox.getSelectedItem(),
        this.eValue.getValue(),
        this.maxTargetSeqs.getValue(),
        this.isExtractOnlyHitRegions(),
        this.hitRegionsWindowSize.getValue()
      );
    this.blastExecutorChanged();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, Y_AXIS));

    mainPanel.add(getBlastConfigurationPanel());
    mainPanel.add(getDatabaseConfigurationPanel());
    mainPanel.add(getQueryConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private InputParametersPanel getBlastConfigurationPanel() {
    InputParametersPanel blastConfigurationPanel = new InputParametersPanel(getBlastParameters());
    blastConfigurationPanel.setBorder(BorderFactory.createTitledBorder("BLAST configuration"));

    return blastConfigurationPanel;
  }

  private InputParameter getBlastExecutableParameter() {
    this.blastExecutionConfigurationPanel = new BlastExecutionConfigurationPanel(this::blastExecutorChanged);

    return new InputParameter("", blastExecutionConfigurationPanel, "The mode to execute BLAST.");
  }
  
  private void blastExecutorChanged(BinaryExecutionConfigurationPanel<BlastBinariesExecutor> source) {
    this.blastExecutorChanged();
  }

  private void blastExecutorChanged() {
    invokeLater(() -> {
      this.transformationProvider.setBlastBinariesExecutor(this.blastExecutionConfigurationPanel.getBinariesExecutor());
    });
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

    return new InputParameter(
      PARAM_DATABASE_QUERY_MODE_DESCRIPTION + ": ", this.databaseQueryModeRadioButtonsPanel,
      PARAM_DATABASE_QUERY_MODE_HELP_GUI
    );
  }

  private void databaseQueryModeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setDatabaseQueryMode(this.getDatabaseQueryMode());
      });
      this.storeAliasChanged();
      SwingUtilities.invokeLater(this::checkDatabaseQueryMode);
    }
  }

  private DatabaseQueryMode getDatabaseQueryMode() {
    return this.databaseQueryModeRadioButtonsPanel.getSelectedItem().get();
  }

  private InputParameter getQueryTypeParameter() {
    this.queryTypeRadioButtonsPanel = new RadioButtonsPanel<QueryType>(QueryType.values(), 1, 0);
    this.queryTypeRadioButtonsPanel.addItemListener(this::queryTypeChanged);

    return new InputParameter(
      PARAM_QUERY_SOURCE_DESCRIPTION + ": ", this.queryTypeRadioButtonsPanel, PARAM_QUERY_SOURCE_HELP_GUI
    );
  }

  private void queryTypeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.queryFileChanged();
    }
    SwingUtilities.invokeLater(this::checkQuerySelection);
  }

  private void queryFileChanged() {
    invokeLater(() -> {
      Optional<File> queryFile = getQueryFile();
      if (queryFile.isPresent()) {
        this.transformationProvider.setQueryFile(queryFile.get());
      } else {
        this.transformationProvider.clearQueryFile();
      }
    });
  }

  private InputParameter getBlastTypeParameter() {
    this.blastTypeCombobox = new JComboBox<>(BlastType.values());
    this.blastTypeCombobox.addItemListener(this::blastTypeChanged);

    return new InputParameter(
      PARAM_QUERY_BLAST_TYPE_DESCRIPTION + ": ", this.blastTypeCombobox, PARAM_QUERY_BLAST_TYPE_HELP_GUI
    );
  }

  private void blastTypeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.sequenceTypeRbtnPanel.setSelectedItem(getBlastType().getDatabaseType());
      this.checkQuerySelection();
      invokeLater(() -> {
        this.transformationProvider.setBlastType(getBlastType());
      });
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

  private BlastType getBlastType() {
    return (BlastType) this.blastTypeCombobox.getSelectedItem();
  }

  private InputParameter getStoreDatabasesParameter() {
    this.storeDatabases = new JCheckBox();
    this.storeDatabases.addItemListener(this::storeDatabasesChanged);

    return new InputParameter(
      PARAM_STORE_DATABASES_DESCRIPTION + ":", this.storeDatabases, PARAM_STORE_DATABASES_HELP_GUI
    );
  }

  private void storeDatabasesChanged(ItemEvent event) {
    invokeLater(() -> {
      this.transformationProvider.setStoreDatabases(this.storeDatabases.isSelected());
    });
    SwingUtilities.invokeLater(this::checkDatabaseFileChooser);
  }

  private void checkDatabaseFileChooser() {
    boolean enabled = this.storeDatabases.isEnabled() && this.storeDatabases.isSelected();
    this.databasesDirectory.getBrowseAction().setEnabled(enabled);
  }

  private InputParameter getStoreAliasParameter() {
    this.storeAlias = new JCheckBox();
    this.storeAlias.addItemListener(e -> this.storeAliasChanged());

    return new InputParameter(PARAM_STORE_ALIAS_DESCRIPTION + ":", this.storeAlias, PARAM_STORE_ALIAS_HELP_GUI);
  }

  private void storeAliasChanged() {
    this.transformationProvider.setStoreAlias(isStoreAlias());
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
        .withClearSelectedFileOnShow(true)
        .build();
    this.databasesDirectory.addFileChooserListener(this::databasesDirectoryChanged);

    return new InputParameter(
      PARAM_STORE_DATABASES_DIRECTORY_DESCRIPTION + ":", this.databasesDirectory,
      PARAM_STORE_DATABASES_DIRECTORY_HELP_GUI
    );
  }

  private void databasesDirectoryChanged(ChangeEvent event) {
    invokeLater(() -> {
      File databasesDirectory = this.databasesDirectory.getSelectedFile();
      if (databasesDirectory == null) {
        this.transformationProvider.clearDatabasesDirectory();
      } else {
        this.transformationProvider.setDatabasesDirectory(databasesDirectory);
      }
    });
  }

  private InputParameter getAliasFileParameter() {
    this.aliasFile =
      JFileChooserPanelBuilder
        .createSaveJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withFileChooserSelectionMode(SelectionMode.FILES)
        .withLabel("")
        .withClearSelectedFileOnShow(true)
        .build();
    this.aliasFile.addFileChooserListener(this::aliasFileChanged);

    return new InputParameter(
      PARAM_STORE_ALIAS_FILE_DESCRIPTION + ":", this.aliasFile, PARAM_STORE_ALIAS_FILE_HELP_GUI
    );
  }

  private void aliasFileChanged(ChangeEvent event) {
    invokeLater(() -> {
      File aliasFile = this.aliasFile.getSelectedFile();
      if (aliasFile == null) {
        this.transformationProvider.clearAliasFile();
      } else {
        this.transformationProvider.setAliasFile(aliasFile);
      }
    });
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
      this.queryFileChanged();
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
        .withClearSelectedFileOnShow(true)
        .build();
    this.fileQuery.addFileChooserListener(this::externalFileQueryChanged);

    return new InputParameter("External file query:", this.fileQuery, HELP_QUERY_FILE);
  }

  private void externalFileQueryChanged(ChangeEvent event) {
    this.queryFileChanged();
  }

  private InputParameter getEvalueParameter() {
    this.eValue = new DoubleTextField(DEFAULT_EVALUE);
    this.eValue.getDocument().addDocumentListener(new RunnableDocumentAdapter(this::eValueChanged));

    return new InputParameter(PARAM_EVALUE_DESCRIPTION + ":", this.eValue, PARAM_EVALUE_HELP_GUI);
  }

  private void eValueChanged() {
    invokeLater(() -> {
      this.transformationProvider.setEvalue(this.eValue.getValue());
    });
  }

  private InputParameter getMaxTargetSeqsParameter() {
    this.maxTargetSeqs = new JIntegerTextField(DEFAULT_MAX_TARGET_SEQS);
    this.maxTargetSeqs.getDocument().addDocumentListener(new RunnableDocumentAdapter(this::maxTargetSeqsChanged));

    return new InputParameter(
      PARAM_MAX_TARGET_SEQS_DESCRIPTION + ":", this.maxTargetSeqs, PARAM_MAX_TARGET_SEQS_HELP_GUI
    );
  }
  
  private void maxTargetSeqsChanged() {
    invokeLater(() -> {
      this.transformationProvider.setMaxTargetSeqs(this.maxTargetSeqs.getValue());
    });
  }

  private InputParameter getAdditionalBlastParamsParameter() {
    this.additionalBlastParameters = new JXTextField("Additional parameters for BLAST");
    this.additionalBlastParameters.getDocument()
      .addDocumentListener(new RunnableDocumentAdapter(this::additionaParametersChanged));

    return new InputParameter(
      PARAM_ADDITIONAL_PARAMS_DESCRIPTION + ":", this.additionalBlastParameters, PARAM_ADDITIONAL_PARAMS_HELP_GUI
    );
  }

  private void additionaParametersChanged() {
    invokeLater(() -> {
      this.transformationProvider.setAdditionalParameters(this.additionalBlastParameters.getText());
    });
  }

  private InputParameter getExtractOnlyHitRegionsParamsParameter() {
    this.extractOnlyHitRegions = new JCheckBox();
    this.extractOnlyHitRegions.addItemListener(this::extractOnlyHiyRegionsChanged);

    return new InputParameter(
      PARAM_EXTRACT_HIT_REGIONS_SIZE_DESCRIPTION + ":", this.extractOnlyHitRegions, PARAM_EXTRACT_HIT_REGIONS_HELP
    );
  }

  private void extractOnlyHiyRegionsChanged(ItemEvent event) {
    this.hitRegionsWindowSize.setEnabled(isExtractOnlyHitRegions());
    invokeLater(() -> {
      this.transformationProvider.setExtractOnlyHitRegions(isExtractOnlyHitRegions());
    });
  }

  private boolean isExtractOnlyHitRegions() {
    return this.extractOnlyHitRegions.isSelected();
  }

  private InputParameter getHitRegionsWindowSizeParamsParameter() {
    this.hitRegionsWindowSize = new JIntegerTextField(DEFAULT_HIT_REGIONS_WINDOW_SIZE);
    this.hitRegionsWindowSize.setEnabled(isExtractOnlyHitRegions());
    this.hitRegionsWindowSize.getDocument()
      .addDocumentListener(new RunnableDocumentAdapter(this::hitRegionsWindowSizeChanged));

    return new InputParameter(
      PARAM_HIT_REGION_WINDOW_SIZE_DESCRIPTION + ":", this.hitRegionsWindowSize, PARAM_HIT_REGION_WINDOW_SIZE_HELP_GUI
    );
  }

  private void hitRegionsWindowSizeChanged() {
    invokeLater(() -> {
      this.transformationProvider.setHitRegionsWindowSize(this.hitRegionsWindowSize.getValue());
    });
  }

  public BlastTransformationProvider getTransformationProvider() {
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
    this.fileQueryComboboxModel.removeAllElements();

    List<String> selectedPaths = this.context.getSelectedPaths();
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

  private Optional<File> getQueryFile() {
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

  private boolean isStoreAlias() {
    return this.getDatabaseQueryMode().equals(DatabaseQueryMode.ALL) && this.storeAlias.isSelected();
  }

  public void setTransformationProvider(BlastTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    if (this.transformationProvider.getBlastBinariesExecutor() != null) {
      this.blastExecutionConfigurationPanel.setBinariesExecutor(this.transformationProvider.getBlastBinariesExecutor());
    }

    this.storeDatabases.setSelected(this.transformationProvider.isStoreDatabases());
    File databasesDirectory = this.transformationProvider.getDatabasesDirectory();
    if (databasesDirectory == null) {
      this.databasesDirectory.clearSelectedFile();
    } else {
      this.databasesDirectory.setSelectedFile(databasesDirectory);
    }

    this.storeAlias.setSelected(this.transformationProvider.isStoreAlias());
    File aliasFile = this.transformationProvider.getAliasFile();
    if (aliasFile == null) {
      this.aliasFile.clearSelectedFile();
    } else {
      this.aliasFile.setSelectedFile(aliasFile);
    }

    if (this.transformationProvider.getDatabaseQueryMode() != null) {
      this.databaseQueryModeRadioButtonsPanel.setSelectedItem(this.transformationProvider.getDatabaseQueryMode());
    }

    if (this.transformationProvider.getBlastType() != null) {
      this.blastTypeCombobox.setSelectedItem(this.transformationProvider.getBlastType());
    }

    if (this.transformationProvider.getQueryFile() != null) {
      this.fileQuery.setSelectedFile(this.transformationProvider.getQueryFile());
      this.queryTypeRadioButtonsPanel.setSelectedItem(QueryType.EXTERNAL);
    } else {
      this.fileQuery.clearSelectedFile();
    }

    this.eValue.setValue(this.transformationProvider.geteValue());
    this.maxTargetSeqs.setValue(this.transformationProvider.getMaxTargetSeqs());
    this.additionalBlastParameters.setText(this.transformationProvider.getAdditionalParameters());
    this.extractOnlyHitRegions.setSelected(this.transformationProvider.isExtractOnlyHitRegions());
    this.hitRegionsWindowSize.setValue(this.transformationProvider.getHitRegionsWindowSize());
  }
}
