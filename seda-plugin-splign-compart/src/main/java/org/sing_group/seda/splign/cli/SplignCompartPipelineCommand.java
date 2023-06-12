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
package org.sing_group.seda.splign.cli;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static org.sing_group.seda.bedtools.core.BedToolsSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_BEDTOOLS;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_SHORT_NAME;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.GROUP;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.NAME;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_CONCATENATE_EXONS_HELP;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_CONCATENATE_EXONS_NAME;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_CONCATENATE_EXONS_SHORT_NAME;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_EXTERNAL_QUERY_FILE_HELP;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_EXTERNAL_QUERY_FILE_NAME;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PARAM_EXTERNAL_QUERY_FILE_SHORT_NAME;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_SPLIGN_COMPART;
import static org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.sing_group.seda.bedtools.cli.BedToolsCommandUtils;
import org.sing_group.seda.bedtools.core.BedToolsSedaPluginInfo;
import org.sing_group.seda.blast.cli.blast.BlastCommand;
import org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo;
import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.splign.execution.DefaultSplignCompartBinariesExecutor;
import org.sing_group.seda.splign.execution.DockerSplignCompartBinariesExecutor;
import org.sing_group.seda.splign.execution.SplignCompartBinariesExecutor;
import org.sing_group.seda.splign.plugin.core.SplignCompartPipelineSedaPluginInfo;
import org.sing_group.seda.splign.transformation.provider.SplignCompartPipelineTransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class SplignCompartPipelineCommand extends ExternalSoftwareExecutionCommand {
  
  public static final StringOption OPTION_SPLIGN_COMPART_DOCKER_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      "splign-compart-" + PARAM_DOCKER_MODE_NAME, "splign-compart-" + PARAM_DOCKER_MODE_SHORT_NAME,
      SplignCompartPipelineSedaPluginInfo.PARAM_DOCKER_MODE_HELP, true, true
    );

  public static final StringOption OPTION_SPLIGN_COMPART_LOCAL_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      "splign-compart-" + PARAM_LOCAL_MODE_NAME, "splign-compart-" + PARAM_LOCAL_MODE_SHORT_NAME,
      SplignCompartPipelineSedaPluginInfo.PARAM_LOCAL_MODE_HELP, true, true
    );
  
  public static final FileOption OPTION_EXTERNAL_QUERY_FILE =
    new FileOption(
      PARAM_EXTERNAL_QUERY_FILE_NAME, PARAM_EXTERNAL_QUERY_FILE_SHORT_NAME, PARAM_EXTERNAL_QUERY_FILE_HELP, true, true
    );
  
  public static final FlagOption OPTION_CONCATENATE_EXONS =
    new FlagOption(
      PARAM_CONCATENATE_EXONS_NAME, PARAM_CONCATENATE_EXONS_SHORT_NAME, PARAM_CONCATENATE_EXONS_HELP
    );
  
  public static final StringOption OPTION_BLAST_DOCKER_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      "blast-" + PARAM_DOCKER_MODE_NAME, "blast-" + PARAM_DOCKER_MODE_SHORT_NAME,
      BlastSedaPluginInfo.PARAM_DOCKER_MODE_HELP, true, true
    );

  public static final StringOption OPTION_BLAST_LOCAL_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      "blast-" + PARAM_LOCAL_MODE_NAME, "blast-" + PARAM_LOCAL_MODE_SHORT_NAME,
      BlastSedaPluginInfo.PARAM_LOCAL_MODE_HELP, true, true
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
    return DESCRIPTION;
  }
  

  @Override
  protected String getSedaGroup() {
    return GROUP;
  }

  @Override
  protected List<Option<?>> getMandatoryOptions() {
    return asList(OPTION_EXTERNAL_QUERY_FILE);
  }
  
  @Override
  protected List<Option<?>> getLocalOptionsList() {
    return asList(
      OPTION_SPLIGN_COMPART_LOCAL_MODE,
      OPTION_BLAST_LOCAL_MODE,
      OPTION_BEDTOOLS_LOCAL_MODE
   );
  }

  @Override
  protected Map<Option<?>, String> getLocalOptionsToEnablePropertyMap() {
    return fromLists(
      getLocalOptionsList(), 
      asList(
        PROPERTY_ENABLE_LOCAL_EXECUTION_SPLIGN_COMPART,
        PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST,
        PROPERTY_ENABLE_LOCAL_EXECUTION_BEDTOOLS
      )
    );
  }

  @Override
  protected List<Option<?>> createExternalSedaOptions() {
    return asList(
      OPTION_SPLIGN_COMPART_LOCAL_MODE,
      OPTION_SPLIGN_COMPART_DOCKER_MODE,
      OPTION_EXTERNAL_QUERY_FILE,
      OPTION_CONCATENATE_EXONS,
      OPTION_BLAST_LOCAL_MODE,
      OPTION_BLAST_DOCKER_MODE,
      OPTION_BEDTOOLS_LOCAL_MODE,
      OPTION_BEDTOOLS_DOCKER_MODE
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    SplignCompartPipelineTransformationProvider provider = new SplignCompartPipelineTransformationProvider();
    
    provider.setQueryFile(getExistingFile(parameters, OPTION_EXTERNAL_QUERY_FILE));
    provider.setConcatenateExons(parameters.hasFlag(OPTION_CONCATENATE_EXONS));
    
    provider.setSplignCompartBinariesExecutor(
      of(getSplignCompartBinariesExecutor(parameters)));
    provider.setBlastBinariesExecutor(
      of(BlastCommand.getBlastBinariesExecutor(parameters, OPTION_BLAST_LOCAL_MODE, OPTION_BLAST_DOCKER_MODE))
    );
    provider.setBedToolsBinariesExecutor(
      of(BedToolsCommandUtils.getBedToolsBinariesExecutor(parameters, OPTION_BEDTOOLS_LOCAL_MODE, OPTION_BEDTOOLS_DOCKER_MODE)));

    return provider;
  }

  private SplignCompartBinariesExecutor getSplignCompartBinariesExecutor(Parameters parameters) {
    validateSingleExecutionMode(
      parameters, OPTION_SPLIGN_COMPART_LOCAL_MODE, OPTION_SPLIGN_COMPART_DOCKER_MODE, " for Splign/Compart"
    );
    
    SplignCompartBinariesExecutor executor =
      new DockerSplignCompartBinariesExecutor(DockerSplignCompartBinariesExecutor.getDefaultDockerImage());

    if (parameters.hasOption(OPTION_SPLIGN_COMPART_LOCAL_MODE)) {
      File splignCompartDirectory = new File(parameters.getSingleValueString(OPTION_SPLIGN_COMPART_LOCAL_MODE));

      if (splignCompartDirectory.isDirectory()) {
        executor = new DefaultSplignCompartBinariesExecutor(splignCompartDirectory);
      } else {
        formattedValidationError("The specified Splign/Compart directory does not exist or it is not a directory.");
      }
    }

    if (parameters.hasOption(OPTION_SPLIGN_COMPART_DOCKER_MODE)) {
      executor = new DockerSplignCompartBinariesExecutor(parameters.getSingleValue(OPTION_SPLIGN_COMPART_DOCKER_MODE));
    }

    return executor;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<SplignCompartPipelineTransformationProvider>()
      .read(parametersFile, SplignCompartPipelineTransformationProvider.class);
  }
}
