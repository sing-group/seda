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
package org.sing_group.seda.clustalomega.gui;

import static org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesChecker.checkClustalOmegaBinary;
import static org.sing_group.seda.clustalomega.gui.ClustalOmegaAlignmentTransformationConfigurationChangeType.ADDITIONAL_PARAMETERS_CHANGED;
import static org.sing_group.seda.clustalomega.gui.ClustalOmegaAlignmentTransformationConfigurationChangeType.CLUSTAL_OMEGA_PATH_CHANGED;
import static org.sing_group.seda.clustalomega.gui.ClustalOmegaAlignmentTransformationConfigurationChangeType.NUM_THREADS_CHANGED;

import java.io.File;

import org.sing_group.seda.clustalomega.execution.BinaryCheckException;
import org.sing_group.seda.clustalomega.transformation.sequencesgroup.ClustalOmegaAlignmentSequencesGroupTransformation;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class ClustalOmegaAlignmentTransformationProvider extends AbstractTransformationProvider {
  private ClustalOmegaAlignmentTransformationConfigurationPanel configurationPanel;

  public ClustalOmegaAlignmentTransformationProvider(
    ClustalOmegaAlignmentTransformationConfigurationPanel configurationPanel
  ) {
    this.configurationPanel = configurationPanel;
  }

  @Override
  public boolean isValidTransformation() {
    return isValidClustalOmegaPath();
  }

  private boolean isValidClustalOmegaPath() {
    try {
      checkClustalOmegaBinary(getClustalOmegaBinary());
      return true;
    } catch (BinaryCheckException e) {
      return false;
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(
    DatatypeFactory factory
  ) {
    return new ComposedSequencesGroupDatasetTransformation(getClustalOmegaTransformation(factory));
  }

  private ClustalOmegaAlignmentSequencesGroupTransformation getClustalOmegaTransformation(DatatypeFactory factory) {
    return new ClustalOmegaAlignmentSequencesGroupTransformation(
      factory,
      getClustalOmegaBinary(),
      this.configurationPanel.getNumThreads(),
      this.configurationPanel.getAdditionalParameters()
    );
  }

  private File getClustalOmegaBinary() {
    return configurationPanel.getClustalOmegaPath();
  }

  public void numThreadsChanged() {
    fireTransformationsConfigurationModelEvent(NUM_THREADS_CHANGED, configurationPanel.getNumThreads());
  }

  public void clustalOmegaPathChanged() {
    fireTransformationsConfigurationModelEvent(CLUSTAL_OMEGA_PATH_CHANGED, configurationPanel.getClustalOmegaPath());
  }

  public void additionalParametersChanged() {
    fireTransformationsConfigurationModelEvent(
      ADDITIONAL_PARAMETERS_CHANGED, configurationPanel.getAdditionalParameters()
    );
  }
}