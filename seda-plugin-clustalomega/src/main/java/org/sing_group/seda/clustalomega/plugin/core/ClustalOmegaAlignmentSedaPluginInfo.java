/*
 * #%L
 * SEquence DAtaset builder Clustal Omega plugin
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
package org.sing_group.seda.clustalomega.plugin.core;

import static java.util.Arrays.asList;

import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;
import org.sing_group.seda.util.SedaProperties;

public class ClustalOmegaAlignmentSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Clustal Omega Alignment";
  public static final String SHORT_NAME = "clustal-align";
  public static final String DESCRIPTION = "Use Clustal Omega to align the input FASTA files.";
  public static final String GROUP = Group.GROUP_ALIGNMENT.getName();

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION_CLUSTAL_OMEGA =
    SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION + ".clustalomega";

  public static final String PARAM_NUM_THREADS_NAME = "num-threads";
  public static final String PARAM_NUM_THREADS_SHORT_NAME = "th";
  public static final String PARAM_NUM_THREADS_DESCRIPTION = "Num. threads";
  public static final String PARAM_NUM_THREADS_HELP = "Number of threads to use.";
  public static final String PARAM_NUM_THREADS_HELP_GUI = PARAM_NUM_THREADS_HELP;

  public static final String PARAM_ADDITIONAL_PARAMETERS_NAME = "additional-parameters";
  public static final String PARAM_ADDITIONAL_PARAMETERS_SHORT_NAME = "ad";
  public static final String PARAM_ADDITIONAL_PARAMETERS_DESCRIPTION = "Additional parameters";
  public static final String PARAM_ADDITIONAL_PARAMETERS_HELP = "Additional parameters for the Clustal Omega command.";
  public static final String PARAM_ADDITIONAL_PARAMETERS_HELP_GUI = PARAM_ADDITIONAL_PARAMETERS_HELP;

  public static final String PARAM_DOCKER_MODE_HELP =
    "The Clustal Omega docker image. By default, the official SEDA image for Clustal Omega is used. "
      + "If you provide a custom image, you may also need to specify the Clustal Omega executable "
      + "command inside the image, in case it is not defined as ENTRYPOINT.";
  public static final String PARAM_DOCKER_MODE_HELP_GUI = toHtml(PARAM_DOCKER_MODE_HELP);

  public static final String PARAM_LOCAL_MODE_HELP =
    "The Clustal Omega binary file. If the Clustal Omega binary is in the path (clustalo in Unix "
      + "systems and clustalo.exe in Windows systems), then this can be empty and the Check binary would "
      + "say that it is right.";
  public static final String PARAM_LOCAL_MODE_HELP_GUI =
    toHtml(PARAM_LOCAL_MODE_HELP, asList("clustalo.exe", "clustalo"), asList("Check binary"), true);
}
