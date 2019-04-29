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
package org.sing_group.seda.blast.gui;

import org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationConfigurationChangeType;
import org.sing_group.seda.blast.transformation.dataset.BlastTransformation;
import org.sing_group.seda.blast.transformation.dataset.BlastTransformationBuilder;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class BlastTransformationProvider extends AbstractTransformationProvider {

  private BlastTransformationConfigurationPanel configurationPanel;

  public BlastTransformationProvider(BlastTransformationConfigurationPanel configurationPanel) {
    this.configurationPanel = configurationPanel;
  }

  @Override
  public boolean isValidTransformation() {
    try {
      if (this.configurationPanel.isStoreAlias() && !this.configurationPanel.getAliasFile().isPresent()) {
        return false;
      }

      if (this.configurationPanel.isStoreDatabases() && this.configurationPanel.getDatabasesDirectory() == null) {
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
    if (!this.configurationPanel.getBlastBinariesExecutor().isPresent()) {
      return false;
    }

    try {
      this.configurationPanel.getBlastBinariesExecutor().get().checkBinary();

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
    BlastTransformationBuilder builder = new BlastTransformationBuilder(
      configurationPanel.getBlastType(),
      configurationPanel.getQueryFile().get(),
      configurationPanel.getDatabaseQueryMode()
    )
      .withDatatypeFactory(factory)
      .withMaxTargetSeqs(configurationPanel.getMaxTargetSeqs())
      .withEvalue(configurationPanel.getEvalue())
      .withBlastAditionalParameters(configurationPanel.getBlastAditionalParameters())
      .withBlastBinariesExecutor(configurationPanel.getBlastBinariesExecutor().get());

    if (configurationPanel.isStoreDatabases()) {
      builder.withDatabasesDirectory(configurationPanel.getDatabasesDirectory());
    }

    if (configurationPanel.isStoreAlias() && configurationPanel.getAliasFile().isPresent()) {
      builder.withAliasFileDirectory(configurationPanel.getAliasFile().get());
    }

    if (configurationPanel.isExtractOnlyHitRegions()) {
      builder
        .withExtractOnlyHitRegions(configurationPanel.isExtractOnlyHitRegions())
        .withHitRegionsWindowSize(configurationPanel.getHitRegionsWindowSize());
    }

    return builder.build();
  }

  public void blastPathChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.BLAST_PATH_CHANGED, configurationPanel.getBlastPath()
    );
  }

  public void blastExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED, configurationPanel.getBlastBinariesExecutor()
    );
  }

  public void storeDatabasesChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.STORE_DATABASES_CHANGED, configurationPanel.isStoreDatabases()
    );
  }

  public void databasesDirectoryChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.DATABASES_DIRECTORY_CHANGED, configurationPanel.getDatabasesDirectory()
    );
  }

  public void storeAliasChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.STORE_ALIAS_CHANGED, configurationPanel.isStoreAlias()
    );
  }

  public void aliasFileChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.ALIAS_FILE_CHANGED, configurationPanel.getAliasFile()
    );
  }

  public void blastTypeChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.BLAST_TYPE_CHANGED, configurationPanel.getBlastType()
    );
  }

  public void databaseQueryModeChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.DATABASE_QUERY_MODE_CHANGED, configurationPanel.getDatabaseQueryMode()
    );
  }

  public void queryFileChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.QUERY_FILE_CHANGED, configurationPanel.getQueryFile()
    );
  }

  public void eValueChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.E_VALUE_CHANGED, configurationPanel.getEvalue()
    );
  }

  public void maxTargetSeqsChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.MAX_TARGET_SEQS_CHANGED, configurationPanel.getMaxTargetSeqs()
    );
  }

  public void blastAdditionalParametersChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.BLAST_ADDITONAL_PARAMETERS_CHANGED, configurationPanel.getBlastAditionalParameters()
    );
  }

  public void extractOnlyHitRegionsChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.EXTRACT_ONLY_HIT_REGIONS_CHANGED, configurationPanel.isExtractOnlyHitRegions()
    );
  }

  public void hitRegionsWindowSizeChanged() {
    fireTransformationsConfigurationModelEvent(
      BlastTransformationConfigurationChangeType.HIT_REGIONS_WINDOW_SIZE_CHANGED, configurationPanel.getHitRegionsWindowSize()
    );
  }
}
