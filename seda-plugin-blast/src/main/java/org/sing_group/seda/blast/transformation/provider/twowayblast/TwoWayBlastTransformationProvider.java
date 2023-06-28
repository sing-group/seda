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
package org.sing_group.seda.blast.transformation.provider.twowayblast;

import static org.sing_group.seda.blast.transformation.provider.twowayblast.TwoWayBlastTransformationConfigurationChangeType.BLAST_ADDITONAL_PARAMETERS_CHANGED;
import static org.sing_group.seda.blast.transformation.provider.twowayblast.TwoWayBlastTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED;
import static org.sing_group.seda.blast.transformation.provider.twowayblast.TwoWayBlastTransformationConfigurationChangeType.BLAST_TYPE_CHANGED;
import static org.sing_group.seda.blast.transformation.provider.twowayblast.TwoWayBlastTransformationConfigurationChangeType.DATABASES_DIRECTORY_CHANGED;
import static org.sing_group.seda.blast.transformation.provider.twowayblast.TwoWayBlastTransformationConfigurationChangeType.E_VALUE_CHANGED;
import static org.sing_group.seda.blast.transformation.provider.twowayblast.TwoWayBlastTransformationConfigurationChangeType.NUM_THREADS_CHANGED;
import static org.sing_group.seda.blast.transformation.provider.twowayblast.TwoWayBlastTransformationConfigurationChangeType.QUERY_FILE_CHANGED;
import static org.sing_group.seda.blast.transformation.provider.twowayblast.TwoWayBlastTransformationConfigurationChangeType.QUERY_MODE_CHANGED;
import static org.sing_group.seda.blast.transformation.provider.twowayblast.TwoWayBlastTransformationConfigurationChangeType.STORE_DATABASES_CHANGED;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.blast.datatype.TwoWayBlastMode;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.transformation.dataset.TwoWayBlastTransformation;
import org.sing_group.seda.blast.transformation.dataset.TwoWayBlastTransformationBuilder;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
public class TwoWayBlastTransformationProvider extends AbstractTransformationProvider {

  @XmlAnyElement(lax = true)
  private BlastBinariesExecutor blastBinariesExecutor;

  @XmlElement
  private boolean storeDatabases;
  @XmlElement
  private File databasesDirectory;

  @XmlElement
  private TwoWayBlastMode queryMode;
  @XmlElement
  private BlastType blastType;
  @XmlElement
  private File queryFile;
  @XmlElement
  private double eValue;
  @XmlElement
  private String additionalParameters;
  @XmlElement
  private int numThreads;

  public TwoWayBlastTransformationProvider() {}

  public TwoWayBlastTransformationProvider(
    TwoWayBlastMode queryMode, BlastType blastType, double eValue, String additionalParameters, int numThreads
  ) {
    this.queryMode = queryMode;
    this.blastType = blastType;
    this.eValue = eValue;
    this.additionalParameters = additionalParameters;
    this.numThreads = numThreads;
  }
  
  @Override
  public Validation validate() {
    try {
      List<String> validationErrors = new LinkedList<String>();

      if (this.queryMode == null) {
        validationErrors.add("The database query mode can't be null");
      }

      if (this.blastType == null) {
        validationErrors.add("The blast type mode can't be null");
      }

      if (this.queryFile == null) {
        validationErrors.add("The query file can't be null");
      }

      if (this.numThreads < 1) {
        validationErrors.add("The number of threads can't be less than one");
      }
      
      if (this.storeDatabases && this.databasesDirectory == null) {
        validationErrors.add("The databases directory can't be null");
      }

      if (!isValidBlastBinariesExecutor()) {
        validationErrors.add("The BLAST binaries executor is not valid");
      }

      if (validationErrors.isEmpty()) {
        getBlastTransformation(DatatypeFactory.getDefaultDatatypeFactory());

        return new DefaultValidation();
      } else {
        return new DefaultValidation(validationErrors);
      }
    } catch (RuntimeException ex) {
      return new DefaultValidation(ex.toString());
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
        .withNumThreads(this.numThreads)
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

  public void setQueryMode(TwoWayBlastMode queryMode) {
    if (this.queryMode == null || !this.queryMode.equals(queryMode)) {
      this.queryMode = queryMode;
      fireTransformationsConfigurationModelEvent(QUERY_MODE_CHANGED, this.queryMode);
    }
  }

  public TwoWayBlastMode getQueryMode() {
    return queryMode;
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

  public void setAdditionalParameters(String additionalParameters) {
    if (this.additionalParameters == null || !this.additionalParameters.equals(additionalParameters)) {
      this.additionalParameters = additionalParameters;
      fireTransformationsConfigurationModelEvent(BLAST_ADDITONAL_PARAMETERS_CHANGED, this.additionalParameters);
    }
  }

  public String getAdditionalParameters() {
    return additionalParameters == null ? "" : additionalParameters;
  }

  public void setNumThreads(int numThreads) {
    if (this.numThreads != numThreads) {
      this.numThreads = numThreads;
      fireTransformationsConfigurationModelEvent(NUM_THREADS_CHANGED, this.numThreads);
    }
  }

  public int getNumThreads() {
    return this.numThreads;
  }
}
