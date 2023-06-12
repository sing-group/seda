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
package org.sing_group.seda.prosplign.cli;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_SHORT_NAME;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.DEFAULT_MAX_TARGET_SEQS;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.GROUP;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.NAME;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.PARAM_EXTERNAL_QUERY_FILE_HELP;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.PARAM_EXTERNAL_QUERY_FILE_NAME;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.PARAM_EXTERNAL_QUERY_FILE_SHORT_NAME;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.PARAM_MAX_TARGET_SEQS_HELP;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.PARAM_MAX_TARGET_SEQS_NAME;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.PARAM_MAX_TARGET_SEQS_SHORT_NAME;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_PROSPLIGN_PROCOMPART;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sing_group.seda.blast.cli.blast.BlastCommand;
import org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo;
import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.prosplign.execution.DefaultProSplignCompartBinariesExecutor;
import org.sing_group.seda.prosplign.execution.DockerProSplignCompartBinariesExecutor;
import org.sing_group.seda.prosplign.execution.ProSplignCompartBinariesExecutor;
import org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo;
import org.sing_group.seda.prosplign.transformation.provider.ProSplignCompartPipelineTransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class ProSplignCompartPipelineCommand extends ExternalSoftwareExecutionCommand {

  public static final StringOption OPTION_PROSPLIGN_PROCOMPART_DOCKER_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      "prosplign-procompart-" + PARAM_DOCKER_MODE_NAME, "prosplign-procompart-" + PARAM_DOCKER_MODE_SHORT_NAME,
      ProSplignCompartPipelineSedaPluginInfo.PARAM_DOCKER_MODE_HELP, true, true
    );

  public static final StringOption OPTION_PROSPLIGN_PROCOMPART_LOCAL_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      "prosplign-procompart-" + PARAM_LOCAL_MODE_NAME, "prosplign-procompart-" + PARAM_LOCAL_MODE_SHORT_NAME,
      ProSplignCompartPipelineSedaPluginInfo.PARAM_LOCAL_MODE_HELP, true, true
    );
  
  public static final FileOption OPTION_EXTERNAL_QUERY_FILE =
    new FileOption(
      PARAM_EXTERNAL_QUERY_FILE_NAME, PARAM_EXTERNAL_QUERY_FILE_SHORT_NAME, PARAM_EXTERNAL_QUERY_FILE_HELP, true, true
    );
  
  public static final DefaultValuedStringOption OPTION_MAX_TARGET_SEQS =
    new DefaultValuedStringOption(
      PARAM_MAX_TARGET_SEQS_NAME, PARAM_MAX_TARGET_SEQS_SHORT_NAME, PARAM_MAX_TARGET_SEQS_HELP,
      Integer.toString(DEFAULT_MAX_TARGET_SEQS)
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
      OPTION_PROSPLIGN_PROCOMPART_LOCAL_MODE,
      OPTION_BLAST_LOCAL_MODE
   );
  }

  @Override
  protected Map<Option<?>, String> getLocalOptionsToEnablePropertyMap() {
    return fromLists(
      getLocalOptionsList(), 
      asList(
        PROPERTY_ENABLE_LOCAL_EXECUTION_PROSPLIGN_PROCOMPART,
        PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST
      )
    );
  }

  @Override
  protected List<Option<?>> createExternalSedaOptions() {
    return asList(
      OPTION_PROSPLIGN_PROCOMPART_LOCAL_MODE,
      OPTION_PROSPLIGN_PROCOMPART_DOCKER_MODE,
      OPTION_EXTERNAL_QUERY_FILE,
      OPTION_MAX_TARGET_SEQS,
      OPTION_BLAST_LOCAL_MODE,
      OPTION_BLAST_DOCKER_MODE
    );
  }


  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    ProSplignCompartPipelineTransformationProvider provider = new ProSplignCompartPipelineTransformationProvider();

    provider.setProteinQueryFile(getExistingFile(parameters, OPTION_EXTERNAL_QUERY_FILE));
    provider.setMaxTargetSeqs(getIntegerFromStringOption(parameters, OPTION_MAX_TARGET_SEQS));

    provider.setProSplignCompartBinariresExecutor(Optional.of(getProSplignProCompartBinariesExecutor(parameters)));
    provider.setBlastBinariesExecutor(
      of(BlastCommand.getBlastBinariesExecutor(parameters, OPTION_BLAST_LOCAL_MODE, OPTION_BLAST_DOCKER_MODE))
    );

    return provider;
  }

  private ProSplignCompartBinariesExecutor getProSplignProCompartBinariesExecutor(Parameters parameters) {
    validateSingleExecutionMode(
      parameters, OPTION_PROSPLIGN_PROCOMPART_LOCAL_MODE, OPTION_PROSPLIGN_PROCOMPART_DOCKER_MODE,
      " for ProSplign/ProCompart"
    );

    ProSplignCompartBinariesExecutor executor =
      new DockerProSplignCompartBinariesExecutor(DockerProSplignCompartBinariesExecutor.getDefaultDockerImage());

    if (parameters.hasOption(OPTION_PROSPLIGN_PROCOMPART_LOCAL_MODE)) {
      File splignCompartDirectory = new File(parameters.getSingleValueString(OPTION_PROSPLIGN_PROCOMPART_LOCAL_MODE));

      if (splignCompartDirectory.isDirectory()) {
        executor = new DefaultProSplignCompartBinariesExecutor(splignCompartDirectory);
      } else {
        formattedValidationError(
          "The specified ProSplign/ProCompart directory does not exist or it is not a directory."
        );
      }
    }

    if (parameters.hasOption(OPTION_PROSPLIGN_PROCOMPART_DOCKER_MODE)) {
      executor =
        new DockerProSplignCompartBinariesExecutor(parameters.getSingleValue(OPTION_PROSPLIGN_PROCOMPART_DOCKER_MODE));
    }

    return executor;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<ProSplignCompartPipelineTransformationProvider>()
      .read(parametersFile, ProSplignCompartPipelineTransformationProvider.class);
  }
}
