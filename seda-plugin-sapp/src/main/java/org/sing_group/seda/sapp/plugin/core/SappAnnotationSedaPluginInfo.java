/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.plugin.core;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static org.sing_group.seda.core.SedaContext.SEDA_JAVA_PATH_PROPERTY;
import static org.sing_group.seda.util.SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION;

import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;
import org.sing_group.seda.sapp.transformation.sequencesgroup.SappAnnotationSequencesGroupTransformation;

public class SappAnnotationSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Augustus (SAPP)";
  public static final String SHORT_NAME = "augustus-sapp";
  public static final String DESCRIPTION =
    "Annotate an eukaryotic genome or sequence of interest by predicting genes using Augustus.";
  public static final String GROUP = Group.GROUP_GENE_ANNOTATION.getName();
  
  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION_SAPP = PROPERTY_ENABLE_LOCAL_EXECUTION + ".sapp";
  
  public static final String PARAM_SAPP_SPECIES_NAME = "species";
  public static final String PARAM_SAPP_SPECIES_SHORT_NAME = "sp";
  public static final String PARAM_SAPP_SPECIES_DESCRIPTION = "Species";
  public static final String PARAM_SAPP_SPECIES_HELP_BASE = "The species to use.";
  public static final String PARAM_SAPP_SPECIES_HELP = shortEnumString(PARAM_SAPP_SPECIES_HELP_BASE, SappSpecies.class);
  public static final String PARAM_SAPP_SPECIES_HELP_GUI = PARAM_SAPP_SPECIES_HELP_BASE;
  
  public static final String PARAM_CODON_NAME = "codon-table";
  public static final String PARAM_CODON_SHORT_NAME = "ct";
  public static final String PARAM_CODON_DESCRIPTION = "Codon";
  public static final String PARAM_CODON_HELP_BASE = "The codon table to use.";
  public static final String PARAM_CODON_HELP = 
    longEnumStringForCli(
      PARAM_CODON_HELP_BASE,
      stream(SappCodon.values()).collect(toMap(sc -> Integer.toString(sc.getParamValue()), SappCodon::getDescription))
    );
  public static final String PARAM_CODON_HELP_GUI = PARAM_CODON_HELP_BASE;
  
  public static final String PARAM_LOCAL_MODE_HELP =
    "Whether to use local binaries to run SAPP. You must provide the path to the SAPP jars directory and, optionally, the Java executable path.";
  public static final String PARAM_DOCKER_MODE_HELP =
    "The SAPP docker image. By default, the official SEDA image for SAPP is used. "
      + "If you provide a custom image, it should have Java and the required SAPP jars available at the specified paths.";
  public static final String PARAM_DOCKER_MODE_HELP_GUI = toHtml(PARAM_DOCKER_MODE_HELP);
  
  public static final String PARAM_JAVA_PATH_NAME = "java-path";
  public static final String PARAM_JAVA_PATH_SHORT_NAME = "jp";
  public static final String PARAM_JAVA_PATH_DESCRIPTION = "Java path";
  public static final String PARAM_JAVA_PATH_HELP =
    "The path to the directory that contains the java executable. Leave it empty if the java command is available in the path.";
  public static final String PARAM_JAVA_PATH_HELP_GUI =
    toHtml(PARAM_JAVA_PATH_HELP);

  public static final String PARAM_SAPP_JARS_PATH_NAME = "sapp-jars-path";
  public static final String PARAM_SAPP_JARS_PATH_SHORT_NAME = "sp";
  public static final String PARAM_SAPP_JARS_PATH_DESCRIPTION = "SAPP jars path";
  public static final String PARAM_SAPP_JARS_PATH_HELP = "The path to the directory that contains the SAPP jar files.";
  public static final String PARAM_SAPP_JARS_PATH_HELP_GUI = toHtml(PARAM_SAPP_JARS_PATH_HELP);
  
  public static final String DEFAULT_JAVA_PATH = System.getProperty(SEDA_JAVA_PATH_PROPERTY, "");
  public static final String DEFAULT_SAPP_SPECIES =
    SappAnnotationSequencesGroupTransformation.DEFAULT_SAPP_SPECIES.name();
  public static final int DEFAULT_CODON = SappAnnotationSequencesGroupTransformation.DEFAULT_CODON.getParamValue();
}
