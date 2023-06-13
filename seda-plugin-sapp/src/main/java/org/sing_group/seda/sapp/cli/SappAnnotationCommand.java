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
package org.sing_group.seda.sapp.cli;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static org.sing_group.seda.bedtools.core.BedToolsSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_BEDTOOLS;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_SHORT_NAME;
import static org.sing_group.seda.sapp.execution.DefaultDockerSappCommands.DEFAULT_DOCKER_IMAGE;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.DEFAULT_JAVA_PATH;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.DEFAULT_SAPP_SPECIES;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.GROUP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.NAME;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_CODON_HELP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_CODON_NAME;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_CODON_SHORT_NAME;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_DOCKER_MODE_HELP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_JAVA_PATH_HELP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_JAVA_PATH_NAME;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_JAVA_PATH_SHORT_NAME;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_LOCAL_MODE_HELP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_SAPP_JARS_PATH_HELP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_SAPP_JARS_PATH_NAME;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_SAPP_JARS_PATH_SHORT_NAME;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_SAPP_SPECIES_HELP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_SAPP_SPECIES_NAME;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_SAPP_SPECIES_SHORT_NAME;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_SAPP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.sing_group.seda.bedtools.cli.BedToolsCommandUtils;
import org.sing_group.seda.bedtools.core.BedToolsSedaPluginInfo;
import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;
import org.sing_group.seda.sapp.execution.DefaultDockerSappCommands;
import org.sing_group.seda.sapp.execution.DefaultSappBinariesExecutor;
import org.sing_group.seda.sapp.execution.DefaultSappCommands;
import org.sing_group.seda.sapp.execution.DockerSappBinariesExecutor;
import org.sing_group.seda.sapp.execution.SappBinariesExecutor;
import org.sing_group.seda.sapp.execution.SappEnvironment;
import org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo;
import org.sing_group.seda.sapp.transformation.provider.SappAnnotationTransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class SappAnnotationCommand extends ExternalSoftwareExecutionCommand {

  public static final DefaultValuedStringOption OPTION_SPECIES =
    new DefaultValuedStringOption(
      PARAM_SAPP_SPECIES_NAME, PARAM_SAPP_SPECIES_SHORT_NAME, PARAM_SAPP_SPECIES_HELP,
      DEFAULT_SAPP_SPECIES
    );

  public static final DefaultValuedStringOption OPTION_CODON =
    new DefaultValuedStringOption(
      PARAM_CODON_NAME, PARAM_CODON_SHORT_NAME, PARAM_CODON_HELP,
      Integer.toString(SappAnnotationSedaPluginInfo.DEFAULT_CODON)
    );
  
  public static final DefaultValuedStringOption OPTION_DOCKER_MODE =
    new DefaultValuedStringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      PARAM_DOCKER_MODE_NAME, PARAM_DOCKER_MODE_SHORT_NAME,
      PARAM_DOCKER_MODE_HELP, DEFAULT_DOCKER_IMAGE
    );

  public static final FlagOption OPTION_LOCAL_MODE =
    new FlagOption(
      SOFTWARE_EXECUTION_CATEGORY,
      PARAM_LOCAL_MODE_NAME, PARAM_LOCAL_MODE_SHORT_NAME, PARAM_LOCAL_MODE_HELP
    );
  
  public static final DefaultValuedStringOption OPTION_JAVA_PATH =
    new DefaultValuedStringOption(
      SOFTWARE_EXECUTION_CATEGORY, 
      PARAM_JAVA_PATH_NAME, PARAM_JAVA_PATH_SHORT_NAME, PARAM_JAVA_PATH_HELP, 
      DEFAULT_JAVA_PATH
    );
  
  public static final StringOption OPTION_SAPP_JARS_PATH =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      PARAM_SAPP_JARS_PATH_NAME, PARAM_SAPP_JARS_PATH_SHORT_NAME, PARAM_SAPP_JARS_PATH_HELP,
      true, true
    );

  public static final StringOption OPTION_BEDTOOLS_DOCKER_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      "bedtools-" + PARAM_DOCKER_MODE_NAME, "bedtools-" + PARAM_DOCKER_MODE_SHORT_NAME,
      BedToolsSedaPluginInfo.PARAM_DOCKER_MODE_HELP, true, true
    );

  public static final StringOption OPTION_BEDTOOLS_LOCAL_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      "bedtools-" + PARAM_LOCAL_MODE_NAME, "bedtools-" + PARAM_LOCAL_MODE_SHORT_NAME,
      BedToolsSedaPluginInfo.PARAM_LOCAL_MODE_HELP, true, true
    );
  
  @Override
  public String getName() {
    return SHORT_NAME;
  }

  @Override
  public String getDescriptiveName() {
    return NAME;
  }

  @Override
  public String getDescription() {
    return SappAnnotationSedaPluginInfo.DESCRIPTION;
  }
  
  @Override
  protected String getSedaGroup() {
    return GROUP;
  }

  @Override
  protected List<Option<?>> getLocalOptionsList() {
    return asList(OPTION_LOCAL_MODE);
  }

  @Override
  protected Map<Option<?>, String> getLocalOptionsToEnablePropertyMap() {
    return fromLists(
      asList(OPTION_LOCAL_MODE, OPTION_BEDTOOLS_LOCAL_MODE),
      asList(PROPERTY_ENABLE_LOCAL_EXECUTION_SAPP, PROPERTY_ENABLE_LOCAL_EXECUTION_BEDTOOLS)
    );
  }

  @Override
  protected List<Option<?>> createExternalSedaOptions() {
    return asList(
      OPTION_SPECIES,
      OPTION_CODON,
      OPTION_LOCAL_MODE,
      OPTION_DOCKER_MODE,
      OPTION_JAVA_PATH,
      OPTION_SAPP_JARS_PATH,
      OPTION_BEDTOOLS_LOCAL_MODE,
      OPTION_BEDTOOLS_DOCKER_MODE
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    SappAnnotationTransformationProvider provider = new SappAnnotationTransformationProvider();

    provider.setSappCodon(getSappCodon(parameters));
    provider.setSappSpecies(getEnumValue(parameters, SappSpecies.class, OPTION_SPECIES));

    provider.setSappBinariesExecutor(of(getSappBinariesExecutor(parameters)));

    provider.setBedToolsBinariesExecutor(
      of(
        BedToolsCommandUtils
          .getBedToolsBinariesExecutor(parameters, OPTION_BEDTOOLS_LOCAL_MODE, OPTION_BEDTOOLS_DOCKER_MODE)
      )
    );

    return provider;
  }

  private SappCodon getSappCodon(Parameters parameters) {
    Optional<SappCodon> codon =
      Stream.of(SappCodon.values())
        .filter(c -> c.getParamValue() == Integer.valueOf(parameters.getSingleValueString(OPTION_CODON)))
        .findFirst();

    if (codon.isPresent()) {
      return codon.get();
    } else {
      invalidEnumValue(OPTION_CODON);

      throw new RuntimeException();
    }
  }

  private SappBinariesExecutor getSappBinariesExecutor(Parameters parameters) {
    if (parameters.hasFlag(OPTION_LOCAL_MODE)) {
      SappEnvironment sappEnvironment = SappEnvironment.getInstance();

      String javaPath = parameters.getSingleValueString(OPTION_JAVA_PATH);

      if (javaPath.isEmpty()) {
        return new DefaultSappBinariesExecutor(
          new DefaultSappCommands(
            parameters.getSingleValueString(OPTION_SAPP_JARS_PATH) + "/" + sappEnvironment.getConversionJar(),
            parameters.getSingleValueString(OPTION_SAPP_JARS_PATH) + "/" + sappEnvironment.getGeneCallerJar()
          )
        );
      } else {
        return new DefaultSappBinariesExecutor(
          new DefaultSappCommands(
            javaPath,
            parameters.getSingleValueString(OPTION_SAPP_JARS_PATH) + "/" + sappEnvironment.getConversionJar(),
            parameters.getSingleValueString(OPTION_SAPP_JARS_PATH) + "/" + sappEnvironment.getGeneCallerJar()
          )
        );
      }
    } else {
      String dockerImage = parameters.getSingleValueString(OPTION_DOCKER_MODE);

      if (dockerImage.equals(DEFAULT_DOCKER_IMAGE)) {
        return new DockerSappBinariesExecutor();
      } else {
        SappEnvironment sappEnvironment = SappEnvironment.getInstance();
        return new DockerSappBinariesExecutor(
          new DefaultDockerSappCommands(
            parameters.getSingleValueString(OPTION_JAVA_PATH),
            parameters.getSingleValueString(OPTION_SAPP_JARS_PATH) + "/" + sappEnvironment.getConversionJar(),
            parameters.getSingleValueString(OPTION_SAPP_JARS_PATH) + "/" + sappEnvironment.getGeneCallerJar(),
            dockerImage
          )
        );
      }
    }
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<SappAnnotationTransformationProvider>()
      .read(parametersFile, SappAnnotationTransformationProvider.class);
  }
}
