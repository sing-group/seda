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
package org.sing_group.seda.gui.ncbi;

import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.FILE_DELIMITER;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.FILE_POSITION;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.NCBI_TAXONOMY_DELIMITER;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.NCBI_TAXONOMY_FIELDS;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.REPLACEMENTS_MAP_FILE;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.REPLACEMENT_STRING;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.REPLACE_BLANK_SPACES;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.REPLACE_SPECIAL_CHARACTERS;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.SAVE_REPLACEMENTS_MAP;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.SEQUENCE_ADD_INDEX;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.SEQUENCE_DELIMITER;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.SEQUENCE_INDEX_DELIMITER;
import static org.sing_group.seda.gui.ncbi.NcbiRenameTransformationChangeType.SEQUENCE_POSITION;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.List;
import java.util.Optional;

import org.sing_group.seda.core.ncbi.NcbiAssemblyAccesionResolver;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyConfiguration;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyFields;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.rename.FileRenameConfiguration;
import org.sing_group.seda.datatype.rename.ReplaceCharacterConfiguration;
import org.sing_group.seda.datatype.rename.SequenceHeaderRenameConfiguration;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation.RenameMode;
import org.sing_group.seda.transformation.dataset.NcbiRenameSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.util.NetUtils;

public class NcbiRenameTransformationProvider extends AbstractTransformationProvider {

  private FileRenameConfigurationPanel fileRenamePanel;
  private SequenceHeaderRenameConfigurationPanel sequenceHeaderRenamePanel;
  private ReplaceOptionsConfigurationPanel replaceOptionsConfigurationPanel;
  private NcbiTaxonomyConfigurationPanel ncbiTaxonomyConfigurationPanel;

  public NcbiRenameTransformationProvider(
    FileRenameConfigurationPanel fileRenamePanel,
    SequenceHeaderRenameConfigurationPanel sequenceHeaderRenamePanel,
    ReplaceOptionsConfigurationPanel replaceOptionsConfigurationPanel,
    NcbiTaxonomyConfigurationPanel ncbiTaxonomyConfigurationPanel
  ) {
    this.fileRenamePanel = fileRenamePanel;
    this.sequenceHeaderRenamePanel = sequenceHeaderRenamePanel;
    this.replaceOptionsConfigurationPanel = replaceOptionsConfigurationPanel;
    this.ncbiTaxonomyConfigurationPanel = ncbiTaxonomyConfigurationPanel;

    this.addListeners();
  }

  private void addListeners() {
    this.fileRenamePanel.addPropertyChangeListener(this::fileRenamePropertyChanged);
    this.sequenceHeaderRenamePanel.addPropertyChangeListener(this::sequenceHeaderRenamePropertyChanged);
    this.replaceOptionsConfigurationPanel.addPropertyChangeListener(this::replaceOptionsConfigurationPropertyChanged);
    this.ncbiTaxonomyConfigurationPanel.addPropertyChangeListener(this::ncbiTaxonomyConfigurationPropertyChanged);
  }

  private void fileRenamePropertyChanged(PropertyChangeEvent event) {
    switch (event.getPropertyName()) {
      case FileRenameConfigurationPanel.PROPERTY_POSITION:
        fireTransformationsConfigurationModelEvent(FILE_POSITION, this.getFilePosition());
        break;
      case FileRenameConfigurationPanel.PROPERTY_DELIMITER:
        fireTransformationsConfigurationModelEvent(FILE_DELIMITER, this.getFileDelimiter());
        break;
    }
  }

  private String getFileDelimiter() {
    return this.fileRenamePanel.getDelimiter();
  }

  private RenameMode getFilePosition() {
    return this.fileRenamePanel.getPosition();
  }

  private void sequenceHeaderRenamePropertyChanged(PropertyChangeEvent event) {
    switch (event.getPropertyName()) {
      case SequenceHeaderRenameConfigurationPanel.PROPERTY_POSITION:
        fireTransformationsConfigurationModelEvent(SEQUENCE_POSITION, this.getSequencePosition());
        break;
      case SequenceHeaderRenameConfigurationPanel.PROPERTY_DELIMITER:
        fireTransformationsConfigurationModelEvent(SEQUENCE_DELIMITER, this.getSequenceDelimiter());
        break;
      case SequenceHeaderRenameConfigurationPanel.PROPERTY_ADD_INDEX:
        fireTransformationsConfigurationModelEvent(SEQUENCE_ADD_INDEX, this.isSequenceAddIndex());
        break;
      case SequenceHeaderRenameConfigurationPanel.PROPERTY_INDEX_DELIMITER:
        fireTransformationsConfigurationModelEvent(SEQUENCE_INDEX_DELIMITER, this.getSequenceIndexDelimiter());
        break;
    }
  }

  private boolean isSequenceAddIndex() {
    return this.sequenceHeaderRenamePanel.isAddIndex();
  }

  private String getSequenceDelimiter() {
    return this.sequenceHeaderRenamePanel.getDelimiter();
  }

  private String getSequenceIndexDelimiter() {
    return this.sequenceHeaderRenamePanel.getIndexDelimiter();
  }

  private RenameMode getSequencePosition() {
    return this.sequenceHeaderRenamePanel.getPosition();
  }

