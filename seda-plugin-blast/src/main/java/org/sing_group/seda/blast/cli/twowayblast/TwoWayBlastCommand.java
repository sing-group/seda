/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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
package org.sing_group.seda.blast.cli.twowayblast;

import static java.util.Arrays.asList;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.DEFAULT_BLAST_TYPE;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_DOCKER_MODE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EVALUE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EVALUE_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EVALUE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_LOCAL_MODE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_BLAST_TYPE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_BLAST_TYPE_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_BLAST_TYPE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_FILE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_FILE_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_FILE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_DIRECTORY_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_DIRECTORY_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_DIRECTORY_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.DEFAULT_QUERY_MODE;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.GROUP;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.NAME;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.PARAM_NUM_THREADS_HELP;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.PARAM_NUM_THREADS_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.PARAM_NUM_THREADS_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.PARAM_QUERY_MODE_HELP;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.PARAM_QUERY_MODE_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.PARAM_QUERY_MODE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoWayBlastSedaPluginInfo.SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sing_group.seda.blast.cli.blast.BlastCommand;
import org.sing_group.seda.blast.datatype.TwoWayBlastMode;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DefaultBlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DockerBlastBinariesExecutor;
import org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationProvider;
import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.BigDecimalDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class TwoWayBlastCommand extends ExternalSoftwareExecutionCommand {

  public static final StringOption OPTION_DOCKER_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      PARAM_DOCKER_MODE_NAME, PARAM_DOCKER_MODE_SHORT_NAME, PARAM_DOCKER_MODE_HELP, 
      true, true
    );

  public static final StringOption OPTION_LOCAL_MODE =
    new StringOption(
      SOFTWARE_EXECUTION_CATEGORY,
      PARAM_LOCAL_MODE_NAME, PARAM_LOCAL_MODE_SHORT_NAME, PARAM_LOCAL_MODE_HELP, 
      true, true
    );

  public static final FileOption OPTION_STORE_DATABASES_DIRECTORY =
    new FileOption(
      PARAM_STORE_DATABASES_DIRECTORY_NAME, PARAM_STORE_DATABASES_DIRECTORY_SHORT_NAME, PARAM_STORE_DATABASES_DIRECTORY_HELP, 
      true, true
    );

  public static final DefaultValuedStringOption OPTION_QUERY_MODE =
    new DefaultValuedStringOption(
      PARAM_QUERY_MODE_NAME, PARAM_QUERY_MODE_SHORT_NAME, PARAM_QUERY_MODE_HELP, DEFAULT_QUERY_MODE.name()
    );

  public static final DefaultValuedStringOption OPTION_QUERY_BLAST_TYPE =
    new DefaultValuedStringOption(
      PARAM_QUERY_BLAST_TYPE_NAME, PARAM_QUERY_BLAST_TYPE_SHORT_NAME, PARAM_QUERY_BLAST_TYPE_HELP,
      DEFAULT_BLAST_TYPE.name().toLowerCase()
    );

  public static final FileOption OPTION_QUERY_FILE =
    new FileOption(
      PARAM_QUERY_FILE_NAME, PARAM_QUERY_FILE_SHORT_NAME, PARAM_QUERY_FILE_HELP, 
      true, true
    );

  public static final BigDecimalDefaultValuedStringConstructedOption OPTION_EXPECTATION_VALUE =
    new BigDecimalDefaultValuedStringConstructedOption(
      PARAM_EVALUE_NAME, PARAM_EVALUE_SHORT_NAME, PARAM_EVALUE_HELP, BigDecimal.valueOf(0.05)
    );

  public static final StringOption OPTION_ADDITIONAL_PARAMS =
    new StringOption(
      PARAM_ADDITIONAL_PARAMS_NAME, PARAM_ADDITIONAL_PARAMS_SHORT_NAME, PARAM_ADDITIONAL_PARAMS_HELP,
      true, true
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_NUM_THREADS =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_NUM_THREADS_NAME, PARAM_NUM_THREADS_SHORT_NAME, PARAM_NUM_THREADS_HELP, 1
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
    return asList(OPTION_QUERY_FILE);
  }

  @Override
  protected Map<Option<?>, String> getLocalOptionsToEnablePropertyMap() {
    return fromKeyValue(OPTION_LOCAL_MODE, PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST);
  }

  @Override
  protected List<Option<?>> getLocalOptionsList() {
    return asList(OPTION_LOCAL_MODE);
  }

  @Override
  protected List<Option<?>> createExternalSedaOptions() {
    return asList(
      OPTION_LOCAL_MODE,
      OPTION_DOCKER_MODE,
      OPTION_STORE_DATABASES_DIRECTORY,
      OPTION_QUERY_MODE,
      OPTION_QUERY_BLAST_TYPE,
      OPTION_QUERY_FILE,
      OPTION_EXPECTATION_VALUE,
      OPTION_ADDITIONAL_PARAMS,
      OPTION_NUM_THREADS
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    TwoWayBlastTransformationProvider provider = new TwoWayBlastTransformationProvider();

    provider.setBlastType(this.getBlastType(parameters));
    provider.setEvalue(this.getEvalue(parameters));
    provider.setQueryFile(getExistingFile(parameters, OPTION_QUERY_FILE));
    provider.setQueryMode(this.getQueryMode(parameters));

    Optional<File> databasesDirectory = BlastCommand.getDatabasesDirectory(parameters, OPTION_STORE_DATABASES_DIRECTORY);
    if (databasesDirectory.isPresent()) {
      provider.setStoreDatabases(true);
      provider.setDatabasesDirectory(databasesDirectory.get());
    }

    Optional<String> additionalParameters = this.getAdditionalParameters(parameters);
    additionalParameters.ifPresent(provider::setAdditionalParameters);

    provider.setNumThreads(this.getNumThreads(parameters));
    provider.setBlastBinariesExecutor(this.getBlastBinariesExecutor(parameters));

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<TwoWayBlastTransformationProvider>()
      .read(parametersFile, TwoWayBlastTransformationProvider.class);
  }

  private BlastType getBlastType(Parameters parameters) {
    try {
      return BlastType.valueOf(parameters.getSingleValueString(OPTION_QUERY_BLAST_TYPE).toUpperCase());
    } catch (IllegalArgumentException exc) {
      invalidEnumValue(OPTION_QUERY_BLAST_TYPE);
    }
    throw new IllegalStateException();
  }

  private double getEvalue(Parameters parameters) {
    try {
      return parameters.getSingleValue(OPTION_EXPECTATION_VALUE).doubleValue();
    } catch (NumberFormatException ex) {
      formattedValidationError(
        "Invalid value for " + formatParam(OPTION_EXPECTATION_VALUE) + " ("
          + parameters.getSingleValue(OPTION_EXPECTATION_VALUE)
          + "). It must be a number."
      );
    }
    throw new IllegalStateException();
  }

  private TwoWayBlastMode getQueryMode(Parameters parameters) {
    try {
      return TwoWayBlastMode.valueOf(parameters.getSingleValueString(OPTION_QUERY_MODE).toUpperCase());
    } catch (IllegalArgumentException exc) {
      invalidEnumValue(OPTION_QUERY_MODE);
    }
    throw new IllegalStateException();
  }

  private Optional<String> getAdditionalParameters(Parameters parameters) {
    if (!parameters.hasOption(OPTION_ADDITIONAL_PARAMS)) {
      return Optional.empty();
    } else {
      return Optional.of(parameters.getSingleValue(OPTION_ADDITIONAL_PARAMS));
    }
  }

  private int getNumThreads(Parameters parameters) {
    try {
      return parameters.getSingleValue(OPTION_NUM_THREADS);
    } catch (NumberFormatException ex) {
      formattedValidationError(
        "Invalid value for " + formatParam(OPTION_NUM_THREADS) + " ("
          + parameters.getSingleValue(OPTION_NUM_THREADS)
          + "). It must be a number."
      );
    }
    throw new IllegalStateException();
  }

  private Optional<BlastBinariesExecutor> getBlastBinariesExecutor(Parameters parameters) {
    BlastBinariesExecutor executor =
      new DockerBlastBinariesExecutor(DockerBlastBinariesExecutor.getDefaultDockerImage());

    if (parameters.hasOption(OPTION_LOCAL_MODE)) {

      File blastBinariesDirectory = new File(parameters.getSingleValueString(OPTION_LOCAL_MODE));

      if (blastBinariesDirectory.isDirectory()) {
        executor = new DefaultBlastBinariesExecutor(blastBinariesDirectory);
      } else {
        formattedValidationError("The specified BLAST directory it is not a directory or does not exist.");
      }
    }

    if (parameters.hasOption(OPTION_DOCKER_MODE)) {
      executor = new DockerBlastBinariesExecutor(parameters.getSingleValue(OPTION_DOCKER_MODE));
    }
    return Optional.of(executor);
  }
}
