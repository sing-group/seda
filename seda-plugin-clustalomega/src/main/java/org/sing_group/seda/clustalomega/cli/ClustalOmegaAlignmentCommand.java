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

import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Optional;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DefaultClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DockerClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.gui.ClustalOmegaAlignmentTransformationProvider;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

import es.uvigo.ei.sing.yacli.command.option.BooleanOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class ClustalOmegaAlignmentCommand extends SedaCommand {

  private static final String OPTION_NUM_THREADS_NAME = "num-threads";
  private static final String OPTION_ADDITIONAL_PARAMETERS_NAME = "additional-parameters";
  private static final String OPTION_DOCKER_MODE_NAME = "docker-mode";
  private static final String OPTION_LOCAL_MODE_NAME = "local-mode";

  public static final IntegerDefaultValuedStringConstructedOption OPTION_NUM_THREADS =
    new IntegerDefaultValuedStringConstructedOption(
      OPTION_NUM_THREADS_NAME, "th", "Number of threads to use.", 1
    );

  public static final StringOption OPTION_ADDITIONAL_PARAMETERS =
    new StringOption(
      OPTION_ADDITIONAL_PARAMETERS_NAME, "rm", "Additional parameters for the Clustal Omega command.", true, false
    );

  public static final BooleanOption OPTION_DOCKER_MODE =
    new BooleanOption(
      OPTION_DOCKER_MODE_NAME, "dk", "Uses a docker image to execute the transformation", true, false
    );

  public static final BooleanOption OPTION_LOCAL_MODE =
    new BooleanOption(
      OPTION_LOCAL_MODE_NAME, "bi", "Uses a local binary to execute the transformation", true, false
    );

  @Override
  public String getName() {
    return "clustal";
  }

  @Override
  public String getDescriptiveName() {
    return "This operation permits using Clustal Omega (http://www.clustal.org/omega/) to align the input FASTA files";
  }

  @Override
  public String getDescription() {
    return "This operation permits using Clustal Omega (http://www.clustal.org/omega/) to align the input FASTA files";
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(Parameters parameters) {
    ClustalOmegaAlignmentTransformationProvider provider = new ClustalOmegaAlignmentTransformationProvider();

    if (parameters.hasOption(OPTION_ADDITIONAL_PARAMETERS)) {
      provider.setAdditionalParameters(parameters.getSingleValueString(OPTION_ADDITIONAL_PARAMETERS));
    }

    if (parameters.hasOption(OPTION_NUM_THREADS)) {
      provider.setNumThreads(parameters.getSingleValue(OPTION_NUM_THREADS));
    }

    ClustalOmegaBinariesExecutor executor;

    if (parameters.hasOption(OPTION_DOCKER_MODE)) {
      executor = new DockerClustalOmegaBinariesExecutor();
    } else if (parameters.hasOption(OPTION_LOCAL_MODE)) {
      executor = new DefaultClustalOmegaBinariesExecutor();
    } else {
      throw new MissingFormatArgumentException(
        "Necessary choose an execution mode: \n" + "-" + OPTION_DOCKER_MODE_NAME + "\n" + "-" + OPTION_LOCAL_MODE_NAME
      );
    }

    provider.setBinariesExecutor(Optional.of(executor));

    return provider.getTransformation(DatatypeFactory.getDefaultDatatypeFactory());

  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    return asList(OPTION_NUM_THREADS, OPTION_ADDITIONAL_PARAMETERS, OPTION_DOCKER_MODE, OPTION_LOCAL_MODE);
  }

}
