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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.ncbi.NcbiAssemblyAccesionResolver;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyConfiguration;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyFields;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.rename.FileRenameConfiguration;
import org.sing_group.seda.datatype.rename.ReplaceCharacterConfiguration;
import org.sing_group.seda.datatype.rename.SequenceHeaderRenameConfiguration;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation.RenameMode;
import org.sing_group.seda.transformation.dataset.NcbiRenameSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.util.NetUtils;

@XmlRootElement
public class NcbiRenameTransformationProvider extends AbstractTransformationProvider {

  @XmlElement
  private RenameMode filePosition;
  @XmlElement
  private String fileDelimiter;

  @XmlElement
  private RenameMode sequencePosition;
  @XmlElement
  private String sequenceDelimiter;
  @XmlElement
  private boolean sequenceAddIndex;
  @XmlElement
  private String sequenceIndexDelimiter;

  @XmlElement
  private boolean replaceBlankSpaces;
  @XmlElement
  private boolean replaceSpecialCharacters;
  @XmlElement
  private String replacementString;
  @XmlElement
  private boolean saveReplacementsMap;
  @XmlElement
  private File replacementsMapFile;

  @XmlElement
  private String ncbiTaxonomyDelimiter;
  @XmlElement
  private List<NcbiTaxonomyFields> ncbiTaxonomyFields;

  public NcbiRenameTransformationProvider() {}

  public NcbiRenameTransformationProvider(
    RenameMode filePosition, String fileDelimiter, RenameMode sequencePosition, String sequenceDelimiter,
    boolean sequenceAddIndex, String sequenceIndexDelimiter, boolean replaceBlankSpaces,
    boolean replaceSpecialCharacters, String replacementString, boolean saveReplacementsMap, File replacementsMapFile,
    String ncbiTaxonomyDelimiter, List<NcbiTaxonomyFields> ncbiTaxonomyFields
  ) {
    this.filePosition = filePosition;
    this.fileDelimiter = fileDelimiter;

    this.sequencePosition = sequencePosition;
    this.sequenceDelimiter = sequenceDelimiter;
    this.sequenceAddIndex = sequenceAddIndex;
    this.sequenceIndexDelimiter = sequenceIndexDelimiter;

    this.replaceBlankSpaces = replaceBlankSpaces;
    this.replaceSpecialCharacters = replaceSpecialCharacters;
    this.replacementString = replacementString;
    this.saveReplacementsMap = saveReplacementsMap;
    this.replacementsMapFile = replacementsMapFile;

    this.ncbiTaxonomyDelimiter = ncbiTaxonomyDelimiter;
    this.ncbiTaxonomyFields = ncbiTaxonomyFields;
  }

  public void setFileDelimiter(String fileDelimiter) {
    if (this.fileDelimiter == null || !this.fileDelimiter.equals(fileDelimiter)) {
      this.fileDelimiter = fileDelimiter;
      fireTransformationsConfigurationModelEvent(FILE_DELIMITER, this.fileDelimiter);
    }
  }

  public String getFileDelimiter() {
    return this.fileDelimiter;
  }

  public void setFilePosition(RenameMode filePosition) {
    if (this.filePosition == null || !this.filePosition.equals(filePosition)) {
      this.filePosition = filePosition;
      fireTransformationsConfigurationModelEvent(FILE_POSITION, this.filePosition);
    }
  }

  public RenameMode getFilePosition() {
    return this.filePosition;
  }

  public void setSequenceAddIndex(boolean sequenceAddIndex) {
    if (this.sequenceAddIndex != sequenceAddIndex) {
      this.sequenceAddIndex = sequenceAddIndex;
      fireTransformationsConfigurationModelEvent(SEQUENCE_ADD_INDEX, this.sequenceAddIndex);
    }
  }

  public boolean isSequenceAddIndex() {
    return this.sequenceAddIndex;
  }

  public void setSequenceDelimiter(String sequenceDelimiter) {
    if (this.sequenceDelimiter == null || !this.sequenceDelimiter.equals(sequenceDelimiter)) {
      this.sequenceDelimiter = sequenceDelimiter;
      fireTransformationsConfigurationModelEvent(SEQUENCE_DELIMITER, this.sequenceDelimiter);
    }
  }

  public String getSequenceDelimiter() {
    return this.sequenceDelimiter;
  }

  public void setSequenceIndexDelimiter(String sequenceIndexDelimiter) {
    if (this.sequenceIndexDelimiter == null || !this.sequenceIndexDelimiter.equals(sequenceIndexDelimiter)) {
      this.sequenceIndexDelimiter = sequenceIndexDelimiter;
      fireTransformationsConfigurationModelEvent(SEQUENCE_INDEX_DELIMITER, this.sequenceIndexDelimiter);
    }
  }

