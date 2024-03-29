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
package org.sing_group.seda.prosplign.transformation.provider;

import static org.sing_group.seda.prosplign.transformation.provider.ProSplignCompartPipelineTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED;
import static org.sing_group.seda.prosplign.transformation.provider.ProSplignCompartPipelineTransformationConfigurationChangeType.MAX_TARGET_SEQS_CHANGED;
import static org.sing_group.seda.prosplign.transformation.provider.ProSplignCompartPipelineTransformationConfigurationChangeType.PRO_SPLIGN_COMPART_EXECUTOR_CHANGED;
import static org.sing_group.seda.prosplign.transformation.provider.ProSplignCompartPipelineTransformationConfigurationChangeType.QUERY_FILE_CHANGED;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.execution.BlastBinariesExecutorWrapper;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.prosplign.execution.ProSplignCompartBinariesExecutor;
import org.sing_group.seda.prosplign.transformation.dataset.ProSplignCompartPipelineSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
public class ProSplignCompartPipelineTransformationProvider extends AbstractTransformationProvider {
  @XmlAnyElement(lax = true)
  private ProSplignCompartBinariesExecutor proSplignCompartBinariesExecutor;
  @XmlElement
  private BlastBinariesExecutorWrapper blastBinariesExecutor;
  @XmlElement
  private File proteinQueryFile;
  @XmlElement
  private int maxTargetSeqs;

  public ProSplignCompartPipelineTransformationProvider() {
    this.blastBinariesExecutor = new BlastBinariesExecutorWrapper();
  }


  @Override
  public Validation validate() {
    try {
      List<String> validationErrors = new LinkedList<String>();

      if (this.proteinQueryFile == null) {
        validationErrors.add("The protein query file can't be null");
      }

      if (!isValidProSplignCompartBinariesExecutor()) {
        validationErrors.add("The ProSplign/ProCompart binaries executor is not valid");
      }

      if (!isValidBlastBinariesExecutor()) {
        validationErrors.add("The BLAST binaries executor is not valid");
      }

      if (validationErrors.isEmpty()) {
        getTransformation(DatatypeFactory.getDefaultDatatypeFactory());

        return new DefaultValidation();
      } else {
        return new DefaultValidation(validationErrors);
      }
    } catch (RuntimeException ex) {
      return new DefaultValidation(ex.toString());
    }
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

  public void setProSplignCompartBinariresExecutor(
    Optional<ProSplignCompartBinariesExecutor> proSplignCompartBinariesExecutor
  ) {
    this.proSplignCompartBinariesExecutor = proSplignCompartBinariesExecutor.orElse(null);
    fireTransformationsConfigurationModelEvent(
      PRO_SPLIGN_COMPART_EXECUTOR_CHANGED, this.proSplignCompartBinariesExecutor
    );
  }

  public ProSplignCompartBinariesExecutor getProSplignCompartBinariesExecutor() {
    return proSplignCompartBinariesExecutor;
  }

  public void setBlastBinariesExecutor(Optional<BlastBinariesExecutor> blastBinariesExecutor) {
    this.blastBinariesExecutor.set(blastBinariesExecutor.orElse(null));
    fireTransformationsConfigurationModelEvent(BLAST_EXECUTOR_CHANGED, this.blastBinariesExecutor.get());
  }

  public BlastBinariesExecutor getBlastBinariesExecutor() {
    return blastBinariesExecutor.get();
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

  public File getProteinQueryFile() {
    return proteinQueryFile;
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
}