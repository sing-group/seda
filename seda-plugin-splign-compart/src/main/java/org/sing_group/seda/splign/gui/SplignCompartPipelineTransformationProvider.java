/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.gui;

import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.BEDTOOLS_EXECUTOR_CHANGED;
import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED;
import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.CONCATENATE_EXONS_CHANGED;
import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.QUERY_FILE_CHANGED;
import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.SPLIGN_COMPART_EXECUTOR_CHANGED;

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.splign.transformation.dataset.SplignCompartPipelineSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class SplignCompartPipelineTransformationProvider extends AbstractTransformationProvider {
  private SplignCompartPipelineTransformationConfigurationPanel configurationPanel;

  public SplignCompartPipelineTransformationProvider(
    SplignCompartPipelineTransformationConfigurationPanel configurationPanel
  ) {
    this.configurationPanel = configurationPanel;
  }

  @Override
  public boolean isValidTransformation() {
    return configurationPanel.getQueryFile() != null
      && isValidProSplignCompartBinariesExecutor()
      && isValidBlastBinariesExecutor()
      && isValidBedToolsBinariesExecutor();
  }

  private boolean isValidProSplignCompartBinariesExecutor() {
    if (!configurationPanel.getSplignCompartBinariesExecutor().isPresent()) {
      return false;
    }

    try {
      configurationPanel.getSplignCompartBinariesExecutor().get().checkBinary();

      return true;
    } catch (BinaryCheckException e) {
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
  
  private boolean isValidBedToolsBinariesExecutor() {
    if (!this.configurationPanel.getBedToolsBinariesExecutor().isPresent()) {
      return false;
    }

    try {
      this.configurationPanel.getBedToolsBinariesExecutor().get().checkBinary();

      return true;
    } catch (BinaryCheckException e) {
      return false;
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return getProSplignCompartPipelineTransformation(factory);
  }

  private SplignCompartPipelineSequencesGroupDatasetTransformation getProSplignCompartPipelineTransformation(DatatypeFactory factory) {
    return new SplignCompartPipelineSequencesGroupDatasetTransformation(
      factory,
      this.configurationPanel.getSplignCompartBinariesExecutor().get(),
      this.configurationPanel.getBedToolsBinariesExecutor().get(),
      this.configurationPanel.getBlastBinariesExecutor().get(),
      this.configurationPanel.getQueryFile(),
      this.configurationPanel.isConcatenateExons()
    );
  }

  public void concatenateExonsChanged() {
    fireTransformationsConfigurationModelEvent(CONCATENATE_EXONS_CHANGED, configurationPanel.isConcatenateExons());
  }

  public void splignCompartExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      SPLIGN_COMPART_EXECUTOR_CHANGED, configurationPanel.getSplignCompartBinariesExecutor()
    );
  }

  public void blastExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      BLAST_EXECUTOR_CHANGED, configurationPanel.getBlastBinariesExecutor()
    );
  }
  
  public void bedToolsExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      BEDTOOLS_EXECUTOR_CHANGED, configurationPanel.getBedToolsBinariesExecutor()
    );
  }

  public void queryFileChanged() {
    fireTransformationsConfigurationModelEvent(
      QUERY_FILE_CHANGED, configurationPanel.getQueryFile()
    );
  }
}