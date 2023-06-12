/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
 * %%
 * Copyright (C) 2017 - 2023 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.prosplign.plugin.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;
import org.sing_group.seda.prosplign.transformation.dataset.ProSplignCompartPipelineSequencesGroupDatasetTransformation;
import org.sing_group.seda.util.SedaProperties;

public class ProSplignCompartPipelineSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "ProSplign/ProCompart Pipeline";
  public static final String SHORT_NAME = "prosplign-procompart";
  public static final String DESCRIPTION = "Obtain CDS annotations with ProSplign/ProCompart, using selected files and a file with reference protein sequences.";
  public static final String GROUP = Group.GROUP_GENE_ANNOTATION.getName();
  
  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION_PROSPLIGN_PROCOMPART =
    SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION + ".prosplignprocompart";
  
  public static final String PARAM_DOCKER_MODE_HELP =
    "The ProSplign/ProCompart docker image. By default, the official SEDA image is used. "
      + "If you provide a custom image, it should have the prosplign and procompart-wrapper "
      + "commands available in the path.";
  public static final String PARAM_DOCKER_MODE_HELP_GUI = 
    toHtml(PARAM_DOCKER_MODE_HELP, asList("prosplign", "procompart-wrapper"), emptyList(), true);

  public static final String PARAM_LOCAL_MODE_HELP =
    "The directory that contains the ProSplign/ProCompart binaries. " +
      "Leave it empty if they are in the path. Check the SEDA manual to see how to obtain them.";
  public static final String PARAM_LOCAL_MODE_HELP_GUI = toHtml(PARAM_LOCAL_MODE_HELP);

  public static final String PARAM_EXTERNAL_QUERY_FILE_NAME = "query-file";
  public static final String PARAM_EXTERNAL_QUERY_FILE_SHORT_NAME = "qf";
  public static final String PARAM_EXTERNAL_QUERY_FILE_DESCRIPTION = "External file query";
  public static final String PARAM_EXTERNAL_QUERY_FILE_HELP = "The query file (proteins).";
  public static final String PARAM_EXTERNAL_QUERY_FILE_HELP_GUI = PARAM_EXTERNAL_QUERY_FILE_HELP;

  public static final String PARAM_MAX_TARGET_SEQS_NAME = "max-target-seqs";
  public static final String PARAM_MAX_TARGET_SEQS_SHORT_NAME = "mts";
  public static final String PARAM_MAX_TARGET_SEQS_DESCRIPTION = "Max. target seqs.";
  public static final String PARAM_MAX_TARGET_SEQS_HELP = "Value of the max_target_seqs BLAST parameter.";
  public static final String PARAM_MAX_TARGET_SEQS_HELP_GUI = PARAM_MAX_TARGET_SEQS_HELP;
  
  public static final int DEFAULT_MAX_TARGET_SEQS =
    ProSplignCompartPipelineSequencesGroupDatasetTransformation.DEFAULT_MAX_TARGET_SEQS;
}
