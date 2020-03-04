/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.gui;

import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.BEDTOOLS_EXECUTOR_CHANGED;
import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.BLAST_EXECUTOR_CHANGED;
import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.CONCATENATE_EXONS_CHANGED;
import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.QUERY_FILE_CHANGED;
import static org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationChangeType.SPLIGN_COMPART_EXECUTOR_CHANGED;

import java.io.File;
import java.util.Optional;

import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.splign.execution.SplignCompartBinariesExecutor;
import org.sing_group.seda.splign.transformation.dataset.SplignCompartPipelineSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class SplignCompartPipelineTransformationProvider extends AbstractTransformationProvider {
  private SplignCompartBinariesExecutor splignCompartBinariesExecutor;
  private BlastBinariesExecutor blastBinariesExecutor;
  private BedToolsBinariesExecutor bedToolsBinariesExecutor;
  private File queryFile;
  private boolean concatenateExons;

  @Override
  public boolean isValidTransformation() {
    return this.queryFile != null
      && isValidProSplignCompartBinariesExecutor()
      && isValidBlastBinariesExecutor()
      && isValidBedToolsBinariesExecutor();
  }

  private boolean isValidProSplignCompartBinariesExecutor() {
    if (this.splignCompartBinariesExecutor == null) {
      return false;
    }

    try {
      this.splignCompartBinariesExecutor.checkBinary();

      return true;
    } catch (BinaryCheckException e) {
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
  
  private boolean isValidBedToolsBinariesExecutor() {
    if (this.bedToolsBinariesExecutor == null) {
      return false;
    }

    try {
      this.bedToolsBinariesExecutor.checkBinary();

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
      this.splignCompartBinariesExecutor,
      this.bedToolsBinariesExecutor,
      this.blastBinariesExecutor,
      this.queryFile,
      this.concatenateExons
    );
  }

  public void setConcatenateExons(boolean concatenateExons) {
    if (this.concatenateExons != concatenateExons) {
      this.concatenateExons = concatenateExons;
      fireTransformationsConfigurationModelEvent(CONCATENATE_EXONS_CHANGED, this.concatenateExons);
    }
  }

  public void setSplignCompartBinariresExecutor(Optional<SplignCompartBinariesExecutor> splignCompartBinariesExecutor) {
    if (splignCompartBinariesExecutor.isPresent()) {
      this.splignCompartBinariesExecutor = splignCompartBinariesExecutor.get();
    } else {
      this.splignCompartBinariesExecutor = null;
    }
    fireTransformationsConfigurationModelEvent(SPLIGN_COMPART_EXECUTOR_CHANGED, this.splignCompartBinariesExecutor);
  }

  public void setBlastBinariesExecutor(Optional<BlastBinariesExecutor> blastBinariesExecutor) {
    if (blastBinariesExecutor.isPresent()) {
      this.blastBinariesExecutor = blastBinariesExecutor.get();
    } else {
      this.blastBinariesExecutor = null;
    }
    fireTransformationsConfigurationModelEvent(BLAST_EXECUTOR_CHANGED, this.blastBinariesExecutor);
  }

  public void setBedToolsBinariesExecutor(Optional<BedToolsBinariesExecutor> bedToolsBinariesExecutor) {
    if (bedToolsBinariesExecutor.isPresent()) {
      this.bedToolsBinariesExecutor = bedToolsBinariesExecutor.get();
    } else {
      this.bedToolsBinariesExecutor = null;
    }
    fireTransformationsConfigurationModelEvent(BEDTOOLS_EXECUTOR_CHANGED, this.bedToolsBinariesExecutor);
  }

  public void clearQueryFile() {
    if (this.queryFile != null) {
      this.queryFile = null;
      fireTransformationsConfigurationModelEvent(QUERY_FILE_CHANGED, this.queryFile);
    }
  }

  public void setQueryFile(File queryFile) {
    if (this.queryFile == null || !this.queryFile.equals(queryFile)) {
      this.queryFile = queryFile;
      fireTransformationsConfigurationModelEvent(QUERY_FILE_CHANGED, this.queryFile);
    }
  }
}