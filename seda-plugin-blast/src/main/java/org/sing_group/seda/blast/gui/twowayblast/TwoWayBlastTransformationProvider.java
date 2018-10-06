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

import org.sing_group.seda.blast.transformation.dataset.TwoWayBlastTransformation;
import org.sing_group.seda.blast.transformation.dataset.TwoWayBlastTransformationBuilder;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class TwoWayBlastTransformationProvider extends AbstractTransformationProvider {

  private TwoWayBlastTransformationConfigurationPanel configurationPanel;

  public TwoWayBlastTransformationProvider(TwoWayBlastTransformationConfigurationPanel configurationPanel) {
    this.configurationPanel = configurationPanel;
  }

  @Override
  public boolean isValidTransformation() {
    try {
      if (this.configurationPanel.isStoreDatabases() && this.configurationPanel.getDatabasesDirectory() == null) {
        return false;
      }

      getBlastTransformation(DatatypeFactory.getDefaultDatatypeFactory());

      return true;
    } catch (RuntimeException ex) {
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
    TwoWayBlastTransformationBuilder builder = new TwoWayBlastTransformationBuilder(
      configurationPanel.getBlastType(),
      configurationPanel.getQueryFile().get(),
      configurationPanel.getQueryMode()
    )
      .withDatatypeFactory(factory)
      .withEvalue(configurationPanel.getEvalue())
      .withBlastAditionalParameters(configurationPanel.getBlastAditionalParameters())
      .withBlastBinariesExecutor(configurationPanel.getBlastBinariesExecutor().get());

    if (configurationPanel.isStoreDatabases()) {
      builder.withDatabasesDirectory(configurationPanel.getDatabasesDirectory());
    }

    return builder.build();
  }

  public void blastPathChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.BLAST_PATH_CHANGED, configurationPanel.getBlastPath()
    );
  }

  public void blastExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED, configurationPanel.getBlastBinariesExecutor()
    );
  }

  public void storeDatabasesChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.STORE_DATABASES_CHANGED, configurationPanel.isStoreDatabases()
    );
  }

  public void databasesDirectoryChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.DATABASES_DIRECTORY_CHANGED, configurationPanel.getDatabasesDirectory()
    );
  }

  public void blastTypeChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.BLAST_TYPE_CHANGED, configurationPanel.getBlastType()
    );
  }

  public void queryModeChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.QUERY_MODE_CHANGED, configurationPanel.getQueryMode()
    );
  }

  public void queryFileChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.QUERY_FILE_CHANGED, configurationPanel.getQueryFile()
    );
  }

  public void eValueChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.E_VALUE_CHANGED, configurationPanel.getEvalue()
    );
  }

  public void blastAdditionalParametersChanged() {
    fireTransformationsConfigurationModelEvent(
      TwoWayBlastTransformationConfigurationChangeType.BLAST_ADDITONAL_PARAMETERS_CHANGED, configurationPanel.getBlastAditionalParameters()
    );
  }
}
