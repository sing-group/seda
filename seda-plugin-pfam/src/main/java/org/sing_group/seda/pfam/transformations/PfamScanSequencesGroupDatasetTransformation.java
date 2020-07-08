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
package org.sing_group.seda.pfam.transformations;

import java.io.File;
import java.io.IOException;

import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;

public class PfamScanSequencesGroupDatasetTransformation extends ComposedSequencesGroupDatasetTransformation {

  private static final String CONCURRENT_PFAM_SCAN_MESSAGE =
    "It seems that a PfamScan operation is already running. "
      + "SEDA only allows to execute one PfamScan operation at a time in order respect EMBL-EBI policies regarding the "
      + "usage of resources and avoid problems.";

  private File lockFile;

  public PfamScanSequencesGroupDatasetTransformation(PfamScanSequencesGroupTransformation pfamScanTransformation) {
    super(pfamScanTransformation);
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) throws TransformationException {
    this.createLockFile();
    try {
      return super.transform(dataset);
    } catch (Throwable t) {
      throw t;
    } finally {
      this.lockFile.delete();
    }
  }

  private void createLockFile() {
    this.lockFile = new File(System.getProperty("user.home"), ".seda-pfam-scan.lock");
    if (this.lockFile.exists()) {
      throw new TransformationException(CONCURRENT_PFAM_SCAN_MESSAGE);
    } else {
      try {
        this.lockFile.createNewFile();
      } catch (IOException e) {
        throw new TransformationException("Could not create lock file", e);
      }
    }
  }
}
