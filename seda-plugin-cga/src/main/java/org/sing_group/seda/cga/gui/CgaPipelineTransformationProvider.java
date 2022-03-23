/*
 * #%L
 * SEquence DAtaset builder CGA Omega plugin
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.cga.gui;

import static org.sing_group.seda.cga.gui.CgaPipelineTransformationConfigurationChangeType.CGA_EXECUTOR_CHANGED;
import static org.sing_group.seda.cga.gui.CgaPipelineTransformationConfigurationChangeType.CGA_PIPELINE_CONFIGURATION_CHANGED;
import static org.sing_group.seda.cga.gui.CgaPipelineTransformationConfigurationChangeType.CGA_RESULTS_CHANGED;
import static org.sing_group.seda.cga.gui.CgaPipelineTransformationConfigurationChangeType.REFERENCE_FASTA_CHANGED;
import static org.sing_group.seda.cga.gui.CgaPipelineTransformationConfigurationChangeType.CGA_COMPI_TASKS;

import java.io.File;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.cga.execution.CgaBinariesExecutor;
import org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration;
import org.sing_group.seda.cga.transformation.dataset.CgaPipelineSequencesGroupDatasetTransformation;
import org.sing_group.seda.cga.transformation.dataset.CgaResults;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
public class CgaPipelineTransformationProvider extends AbstractTransformationProvider {
  @XmlAnyElement(lax = true)
  private CgaBinariesExecutor cgaBinariesExecutor;
  @XmlElement
  private File referenceFasta;
  @XmlElement
  private CgaCompiPipelineConfiguration cgaCompiPipelineConfiguration;
  @XmlElement
  private CgaResults cgaResults;
  @XmlElement
  private int compiTasks;

  @Override
  public boolean isValidTransformation() {
    return this.referenceFasta != null && this.cgaCompiPipelineConfiguration != null && this.cgaResults != null
      && this.isValidCgaBinariesExecutor();
  }

  private boolean isValidCgaBinariesExecutor() {
    if (this.cgaBinariesExecutor == null) {
      return false;
    }

    try {
      this.cgaBinariesExecutor.checkBinary();

      return true;
    } catch (BinaryCheckException e) {
      return false;
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return new CgaPipelineSequencesGroupDatasetTransformation(
      factory, this.cgaCompiPipelineConfiguration, this.referenceFasta, this.cgaResults,
      this.getAdditionalCompiParameters(), this.cgaBinariesExecutor
    );
  }

  private String getAdditionalCompiParameters() {
    return String.join(" ", "-n", Integer.toString(compiTasks));
  }

  public void setCgaBinariesExecutor(Optional<CgaBinariesExecutor> cgaBinariesExecutor) {
    this.cgaBinariesExecutor = cgaBinariesExecutor.orElse(null);
    fireTransformationsConfigurationModelEvent(CGA_EXECUTOR_CHANGED, this.cgaBinariesExecutor);
  }

  public CgaBinariesExecutor getcgaBinariesExecutor() {
    return this.cgaBinariesExecutor;
  }

  public void setReferenceFasta(File referenceFasta) {
    if (this.referenceFasta == null || !this.referenceFasta.equals(referenceFasta)) {
      this.referenceFasta = referenceFasta;
      fireTransformationsConfigurationModelEvent(REFERENCE_FASTA_CHANGED, this.referenceFasta);
    }
  }

  public void clearQueryFile() {
    if (this.referenceFasta != null) {
      this.referenceFasta = null;
      fireTransformationsConfigurationModelEvent(REFERENCE_FASTA_CHANGED, this.referenceFasta);
    }
  }

  public File getreferenceFasta() {
    return this.referenceFasta;
  }

  public void setCgaResults(CgaResults cgaResults) {
    if (this.cgaResults == null || !this.cgaResults.equals(cgaResults)) {
      this.cgaResults = cgaResults;
      fireTransformationsConfigurationModelEvent(CGA_RESULTS_CHANGED, this.cgaResults);
    }
  }

  public CgaResults getCgaResults() {
    return this.cgaResults;
  }

  public void setCgaCompiPipelineConfiguration(CgaCompiPipelineConfiguration cgaCompiPipelineConfiguration) {
    if (
      this.cgaCompiPipelineConfiguration == null
        || !this.cgaCompiPipelineConfiguration.equals(cgaCompiPipelineConfiguration)
    ) {
      this.cgaCompiPipelineConfiguration = cgaCompiPipelineConfiguration;
      fireTransformationsConfigurationModelEvent(
        CGA_PIPELINE_CONFIGURATION_CHANGED, this.cgaCompiPipelineConfiguration
      );
    }
  }

  public CgaCompiPipelineConfiguration getCgaCompiPipelineConfiguration() {
    return this.cgaCompiPipelineConfiguration;
  }

  public void setCompiTasks(int compiTasks) {
    if (this.compiTasks != compiTasks) {
      this.compiTasks = compiTasks;
      fireTransformationsConfigurationModelEvent(CGA_COMPI_TASKS, this.compiTasks);
    }
  }

  public int getCompiTasks() {
    return compiTasks;
  }
}