  public String getSequenceIndexDelimiter() {
    return this.sequenceIndexDelimiter;
  }

  public void setSequencePosition(RenameMode sequencePosition) {
    if (this.sequencePosition == null || !this.sequencePosition.equals(sequencePosition)) {
      this.sequencePosition = sequencePosition;
      fireTransformationsConfigurationModelEvent(SEQUENCE_POSITION, this.sequencePosition);
    }
  }

  public RenameMode getSequencePosition() {
    return this.sequencePosition;
  }

  public void setReplaceBlankSpaces(boolean replaceBlankSpaces) {
    if (this.replaceBlankSpaces != replaceBlankSpaces) {
      this.replaceBlankSpaces = replaceBlankSpaces;
      fireTransformationsConfigurationModelEvent(REPLACE_BLANK_SPACES, this.replaceBlankSpaces);
    }
  }

  public boolean isReplaceBlankSpaces() {
    return this.replaceBlankSpaces;
  }

  public void setReplaceSpecialCharacters(boolean replaceSpecialCharacters) {
    if (this.replaceSpecialCharacters != replaceSpecialCharacters) {
      this.replaceSpecialCharacters = replaceSpecialCharacters;
      fireTransformationsConfigurationModelEvent(REPLACE_SPECIAL_CHARACTERS, this.replaceSpecialCharacters);
    }
  }

  public boolean isReplaceSpecialCharacters() {
    return this.replaceSpecialCharacters;
  }

  public void setReplacementString(String replacementString) {
    if (this.replacementString == null || !this.replacementString.equals(replacementString)) {
      this.replacementString = replacementString;
      fireTransformationsConfigurationModelEvent(REPLACEMENT_STRING, this.replacementString);
    }
  }

  public String getReplacementString() {
    return this.replacementString;
  }

  public void clearReplacementsMapFile() {
    this.replacementsMapFile = null;
    fireTransformationsConfigurationModelEvent(REPLACEMENTS_MAP_FILE, this.replacementsMapFile);
  }

  public void setReplacementsMapFile(File replacementsMapFile) {
    if (this.replacementsMapFile == null || !this.replacementsMapFile.equals(replacementsMapFile)) {
      this.replacementsMapFile = replacementsMapFile;
      fireTransformationsConfigurationModelEvent(REPLACEMENTS_MAP_FILE, this.replacementsMapFile);
    }
  }

  public File getReplacementsMapFile() {
    return this.replacementsMapFile;
  }

  public void setSaveReplacementsMap(boolean saveReplacementsMap) {
    if (this.saveReplacementsMap != saveReplacementsMap) {
      this.saveReplacementsMap = saveReplacementsMap;
      fireTransformationsConfigurationModelEvent(SAVE_REPLACEMENTS_MAP, this.isSaveReplacementsMap());
    }
  }

  public boolean isSaveReplacementsMap() {
    return this.saveReplacementsMap;
  }

  public void setNcbiTaxonomyDelimiter(String ncbiTaxonomyDelimiter) {
    if (this.ncbiTaxonomyDelimiter == null || !this.ncbiTaxonomyDelimiter.equals(ncbiTaxonomyDelimiter)) {
      this.ncbiTaxonomyDelimiter = ncbiTaxonomyDelimiter;
      fireTransformationsConfigurationModelEvent(NCBI_TAXONOMY_DELIMITER, this.ncbiTaxonomyDelimiter);
    }
  }

  public String getNcbiTaxonomyDelimiter() {
    return this.ncbiTaxonomyDelimiter;
  }

  public void setNcbiTaxonomyFields(List<NcbiTaxonomyFields> ncbiTaxonomyFields) {
    if (this.ncbiTaxonomyFields == null || !this.ncbiTaxonomyFields.equals(ncbiTaxonomyFields)) {
      this.ncbiTaxonomyFields = ncbiTaxonomyFields;
      fireTransformationsConfigurationModelEvent(NCBI_TAXONOMY_FIELDS, this.ncbiTaxonomyFields);
    }
  }

  public List<NcbiTaxonomyFields> getNcbiTaxonomyFields() {
    return this.ncbiTaxonomyFields;
  }

  @Override
  public Validation validate() {
    List<String> errorList = new ArrayList<>();
    if (!this.isNetAvailable()) {
      errorList.add("Net is not available");
    }
    if (this.filePosition == null) {
      errorList.add("File position is not set");
    }
    if (this.sequencePosition == null) {
      errorList.add("Sequence position is not set");
    }
    if (this.saveReplacementsMap && this.replacementsMapFile == null) {
      errorList.add("Replacements save map is active but file is not set");
    }

    if (errorList.isEmpty()) {
      return new DefaultValidation();
    } else {
      return new DefaultValidation(errorList);
    }

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
      isReplaceBlankSpaces(), isReplaceSpecialCharacters(), getReplacementString()
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
