/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.plugin.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;
import org.sing_group.seda.util.SedaProperties;

public class SplignCompartPipelineSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Splign/Compart Pipeline";
  public static final String SHORT_NAME = "splign-compart";
  public static final String DESCRIPTION = "Obtain CDS annotations with Splign/Compart, using selected files and a file with reference CDS sequences.";
  public static final String GROUP = Group.GROUP_GENE_ANNOTATION.getName();
  
  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION_SPLIGN_COMPART =
    SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION + ".spligncompart";
  
  public static final String PARAM_DOCKER_MODE_HELP =
    "The Splign/ProCompart docker image. By default, the official SEDA image is used. "
      + "If you provide a custom image, it should have the splign and compart "
      + "commands available in the path.";
  public static final String PARAM_DOCKER_MODE_HELP_GUI =
    toHtml(PARAM_DOCKER_MODE_HELP, asList("splign", "compart"), emptyList(), true);

  public static final String PARAM_LOCAL_MODE_HELP =
    "The directory that contains the Splign/Compart binaries. " +
      "Leave it empty if they are in the path. Check the SEDA manual to see how to obtain them.";
  public static final String PARAM_LOCAL_MODE_HELP_GUI =
    toHtml(PARAM_LOCAL_MODE_HELP, asList("directory", "blast binaries"), asList(), false);
  
  public static final String PARAM_EXTERNAL_QUERY_FILE_NAME = "query-file";
  public static final String PARAM_EXTERNAL_QUERY_FILE_SHORT_NAME = "qf";
  public static final String PARAM_EXTERNAL_QUERY_FILE_DESCRIPTION = "External file query";
  public static final String PARAM_EXTERNAL_QUERY_FILE_HELP = "The CDS query file (nucleotides).";
  public static final String PARAM_EXTERNAL_QUERY_FILE_HELP_GUI = PARAM_EXTERNAL_QUERY_FILE_HELP;
  
  public static final String PARAM_CONCATENATE_EXONS_NAME = "concat-exons";
  public static final String PARAM_CONCATENATE_EXONS_SHORT_NAME = "ce";
  public static final String PARAM_CONCATENATE_EXONS_DESCRIPTION = "Concatenate exons?";
  public static final String PARAM_CONCATENATE_EXONS_HELP =
    "If the concatenate exons option is checked, then adjacent exons will be concatenated. "
      + "Therefore, if an annotation is obtained for every exon of a given gene, the resulting sequence will be the complete CDS.";
  public static final String PARAM_CONCATENATE_EXONS_HELP_GUI =
    toHtml(
      PARAM_CONCATENATE_EXONS_HELP, asList("concatenate exons"), emptyList(), true
    );
}