  private void replaceOptionsConfigurationPropertyChanged(PropertyChangeEvent event) {
    switch (event.getPropertyName()) {
      case ReplaceOptionsConfigurationPanel.PROPERTY_REPLACE_BLANK_SPACES:
        fireTransformationsConfigurationModelEvent(REPLACE_BLANK_SPACES, this.isReplaceblankSpaces());
        break;
      case ReplaceOptionsConfigurationPanel.PROPERTY_REPLACE_SPECIAL_CHARACTERS:
        fireTransformationsConfigurationModelEvent(REPLACE_SPECIAL_CHARACTERS, this.isReplaceSpecialCharacters());
        break;
      case ReplaceOptionsConfigurationPanel.PROPERTY_REPLACEMENT_STRING:
        fireTransformationsConfigurationModelEvent(REPLACEMENT_STRING, this.getReplacementString());
        break;
      case ReplaceOptionsConfigurationPanel.PROPERTY_REPLACEMENTS_MAP_FILE:
        fireTransformationsConfigurationModelEvent(REPLACEMENTS_MAP_FILE, this.getReplacementsMapFile());
        break;
      case ReplaceOptionsConfigurationPanel.PROPERTY_SAVE_REPLACEMENTS_MAP:
        fireTransformationsConfigurationModelEvent(SAVE_REPLACEMENTS_MAP, this.isSaveReplacementsMap());
        break;
    }
  }

  private boolean isReplaceblankSpaces() {
    return this.replaceOptionsConfigurationPanel.isReplaceBlankSpaces();
  }

  private boolean isReplaceSpecialCharacters() {
    return this.replaceOptionsConfigurationPanel.isReplaceSpecialCharacters();
  }

  private String getReplacementString() {
    return this.replaceOptionsConfigurationPanel.getReplacementString();
  }

  private File getReplacementsMapFile() {
    return this.replaceOptionsConfigurationPanel.getReplacementsMapFile();
  }

  private boolean isSaveReplacementsMap() {
    return this.replaceOptionsConfigurationPanel.isSaveReplacementsMap();
  }

  private void ncbiTaxonomyConfigurationPropertyChanged(PropertyChangeEvent event) {
    switch (event.getPropertyName()) {
      case NcbiTaxonomyConfigurationPanel.PROPERTY_DELIMITER:
        fireTransformationsConfigurationModelEvent(NCBI_TAXONOMY_DELIMITER, this.getNcbiTaxonomyDelimiter());
        break;
      case NcbiTaxonomyConfigurationPanel.PROPERTY_FIELDS:
        fireTransformationsConfigurationModelEvent(NCBI_TAXONOMY_FIELDS, this.getNcbiTaxonomyFields());
        break;
    }
  }

  private String getNcbiTaxonomyDelimiter() {
    return this.ncbiTaxonomyConfigurationPanel.getDelimiter();
  }

  private List<NcbiTaxonomyFields> getNcbiTaxonomyFields() {
    return this.ncbiTaxonomyConfigurationPanel.getFields();
  }

  @Override
  public boolean isValidTransformation() {
    return this.isNetAvailable()
      && this.fileRenamePanel.isValidInput()
      && this.sequenceHeaderRenamePanel.isValidInput()
      && this.replaceOptionsConfigurationPanel.isValidInput()
      && this.ncbiTaxonomyConfigurationPanel.isValidInput();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    if (isSaveReplacementsMap()) {
      return new NcbiRenameSequencesGroupDatasetTransformation(
        factory,
        getFileNameConfiguration(), getHeaderConfiguration(), getTaxonomyConfiguration(), getReplacementsMapFile()
      );
    } else {
      return new NcbiRenameSequencesGroupDatasetTransformation(
        factory, getFileNameConfiguration(), getHeaderConfiguration(), getTaxonomyConfiguration()
      );
    }
  }

  private FileRenameConfiguration getFileNameConfiguration() {
    return new FileRenameConfiguration(getFilePosition(), getFileDelimiter(), getReplaceCharacterConfiguration());
  }

  private ReplaceCharacterConfiguration getReplaceCharacterConfiguration() {
    return new ReplaceCharacterConfiguration(
      isReplaceblankSpaces(), isReplaceSpecialCharacters(), getReplacementString()
    );
  }

  private SequenceHeaderRenameConfiguration getHeaderConfiguration() {
    return new SequenceHeaderRenameConfiguration(
      getSequencePosition(), getSequenceDelimiter(), isSequenceAddIndex(), getSequenceIndexDelimiter(),
      getReplaceCharacterConfiguration()
    );
  }

  private NcbiTaxonomyConfiguration getTaxonomyConfiguration() {
    List<NcbiTaxonomyFields> fields = getNcbiTaxonomyFields();

    return new NcbiTaxonomyConfiguration(
      getNcbiTaxonomyDelimiter(), fields.toArray(new NcbiTaxonomyFields[fields.size()])
    );
  }

  private boolean isNetAvailable() {
    return NetUtils.isHostAvailable(NcbiAssemblyAccesionResolver.NCBI_URL.replace("https://", ""));
  }

  public Optional<String> getGenerateButtonTooltipMessage() {
    if (isNetAvailable()) {
      return Optional.empty();
    }
    return Optional.of("Can't connect to the NCBI service. Please, check your internet connection.");
  }
}
