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

import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.ALIAS_FILE_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.BLAST_ADDITONAL_PARAMETERS_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.BLAST_TYPE_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.DATABASES_DIRECTORY_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.DATABASE_QUERY_MODE_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.EXTRACT_ONLY_HIT_REGIONS_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.E_VALUE_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.HIT_REGIONS_WINDOW_SIZE_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.MAX_TARGET_SEQS_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.QUERY_FILE_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.STORE_ALIAS_CHANGED;
import static org.sing_group.seda.blast.gui.BlastTransformationConfigurationChangeType.STORE_DATABASES_CHANGED;

import java.io.File;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.blast.datatype.DatabaseQueryMode;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.transformation.dataset.BlastTransformation;
import org.sing_group.seda.blast.transformation.dataset.BlastTransformationBuilder;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
public class BlastTransformationProvider extends AbstractTransformationProvider {

  @XmlAnyElement(lax = true)
  private BlastBinariesExecutor blastBinariesExecutor;

  @XmlElement
  private boolean storeDatabases;
  @XmlElement
  private File databasesDirectory;
  @XmlElement
  private boolean storeAlias;
  @XmlElement
  private File aliasFile;

  @XmlElement
  private DatabaseQueryMode databaseQueryMode;
  @XmlElement
  private BlastType blastType;
  @XmlElement
  private File queryFile;
  @XmlElement
  private double eValue;
  @XmlElement
  private int maxTargetSeqs;
  @XmlElement
  private String additionalParameters;
  @XmlElement
  private boolean extractOnlyHitRegions;
  @XmlElement
  private int hitRegionsWindowSize;

  public BlastTransformationProvider() {}

  public BlastTransformationProvider(
    DatabaseQueryMode databaseQueryMode, BlastType blastType, Double eValue, Integer maxTargetSeqs,
    boolean extractOnlyHitRegions, int hitRegionsWindowSize
  ) {
    this.databaseQueryMode = databaseQueryMode;
    this.blastType = blastType;
    this.eValue = eValue;
    this.maxTargetSeqs = maxTargetSeqs;
    this.extractOnlyHitRegions = extractOnlyHitRegions;
    this.hitRegionsWindowSize = hitRegionsWindowSize;
  }

  @Override
  public boolean isValidTransformation() {
    try {
      if (this.databaseQueryMode == null || this.blastType == null || this.queryFile == null) {
        return false;
      }

      if (this.storeAlias && this.aliasFile == null) {
        return false;
      }

      if (this.storeDatabases && this.databasesDirectory == null) {
        return false;
      }

      if (!isValidBlastBinariesExecutor()) {
        return false;
      }

      getBlastTransformation(DatatypeFactory.getDefaultDatatypeFactory());

      return true;
    } catch (RuntimeException ex) {
      return false;
    }
  }

