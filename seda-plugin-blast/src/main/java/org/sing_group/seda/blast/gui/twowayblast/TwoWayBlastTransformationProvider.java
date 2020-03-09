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
package org.sing_group.seda.blast.gui.twowayblast;

import static org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationConfigurationChangeType.BLAST_ADDITONAL_PARAMETERS_CHANGED;
import static org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED;
import static org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationConfigurationChangeType.BLAST_TYPE_CHANGED;
import static org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationConfigurationChangeType.DATABASES_DIRECTORY_CHANGED;
import static org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationConfigurationChangeType.E_VALUE_CHANGED;
import static org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationConfigurationChangeType.QUERY_FILE_CHANGED;
import static org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationConfigurationChangeType.QUERY_MODE_CHANGED;
import static org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationConfigurationChangeType.STORE_DATABASES_CHANGED;

import java.io.File;
import java.util.Optional;

import org.sing_group.seda.blast.datatype.TwoWayBlastMode;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.transformation.dataset.TwoWayBlastTransformation;
import org.sing_group.seda.blast.transformation.dataset.TwoWayBlastTransformationBuilder;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class TwoWayBlastTransformationProvider extends AbstractTransformationProvider {

  private BlastBinariesExecutor blastBinariesExecutor;

  private boolean storeDatabases;
  private File databasesDirectory;

  private TwoWayBlastMode queryMode;
  private BlastType blastType;
  private File queryFile;
  private double eValue;
  private String additionalParameters;

  public TwoWayBlastTransformationProvider(
    TwoWayBlastMode queryMode, BlastType blastType, double eValue, String additionalParameters
  ) {
    this.queryMode = queryMode;
    this.blastType = blastType;
    this.eValue = eValue;
    this.additionalParameters = additionalParameters;
  }

  @Override
  public boolean isValidTransformation() {
    try {
      if (this.queryMode == null || this.blastType == null || this.queryFile == null) {
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

  private TwoWayBlastTransformation getBlastTransformation(DatatypeFactory factory) {
    TwoWayBlastTransformationBuilder builder =
      new TwoWayBlastTransformationBuilder(
        this.blastType,
        this.queryFile,
        this.queryMode
      )
        .withDatatypeFactory(factory)
        .withEvalue(this.eValue)
        .withBlastAditionalParameters(this.additionalParameters)
        .withBlastBinariesExecutor(this.blastBinariesExecutor);

    if (this.storeDatabases) {
      builder.withDatabasesDirectory(this.databasesDirectory);
    }

    return builder.build();
  }

  public void setBlastBinariesExecutor(Optional<BlastBinariesExecutor> blastBinariesExecutor) {
    this.blastBinariesExecutor = blastBinariesExecutor.orElse(null);
    fireTransformationsConfigurationModelEvent(BLAST_EXECUTOR_CHANGED, this.blastBinariesExecutor);
  }

  public void setStoreDatabases(boolean storeDatabases) {
    if (this.storeDatabases != storeDatabases) {
      this.storeDatabases = storeDatabases;
      fireTransformationsConfigurationModelEvent(STORE_DATABASES_CHANGED, this.storeDatabases);
    }
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

  public void setQueryMode(TwoWayBlastMode queryMode) {
    if (this.queryMode == null || !this.queryMode.equals(queryMode)) {
      this.queryMode = queryMode;
      fireTransformationsConfigurationModelEvent(QUERY_MODE_CHANGED, this.queryMode);
    }
  }

  public void setBlastType(BlastType blastType) {
    if (this.blastType == null || !this.blastType.equals(blastType)) {
      this.blastType = blastType;
      fireTransformationsConfigurationModelEvent(BLAST_TYPE_CHANGED, this.blastType);
    }
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

  public void setEvalue(double eValue) {
    if (this.eValue != eValue) {
      this.eValue = eValue;
      fireTransformationsConfigurationModelEvent(E_VALUE_CHANGED, this.eValue);
    }
  }

  public void setAdditionalParameters(String additionalParameters) {
    if (this.additionalParameters == null || !this.additionalParameters.equals(additionalParameters)) {
      this.additionalParameters = additionalParameters;
      fireTransformationsConfigurationModelEvent(BLAST_ADDITONAL_PARAMETERS_CHANGED, this.additionalParameters);
    }
  }
}
