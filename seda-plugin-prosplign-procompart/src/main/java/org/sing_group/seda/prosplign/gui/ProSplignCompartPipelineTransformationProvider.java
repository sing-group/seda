/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
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
package org.sing_group.seda.prosplign.gui;

import static org.sing_group.seda.prosplign.gui.ProSplignCompartPipelineTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED;
import static org.sing_group.seda.prosplign.gui.ProSplignCompartPipelineTransformationConfigurationChangeType.MAX_TARGET_SEQS_CHANGED;
import static org.sing_group.seda.prosplign.gui.ProSplignCompartPipelineTransformationConfigurationChangeType.PRO_SPLIGN_COMPART_EXECUTOR_CHANGED;
import static org.sing_group.seda.prosplign.gui.ProSplignCompartPipelineTransformationConfigurationChangeType.QUERY_FILE_CHANGED;

import java.io.File;
import java.util.Optional;

import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.execution.BlastBinariesExecutorWrapper;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.prosplign.execution.ProSplignCompartBinariesExecutor;
import org.sing_group.seda.prosplign.transformation.dataset.ProSplignCompartPipelineSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class ProSplignCompartPipelineTransformationProvider extends AbstractTransformationProvider {
  private ProSplignCompartBinariesExecutor proSplignCompartBinariesExecutor;
  private BlastBinariesExecutorWrapper blastBinariesExecutor;
  private File proteinQueryFile;
  private int maxTargetSeqs;

  public ProSplignCompartPipelineTransformationProvider() {
    this.blastBinariesExecutor = new BlastBinariesExecutorWrapper();
  }

  @Override
  public boolean isValidTransformation() {
    return this.proteinQueryFile != null
      && isValidProSplignCompartBinariesExecutor()
      && isValidBlastBinariesExecutor();
  }

  private boolean isValidProSplignCompartBinariesExecutor() {
    if (this.proSplignCompartBinariesExecutor == null) {
      return false;
    }

    try {
      this.proSplignCompartBinariesExecutor.checkBinary();

      return true;
    } catch (BinaryCheckException e) {
      return false;
    }
  }

  private boolean isValidBlastBinariesExecutor() {
    if (this.blastBinariesExecutor.get() == null) {
      return false;
    }

    try {
      this.blastBinariesExecutor.get().checkBinary();

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
      this.proSplignCompartBinariesExecutor,
      this.blastBinariesExecutor.get(),
      this.proteinQueryFile,
      this.maxTargetSeqs
    );
  }

  public void setMaxTargetSeqs(int maxTargetSeqs) {
    if(this.maxTargetSeqs != maxTargetSeqs) {
      this.maxTargetSeqs = maxTargetSeqs;
      fireTransformationsConfigurationModelEvent(MAX_TARGET_SEQS_CHANGED, this.maxTargetSeqs);
    }
  }

  public void setProSplignCompartBinariresExecutor(
    Optional<ProSplignCompartBinariesExecutor> proSplignCompartBinariesExecutor
  ) {
    this.proSplignCompartBinariesExecutor = proSplignCompartBinariesExecutor.orElse(null);
    fireTransformationsConfigurationModelEvent(
      PRO_SPLIGN_COMPART_EXECUTOR_CHANGED, this.proSplignCompartBinariesExecutor
    );
  }

  public void setBlastBinariesExecutor(Optional<BlastBinariesExecutor> blastBinariesExecutor) {
    this.blastBinariesExecutor.set(blastBinariesExecutor.orElse(null));
    fireTransformationsConfigurationModelEvent(BLAST_EXECUTOR_CHANGED, this.blastBinariesExecutor.get());
  }

  public void clearProteinQueryFile() {
    this.proteinQueryFile = null;
    fireTransformationsConfigurationModelEvent(QUERY_FILE_CHANGED, this.proteinQueryFile);
  }

  public void setProteinQueryFile(File proteinQueryFile) {
    if (this.proteinQueryFile == null || !this.proteinQueryFile.equals(proteinQueryFile)) {
      this.proteinQueryFile = proteinQueryFile;
      fireTransformationsConfigurationModelEvent(QUERY_FILE_CHANGED, this.proteinQueryFile);
    }
  }
}