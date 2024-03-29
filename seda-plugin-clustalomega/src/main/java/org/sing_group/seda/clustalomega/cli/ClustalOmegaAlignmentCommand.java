/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.clustalomega.cli;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.GROUP;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.NAME;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_ADDITIONAL_PARAMETERS_HELP;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_ADDITIONAL_PARAMETERS_NAME;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_ADDITIONAL_PARAMETERS_SHORT_NAME;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_DOCKER_MODE_HELP;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_LOCAL_MODE_HELP;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_NUM_THREADS_HELP;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_NUM_THREADS_NAME;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PARAM_NUM_THREADS_SHORT_NAME;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_CLUSTAL_OMEGA;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DefaultClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DockerClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.transformation.provider.ClustalOmegaAlignmentTransformationProvider;
import org.sing_group.seda.core.io.JsonObjectReader;

import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class ClustalOmegaAlignmentCommand extends ExternalSoftwareExecutionCommand {

  public static final IntegerDefaultValuedStringConstructedOption OPTION_NUM_THREADS =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_NUM_THREADS_NAME, PARAM_NUM_THREADS_SHORT_NAME,
      PARAM_NUM_THREADS_HELP, 1
    );

  public static final StringOption OPTION_ADDITIONAL_PARAMETERS =
    new StringOption(
      PARAM_ADDITIONAL_PARAMETERS_NAME, PARAM_ADDITIONAL_PARAMETERS_SHORT_NAME,
      PARAM_ADDITIONAL_PARAMETERS_HELP, true, false
    );

  public static final StringOption OPTION_DOCKER_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      PARAM_DOCKER_MODE_NAME, PARAM_DOCKER_MODE_SHORT_NAME,
      PARAM_DOCKER_MODE_HELP, true, true
    );

  public static final StringOption OPTION_LOCAL_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      PARAM_LOCAL_MODE_NAME, PARAM_LOCAL_MODE_SHORT_NAME,
      PARAM_LOCAL_MODE_HELP, true, true
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
  public ClustalOmegaAlignmentTransformationProvider getTransformation(Parameters parameters) {
    ClustalOmegaAlignmentTransformationProvider provider = new ClustalOmegaAlignmentTransformationProvider();

    if (parameters.hasOption(OPTION_ADDITIONAL_PARAMETERS)) {
      provider.setAdditionalParameters(parameters.getSingleValueString(OPTION_ADDITIONAL_PARAMETERS));
    }

    if (parameters.hasOption(OPTION_NUM_THREADS)) {
      provider.setNumThreads(parameters.getSingleValue(OPTION_NUM_THREADS));
    }

    provider.setBinariesExecutor(of(getClustalOmegaBinariesExecutor(parameters)));

    return provider;
  }

  public ClustalOmegaBinariesExecutor getClustalOmegaBinariesExecutor(Parameters parameters) {
    validateSingleExecutionMode(parameters, OPTION_LOCAL_MODE, OPTION_DOCKER_MODE);

    ClustalOmegaBinariesExecutor executor =
      new DockerClustalOmegaBinariesExecutor(DockerClustalOmegaBinariesExecutor.getDefaultDockerImage());

    if (parameters.hasOption(OPTION_LOCAL_MODE)) {
      File clustalBinaryFile = new File(parameters.getSingleValueString(OPTION_LOCAL_MODE));

      if (clustalBinaryFile.isFile()) {
        executor = new DefaultClustalOmegaBinariesExecutor(clustalBinaryFile);
      } else {
        formattedValidationError("The Clustal binary file is not a file");
      }
    }

    if (parameters.hasOption(OPTION_DOCKER_MODE)) {
      executor = new DockerClustalOmegaBinariesExecutor(parameters.getSingleValue(OPTION_DOCKER_MODE));
    }
    return executor;
  }

  @Override
  protected Map<Option<?>, String> getLocalOptionsToEnablePropertyMap() {
    return fromKeyValue(OPTION_LOCAL_MODE, PROPERTY_ENABLE_LOCAL_EXECUTION_CLUSTAL_OMEGA);
  }

  @Override
  protected List<Option<?>> getLocalOptionsList() {
    return asList(OPTION_LOCAL_MODE);
  }

  @Override
  protected List<Option<?>> createExternalSedaOptions() {
    return asList(OPTION_NUM_THREADS, OPTION_ADDITIONAL_PARAMETERS, OPTION_DOCKER_MODE, OPTION_LOCAL_MODE);
  }

  @Override
  protected ClustalOmegaAlignmentTransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<ClustalOmegaAlignmentTransformationProvider>()
      .read(parametersFile, ClustalOmegaAlignmentTransformationProvider.class);
  }
}
