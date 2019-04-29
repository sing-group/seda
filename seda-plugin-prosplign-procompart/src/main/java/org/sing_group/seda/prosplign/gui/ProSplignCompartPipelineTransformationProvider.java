/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
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
package org.sing_group.seda.prosplign.gui;

import static org.sing_group.seda.prosplign.gui.ProSplignCompartPipelineTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED;
import static org.sing_group.seda.prosplign.gui.ProSplignCompartPipelineTransformationConfigurationChangeType.MAX_TARGET_SEQS_CHANGED;
import static org.sing_group.seda.prosplign.gui.ProSplignCompartPipelineTransformationConfigurationChangeType.PRO_SPLIGN_COMPART_EXECUTOR_CHANGED;
import static org.sing_group.seda.prosplign.gui.ProSplignCompartPipelineTransformationConfigurationChangeType.QUERY_FILE_CHANGED;

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.prosplign.transformation.dataset.ProSplignCompartPipelineSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class ProSplignCompartPipelineTransformationProvider extends AbstractTransformationProvider {
  private ProSplignCompartPipelineTransformationConfigurationPanel configurationPanel;

  public ProSplignCompartPipelineTransformationProvider(
    ProSplignCompartPipelineTransformationConfigurationPanel configurationPanel
  ) {
    this.configurationPanel = configurationPanel;
  }

  @Override
  public boolean isValidTransformation() {
    return configurationPanel.getQueryFile() != null
      && isValidProSplignCompartBinariesExecutor()
      && isValidBlastBinariesExecutor();
  }

  private boolean isValidProSplignCompartBinariesExecutor() {
    if (!configurationPanel.getProSplignCompartBinariesExecutor().isPresent()) {
      return false;
    }

    try {
      configurationPanel.getProSplignCompartBinariesExecutor().get().checkBinary();

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

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return getProSplignCompartPipelineTransformation(factory);
  }

  private ProSplignCompartPipelineSequencesGroupDatasetTransformation getProSplignCompartPipelineTransformation(DatatypeFactory factory) {
    return new ProSplignCompartPipelineSequencesGroupDatasetTransformation(
      factory,
      this.configurationPanel.getProSplignCompartBinariesExecutor().get(),
      this.configurationPanel.getBlastBinariesExecutor().get(),
      this.configurationPanel.getQueryFile(),
      this.configurationPanel.getMaxTargetSeqs()
    );
  }

  public void maxTargetSeqsChanged() {
    fireTransformationsConfigurationModelEvent(MAX_TARGET_SEQS_CHANGED, configurationPanel.getMaxTargetSeqs());
  }

  public void proSplignCompartExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      PRO_SPLIGN_COMPART_EXECUTOR_CHANGED, configurationPanel.getProSplignCompartBinariesExecutor()
    );
  }
  

  public void blastExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      BLAST_EXECUTOR_CHANGED, configurationPanel.getBlastBinariesExecutor()
    );
  }

  public void queryFileChanged() {
    fireTransformationsConfigurationModelEvent(
      QUERY_FILE_CHANGED, configurationPanel.getQueryFile()
    );
  }
}