  private boolean isValidBlastBinariesExecutor() {
    if (this.blastBinariesExecutor == null) {
      return false;
    }

    try {
      this.blastBinariesExecutor.checkBinary();

      return true;
    } catch (BinaryCheckException e) {
      return false;
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(
    DatatypeFactory factory
  ) {
    return getBlastTransformation(factory);
  }

  private BlastTransformation getBlastTransformation(DatatypeFactory factory) {
    BlastTransformationBuilder builder =
      new BlastTransformationBuilder(
        this.blastType,
        this.queryFile,
        this.databaseQueryMode
      )
        .withDatatypeFactory(factory)
        .withMaxTargetSeqs(this.maxTargetSeqs)
        .withEvalue(this.eValue)
        .withBlastAditionalParameters(this.additionalParameters == null ? "" : this.additionalParameters)
        .withBlastBinariesExecutor(this.blastBinariesExecutor);

    if (this.storeDatabases) {
      builder.withDatabasesDirectory(this.databasesDirectory);
    }

    if (this.storeAlias && this.aliasFile != null) {
      builder.withAliasFile(this.aliasFile);
    }

    if (this.extractOnlyHitRegions) {
      builder
        .withExtractOnlyHitRegions(this.extractOnlyHitRegions)
        .withHitRegionsWindowSize(this.hitRegionsWindowSize);
    }

    return builder.build();
  }

  public void setBlastBinariesExecutor(Optional<BlastBinariesExecutor> blastBinariesExecutor) {
    this.blastBinariesExecutor = blastBinariesExecutor.orElse(null);
    fireTransformationsConfigurationModelEvent(BLAST_EXECUTOR_CHANGED, this.blastBinariesExecutor);
  }

  public BlastBinariesExecutor getBlastBinariesExecutor() {
    return blastBinariesExecutor;
  }

  public void setStoreDatabases(boolean storeDatabases) {
    if (this.storeDatabases != storeDatabases) {
      this.storeDatabases = storeDatabases;
      fireTransformationsConfigurationModelEvent(STORE_DATABASES_CHANGED, this.storeDatabases);
    }
  }

  public boolean isStoreDatabases() {
    return storeDatabases;
  }

  public void clearDatabasesDirectory() {
    this.databasesDirectory = null;
    fireTransformationsConfigurationModelEvent(DATABASES_DIRECTORY_CHANGED, this.databasesDirectory);
  }

  public void setDatabasesDirectory(File databasesDirectory) {
    if (this.databasesDirectory == null || !this.databasesDirectory.equals(databasesDirectory)) {
      this.databasesDirectory = databasesDirectory;
      fireTransformationsConfigurationModelEvent(DATABASES_DIRECTORY_CHANGED, this.databasesDirectory);
    }
  }

  public File getDatabasesDirectory() {
    return databasesDirectory;
  }

  public void setStoreAlias(boolean storeAlias) {
    if (this.storeAlias != storeAlias) {
      this.storeAlias = storeAlias;
      fireTransformationsConfigurationModelEvent(STORE_ALIAS_CHANGED, this.storeAlias);
    }
  }

  public boolean isStoreAlias() {
    return storeAlias;
  }

  public void clearAliasFile() {
    this.aliasFile = null;
    fireTransformationsConfigurationModelEvent(ALIAS_FILE_CHANGED, this.aliasFile);
  }

  public void setAliasFile(File aliasFile) {
    if (this.aliasFile == null || !this.aliasFile.equals(aliasFile)) {
      this.aliasFile = aliasFile;
      fireTransformationsConfigurationModelEvent(ALIAS_FILE_CHANGED, this.aliasFile);
    }
  }

  public File getAliasFile() {
    return aliasFile;
  }

  public void setDatabaseQueryMode(DatabaseQueryMode databaseQueryMode) {
    if (this.databaseQueryMode == null || !this.databaseQueryMode.equals(databaseQueryMode)) {
      this.databaseQueryMode = databaseQueryMode;
      fireTransformationsConfigurationModelEvent(DATABASE_QUERY_MODE_CHANGED, this.databaseQueryMode);
    }
  }

  public DatabaseQueryMode getDatabaseQueryMode() {
    return databaseQueryMode;
  }

  public void setBlastType(BlastType blastType) {
    if (this.blastType == null || !this.blastType.equals(blastType)) {
      this.blastType = blastType;
      fireTransformationsConfigurationModelEvent(BLAST_TYPE_CHANGED, this.blastType);
    }
  }

  public BlastType getBlastType() {
    return blastType;
  }

  public void clearQueryFile() {
    this.queryFile = null;
    fireTransformationsConfigurationModelEvent(QUERY_FILE_CHANGED, this.queryFile);
  }

  public void setQueryFile(File queryFile) {
    if (this.queryFile == null || !this.queryFile.equals(queryFile)) {
      this.queryFile = queryFile;
      fireTransformationsConfigurationModelEvent(QUERY_FILE_CHANGED, this.queryFile);
    }
  }

  public File getQueryFile() {
    return queryFile;
  }

  public void setEvalue(double eValue) {
    if (this.eValue != eValue) {
      this.eValue = eValue;
      fireTransformationsConfigurationModelEvent(E_VALUE_CHANGED, this.eValue);
    }
  }

  public double geteValue() {
    return eValue;
  }

  public void setMaxTargetSeqs(int maxTargetSeqs) {
    if (this.maxTargetSeqs != maxTargetSeqs) {
      this.maxTargetSeqs = maxTargetSeqs;
      fireTransformationsConfigurationModelEvent(MAX_TARGET_SEQS_CHANGED, this.maxTargetSeqs);
    }
  }

  public int getMaxTargetSeqs() {
    return maxTargetSeqs;
  }

  public void setAdditionalParameters(String additionalParameters) {
    if (this.additionalParameters == null || !this.additionalParameters.equals(additionalParameters)) {
      this.additionalParameters = additionalParameters;
      fireTransformationsConfigurationModelEvent(BLAST_ADDITONAL_PARAMETERS_CHANGED, this.additionalParameters);
    }
  }

  public String getAdditionalParameters() {
    return additionalParameters == null ? "" : additionalParameters;
  }

  public void setExtractOnlyHitRegions(boolean extractOnlyHitRegions) {
    if (this.extractOnlyHitRegions != extractOnlyHitRegions) {
      this.extractOnlyHitRegions = extractOnlyHitRegions;
      fireTransformationsConfigurationModelEvent(EXTRACT_ONLY_HIT_REGIONS_CHANGED, this.extractOnlyHitRegions);
    }
  }

  public boolean isExtractOnlyHitRegions() {
    return extractOnlyHitRegions;
  }

  public void setHitRegionsWindowSize(int hitRegionsWindowSize) {
    if (this.hitRegionsWindowSize != hitRegionsWindowSize) {
      this.hitRegionsWindowSize = hitRegionsWindowSize;
      fireTransformationsConfigurationModelEvent(HIT_REGIONS_WINDOW_SIZE_CHANGED, this.hitRegionsWindowSize);
    }
  }

  public int getHitRegionsWindowSize() {
    return hitRegionsWindowSize;
  }
}
