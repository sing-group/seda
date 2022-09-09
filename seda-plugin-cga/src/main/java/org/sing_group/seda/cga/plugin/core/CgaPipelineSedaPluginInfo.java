/*
 * #%L
 * SEquence DAtaset builder CGA plugin
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
package org.sing_group.seda.cga.plugin.core;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static org.sing_group.seda.util.SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION;

import org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration;
import org.sing_group.seda.cga.transformation.dataset.CgaResults;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class CgaPipelineSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Conserved Genome Annotation (CGA) Pipeline";
  public static final String SHORT_NAME = "cga";
  public static final String DESCRIPTION = "Obtain CDS annotations with CGA, using selected files and a FASTA file with the reference sequence.";
  public static final String GROUP = Group.GROUP_GENE_ANNOTATION.getName();

  /**
   * The constant is kept for sake of consistency with other SEDA plugins, but CGA
   * can't be executed with a "local binary". In fact, the corresponding
   * getLocalOptionsList returns an empty list.
   */
  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION_CGA = PROPERTY_ENABLE_LOCAL_EXECUTION + ".cga.not.used";

  public static final String PARAM_REFERENCE_FASTA_NAME = "reference-fasta";
  public static final String PARAM_REFERENCE_FASTA_SHORT_NAME = "rf";
  public static final String PARAM_REFERENCE_FASTA_DESCRIPTION = "Reference FASTA file";
  public static final String PARAM_REFERENCE_FASTA_HELP = "FASTA file containing the reference sequence.";
  public static final String PARAM_REFERENCE_FASTA_HELP_GUI = PARAM_REFERENCE_FASTA_HELP;
  
  public static final String PARAM_CGA_RESULTS_NAME = "results";
  public static final String PARAM_CGA_RESULTS_SHORT_NAME = "r";
  public static final String PARAM_CGA_RESULTS_DESCRIPTION = "Results";
  public static final String PARAM_CGA_RESULTS_HELP = longEnumStringForCli(
    "The CGA results to collect.",
    cliMap(
      CgaResults.values(),
      stream(CgaResults.values()).map(CgaResults::getDescription).toArray(String[]::new)
    )
  );
  public static final String PARAM_CGA_RESULTS_HELP_GUI =  "The CGA results to collect.";
  
  public static final String PARAM_COMPI_TASKS_NAME = "num-tasks";
  public static final String PARAM_COMPI_TASKS_SHORT_NAME = "nt";
  public static final String PARAM_COMPI_TASKS_DESCRIPTION = "Parallell tasks";
  public static final String PARAM_COMPI_TASKS_HELP = "The maximum number of parallell tasks that the Compi pipeline may execute.";
  public static final String PARAM_COMPI_TASKS_HELP_GUI = PARAM_COMPI_TASKS_HELP;
  
  public static final String PARAM_MAX_DIST_NAME = "max-dist";
  public static final String PARAM_MAX_DIST_SHORT_NAME = "md";
  public static final String PARAM_MAX_DIST_DESCRIPTION = "Max. dist.";
  public static final String PARAM_MAX_DIST_HELP = "Maximum distance between exons (in this case sequences identified by getorf) from the same gene. "
    + "It only applies to large genome sequences where there is some chance that two genes with similar features are present.";
  public static final String PARAM_MAX_DIST_HELP_GUI = toHtml(PARAM_MAX_DIST_HELP);

  public static final String PARAM_INTRON_BP_NAME = "intron-bp";
  public static final String PARAM_INTRON_BP_SHORT_NAME = "ibp";
  public static final String PARAM_INTRON_BP_DESCRIPTION = "Intron BP";
  public static final String PARAM_INTRON_BP_HELP = "Distance around the junction point between two sequences where to look for splicing signals.";
  public static final String PARAM_INTRON_BP_HELP_GUI = PARAM_INTRON_BP_HELP;
  
  public static final String PARAM_MIN_FULL_NUCLEOTIDE_SIZE_NAME = "min-full-nucleotide-size";
  public static final String PARAM_MIN_FULL_NUCLEOTIDE_SIZE_SHORT_NAME = "mfs";
  public static final String PARAM_MIN_FULL_NUCLEOTIDE_SIZE_DESCRIPTION = "Min. CDS size";
  public static final String PARAM_MIN_FULL_NUCLEOTIDE_SIZE_HELP = "Minimum size for CDS to be reported.";
  public static final String PARAM_MIN_FULL_NUCLEOTIDE_SIZE_HELP_GUI = PARAM_MIN_FULL_NUCLEOTIDE_SIZE_HELP;
  
  public static final String PARAM_SELECTION_CRITERION_NAME = "selection-criterion";
  public static final String PARAM_SELECTION_CRITERION_SHORT_NAME = "scr";
  public static final String PARAM_SELECTION_CRITERION_DESCRIPTION = "Selection criterion";
  public static final String PARAM_MIN_SELECTION_CRITERION_HELP = longEnumStringForCli(
    "The selection model to be used.",
    cliMap(
      CgaCompiPipelineConfiguration.SelectionCriterion.values(),
      stream(CgaCompiPipelineConfiguration.SelectionCriterion.values())
        .map(CgaCompiPipelineConfiguration.SelectionCriterion::getDescription)
        .toArray(String[]::new)
    )
  );
  
  public static final String PARAM_MIN_SELECTION_CRITERION_HELP_GUI = toHtml(
    longEnumStringForGui(
      "The selection model to be used.",
      guiMap(
        CgaCompiPipelineConfiguration.SelectionCriterion.values(),
        stream(CgaCompiPipelineConfiguration.SelectionCriterion.values())
          .map(CgaCompiPipelineConfiguration.SelectionCriterion::getDescription)
          .toArray(String[]::new)
      )
    ), emptyList(), emptyList(), false
  );

  public static final String PARAM_SELECTION_CORRECTION_NAME = "selection-correction";
  public static final String PARAM_SELECTION_CORRECTION_SHORT_NAME = "sco";
  public static final String PARAM_SELECTION_CORRECTION_DESCRIPTION = "Selection correction";
  public static final String PARAM_SELECTION_CORRECTION_HELP = "A bonus percentage times 10 when using the mixed selection model (3). "
    + "For instance, 20 means 2% bonus. Something with 18% similarity acts as having 20% similarity.";
  public static final String PARAM_SELECTION_CORRECTION_HELP_GUI = PARAM_SELECTION_CORRECTION_HELP;
  
  public static final String PARAM_SKIP_PULL_DOCKER_IMAGES_NAME = "skip-docker-pull";
  public static final String PARAM_SKIP_PULL_DOCKER_IMAGES_SHORT_NAME = "sdp";
  public static final String PARAM_SKIP_PULL_DOCKER_IMAGES_DESCRIPTION = "Skip pull Docker images";
  public static final String PARAM_SKIP_PULL_DOCKER_IMAGES_HELP = "Use this flag to skip the pull-docker-images task.";
  public static final String PARAM_SKIP_PULL_DOCKER_IMAGES_HELP_GUI = toHtml(
    PARAM_SKIP_PULL_DOCKER_IMAGES_HELP, emptyList(), asList("pull-docker-images"), false);
  
  public static final String PARAM_DOCKER_MODE_HELP = "The CGA Docker image. By default, the official pegi3s/cga image is used. "
    + "It is not recommended changing it.";
  public static final String PARAM_DOCKER_MODE_HELP_GUI = toHtml(PARAM_DOCKER_MODE_HELP);
}
