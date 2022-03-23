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
package org.sing_group.seda.cga.execution;

import static java.lang.String.valueOf;

import java.io.File;
import java.util.Properties;

public class CgaPipelineParameters extends Properties {
  private static final long serialVersionUID = 1L;

  public static final String HOST_WORKING_DIR = "host_working_dir";
  public static final String MAX_DIST = "max_dist";
  public static final String INTRON_BP = "intron_bp";
  public static final String SELECTION_CRITERION = "selection_criterion";
  public static final String SELECTION_CORRECTION = "selection_correction";
  public static final String MIN_FULL_NUCLEOTIDE_SIZE = "min_full_nucleotide_size";
  public static final String SKIP_PULL_DOCKER_IMAGES = "skip_pull_docker_images";

  private File workingDir;
  private CgaCompiPipelineConfiguration pipelineConfiguration;
  private String additionalCompiParameters;

  public CgaPipelineParameters(
    File workingDir, CgaCompiPipelineConfiguration pipelineConfiguration, String additionalCompiParameters
  ) {
    this.workingDir = workingDir;
    this.pipelineConfiguration = pipelineConfiguration;
    this.additionalCompiParameters = additionalCompiParameters;

    this.setProperties();
  }

  private void setProperties() {
    this.put(HOST_WORKING_DIR, this.workingDir.getAbsolutePath());
    this.put(MAX_DIST, valueOf(this.pipelineConfiguration.getMaxDist()));
    this.put(INTRON_BP, valueOf(this.pipelineConfiguration.getIntronBp()));
    this.put(SELECTION_CRITERION, valueOf(this.pipelineConfiguration.getSelectionCriterion().getValue()));
    this.put(SELECTION_CORRECTION, valueOf(this.pipelineConfiguration.getSelectionCorrection()));
    this.put(MIN_FULL_NUCLEOTIDE_SIZE, valueOf(this.pipelineConfiguration.getMinFullNucleotideSize()));
    if (this.pipelineConfiguration.isSkipPullDockerImages()) {
      this.put(SKIP_PULL_DOCKER_IMAGES, "true");
    }
  }

  public File getWorkingDir() {
    return workingDir;
  }

  public String getAdditionalCompiParameters() {
    return additionalCompiParameters;
  }
}
