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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DefaultClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DockerClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.gui.ClustalOmegaAlignmentTransformationProvider;
import org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo;
import org.sing_group.seda.core.io.JsonObjectReader;

import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class ClustalOmegaAlignmentCommand extends SedaCommand {

  private static final String OPTION_NUM_THREADS_NAME = ClustalOmegaAlignmentSedaPluginInfo.PARAM_NUM_THREADS_NAME;
  private static final String OPTION_ADDITIONAL_PARAMETERS_NAME =
    ClustalOmegaAlignmentSedaPluginInfo.PARAM_ADDITIONAL_PARAMETERS_NAME;
  private static final String OPTION_DOCKER_MODE_NAME = ClustalOmegaAlignmentSedaPluginInfo.PARAM_DOCKER_MODE_NAME;
  private static final String OPTION_LOCAL_MODE_NAME = ClustalOmegaAlignmentSedaPluginInfo.PARAM_LOCAL_MODE_NAME;

  public static final IntegerDefaultValuedStringConstructedOption OPTION_NUM_THREADS =
    new IntegerDefaultValuedStringConstructedOption(
      OPTION_NUM_THREADS_NAME, ClustalOmegaAlignmentSedaPluginInfo.PARAM_NUM_THREADS_SHORT_NAME,
      ClustalOmegaAlignmentSedaPluginInfo.PARAM_NUM_THREADS_DESCRIPTION, 1
    );

  public static final StringOption OPTION_ADDITIONAL_PARAMETERS =
    new StringOption(
      OPTION_ADDITIONAL_PARAMETERS_NAME, ClustalOmegaAlignmentSedaPluginInfo.PARAM_ADDITIONAL_PARAMETERS_SHORT_NAME,
      ClustalOmegaAlignmentSedaPluginInfo.PARAM_ADDITIONAL_PARAMETERS_DESCRIPTION, true, false
    );

  public static final StringOption OPTION_DOCKER_MODE =
    new StringOption(
      OPTION_DOCKER_MODE_NAME, ClustalOmegaAlignmentSedaPluginInfo.PARAM_DOCKER_MODE_SHORT_NAME,
      ClustalOmegaAlignmentSedaPluginInfo.PARAM_DOCKER_MODE_DESCRIPTION, true, true
    );

  public static final StringOption OPTION_LOCAL_MODE =
    new StringOption(
      OPTION_LOCAL_MODE_NAME, ClustalOmegaAlignmentSedaPluginInfo.PARAM_LOCAL_MODE_SHORT_NAME,
      ClustalOmegaAlignmentSedaPluginInfo.PARAM_LOCAL_MODE_DESCRIPTION, true, true
    );

  @Override
  public String getName() {
    return ClustalOmegaAlignmentSedaPluginInfo.SHORT_NAME;
  }

  @Override
  public String getDescriptiveName() {
    return ClustalOmegaAlignmentSedaPluginInfo.NAME;
  }

  @Override
  public String getDescription() {
    return ClustalOmegaAlignmentSedaPluginInfo.DESCRIPTION;
  }

  @Override
  public ClustalOmegaAlignmentTransformationProvider getTransformation(Parameters parameters) {
    ClustalOmegaAlignmentTransformationProvider provider = new ClustalOmegaAlignmentTransformationProvider();

    if (parameters.hasOption(OPTION_DOCKER_MODE) && parameters.hasOption(OPTION_LOCAL_MODE)) {
      throw new IllegalArgumentException("Only one execution mode can be specified");
    }

    if (parameters.hasOption(OPTION_ADDITIONAL_PARAMETERS)) {
      provider.setAdditionalParameters(parameters.getSingleValueString(OPTION_ADDITIONAL_PARAMETERS));
    }

    if (parameters.hasOption(OPTION_NUM_THREADS)) {
      provider.setNumThreads(parameters.getSingleValue(OPTION_NUM_THREADS));
    }

    ClustalOmegaBinariesExecutor executor =
      new DockerClustalOmegaBinariesExecutor(DockerClustalOmegaBinariesExecutor.getDefaultDockerImage());

    if (parameters.hasOption(OPTION_LOCAL_MODE)) {

      File clustalBinaryFile = new File(parameters.getSingleValueString(OPTION_LOCAL_MODE));

      if (clustalBinaryFile.isFile()) {
        executor = new DefaultClustalOmegaBinariesExecutor(clustalBinaryFile);
      } else {
        throw new IllegalArgumentException("Clustal binary file is not a file");
      }
    }

    if (parameters.hasOption(OPTION_DOCKER_MODE)) {
      executor = new DockerClustalOmegaBinariesExecutor(parameters.getSingleValue(OPTION_DOCKER_MODE));
    }

    provider.setBinariesExecutor(Optional.of(executor));

    return provider;

  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    return asList(OPTION_NUM_THREADS, OPTION_ADDITIONAL_PARAMETERS, OPTION_DOCKER_MODE, OPTION_LOCAL_MODE);
  }

  @Override
  protected ClustalOmegaAlignmentTransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<ClustalOmegaAlignmentTransformationProvider>()
      .read(parametersFile, ClustalOmegaAlignmentTransformationProvider.class);
  }

}
