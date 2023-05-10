/*
 * #%L
 * SEquence DAtaset builder EMBOSS plugin
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
package org.sing_group.seda.emboss.core;

import static java.util.Arrays.asList;
import static org.sing_group.seda.util.SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION;

import java.util.stream.Stream;

import org.sing_group.seda.emboss.getorf.datatype.FindParam;
import org.sing_group.seda.emboss.getorf.datatype.TableParam;
import org.sing_group.seda.emboss.transformation.sequencesgroup.GetOrfSequencesGroupTransformation;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class EmbossGetOrfSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "getorf (EMBOSS)";
  public static final String SHORT_NAME = "getorf";
  public static final String DESCRIPTION =
    "Find and extract all open reading frames (ORFs) with length longer than that specified by user, using the getorf program from the EMBOSS suite.";
  public static final String GROUP = Group.GROUP_GENE_ANNOTATION.getName();

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION_EMBOSS = PROPERTY_ENABLE_LOCAL_EXECUTION + ".emboss";
  
  public static final String PARAM_DOCKER_MODE_HELP =
    "The EMBOSS docker image. By default, the official SEDA image for EMBOSS is used. "
      + "If you provide a custom image, it should have the BLAST commands available in the path.";
  public static final String PARAM_DOCKER_MODE_HELP_GUI = toHtml(PARAM_DOCKER_MODE_HELP);

  public static final String PARAM_LOCAL_MODE_HELP =
    "The directory that contains the EMBOSS binaries. Leave it empty if they are in the path.";
  public static final String PARAM_LOCAL_MODE_HELP_GUI =
    toHtml(PARAM_LOCAL_MODE_HELP, asList("directory", "blast binaries"), asList(), false);
  
  public static final String PARAM_TABLE_NAME = "table";
  public static final String PARAM_TABLE_SHORT_NAME = "t";
  public static final String PARAM_TABLE_DESCRIPTION = "Table";
  public static final String PARAM_TABLE_HELP =
    longEnumStringForCli(
      "The code to use.",
      cliMap(TableParam.values(), Stream.of(TableParam.values()).map(TableParam::getDescription).toArray(String[]::new))
    );
  public static final String PARAM_TABLE_HELP_GUI = "The code to use.";
  
  public static final String PARAM_FIND_NAME = "find";
  public static final String PARAM_FIND_SHORT_NAME = "f";
  public static final String PARAM_FIND_DESCRIPTION = "Find";
  public static final String PARAM_FIND_HELP = shortEnumString(
      "The first four options are to select either the protein translation "
      + "or the original nucleic acid sequence of the open reading frame. There are two possible definitions of an "
      + "open reading frame: it can either be a region that is free of STOP codons or a region that begins with a "
      + "START codon and ends with a STOP codon. The last three options are probably only of interest to "
      + "people who wish to investigate the statistical properties of the regions around potential START or STOP "
      + "codons. The last option assumes that ORF lengths are calculated between two STOP codons.", 
      FindParam.class
    );
  public static final String PARAM_FIND_HELP_GUI = toHtml(PARAM_FIND_HELP, true); 
  
  public static final String PARAM_MIN_SIZE_NAME = "min-size";
  public static final String PARAM_MIN_SIZE_SHORT_NAME = "mis";
  public static final String PARAM_MIN_SIZE_DESCRIPTION = "Min. size";
  public static final String PARAM_MIN_SIZE_HELP = "The minimum nucleotide size of ORF to report (any integer value).";
  public static final String PARAM_MIN_SIZE_HELP_GUI = PARAM_MIN_SIZE_HELP; 
  
  public static final String PARAM_MAX_SIZE_NAME = "max-size";
  public static final String PARAM_MAX_SIZE_SHORT_NAME = "mas";
  public static final String PARAM_MAX_SIZE_DESCRIPTION = "Max. size";
  public static final String PARAM_MAX_SIZE_HELP = "The maximum nucleotide size of ORF to report (any integer value).";
  public static final String PARAM_MAX_SIZE_HELP_GUI = PARAM_MAX_SIZE_HELP; 
  
  public static final String PARAM_ADDITIONAL_PARAMS_NAME = "additional-params";
  public static final String PARAM_ADDITIONAL_PARAMS_SHORT_NAME = "ad";
  public static final String PARAM_ADDITIONAL_PARAMS_DESCRIPTION = "Additional parameters";
  public static final String PARAM_ADDITIONAL_PARAMS_HELP = "Additional parameters for the EMBOSS getorf command.";
  public static final String PARAM_ADDITIONAL_PARAMS_HELP_GUI = PARAM_ADDITIONAL_PARAMS_HELP;

  public static final String DEFAULT_TABLE = TableParam.STANDARD.name().toLowerCase();
  public static final int DEFAULT_MIN_SIZE = GetOrfSequencesGroupTransformation.DEFAULT_MIN_SIZE;
  public static final int DEFAULT_MAX_SIZE = GetOrfSequencesGroupTransformation.DEFAULT_MAX_SIZE;
}
