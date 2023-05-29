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
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.*;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.DEFAULT_QUERY_MODE;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.GROUP;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_DATABASE_DIRECTORY_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_DATABASE_DIRECTORY_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_DATABASE_DIRECTORY_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_NUM_THREADS_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_NUM_THREADS_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_NUM_THREADS_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_QUERY_MODE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_QUERY_MODE_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_QUERY_MODE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_STORE_DATABASE_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_STORE_DATABASE_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_STORE_DATABASE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sing_group.seda.blast.datatype.TwoWayBlastMode;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DefaultBlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DockerBlastBinariesExecutor;
import org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationProvider;
import org.sing_group.seda.blast.transformation.provider.blast.BlastTransformationProvider;
import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.BigDecimalDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.DefaultValueBooleanOption;
import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class TwoWayBlastCommand extends ExternalSoftwareExecutionCommand {

  public static final StringOption OPTION_DOCKER_MODE =
    new StringOption(
      PARAM_DOCKER_MODE_NAME, PARAM_DOCKER_MODE_SHORT_NAME,
      PARAM_DOCKER_MODE_HELP, true, true
    );

  public static final StringOption OPTION_LOCAL_MODE =
    new StringOption(
      PARAM_LOCAL_MODE_NAME, PARAM_LOCAL_MODE_SHORT_NAME,
      PARAM_LOCAL_MODE_HELP, true, true
    );

  public static final DefaultValueBooleanOption OPTION_STORE_DATABASE =
    new DefaultValueBooleanOption(
      PARAM_STORE_DATABASE_NAME, PARAM_STORE_DATABASE_SHORT_NAME, PARAM_STORE_DATABASE_DESCRIPTION, false
    );

  public static final FileOption OPTION_DATABASE_DIRECTORY =
    new FileOption(
      PARAM_DATABASE_DIRECTORY_NAME, PARAM_DATABASE_DIRECTORY_SHORT_NAME, PARAM_DATABASE_DIRECTORY_DESCRIPTION, true,
      true
    );

  public static final DefaultValuedStringOption OPTION_QUERY_MODE =
    new DefaultValuedStringOption(
      PARAM_QUERY_MODE_NAME, PARAM_QUERY_MODE_SHORT_NAME, PARAM_QUERY_MODE_DESCRIPTION, DEFAULT_QUERY_MODE.name()
    );

  public static final DefaultValuedStringOption OPTION_QUERY_BLAST_TYPE =
    new DefaultValuedStringOption(
      PARAM_QUERY_BLAST_TYPE_NAME, PARAM_QUERY_BLAST_TYPE_SHORT_NAME, PARAM_QUERY_BLAST_TYPE_HELP,
      DEFAULT_BLAST_TYPE.name().toLowerCase()
    );

  public static final FileOption OPTION_QUERY_FILE =
    new FileOption(
      PARAM_QUERY_FILE_NAME, PARAM_QUERY_FILE_SHORT_NAME, PARAM_QUERY_SOURCE_DESCRIPTION, false,
      true
    );

  public static final BigDecimalDefaultValuedStringConstructedOption OPTION_EXPECTATION_VALUE =
    new BigDecimalDefaultValuedStringConstructedOption(
      PARAM_EVALUE_NAME, PARAM_EVALUE_SHORT_NAME, PARAM_EVALUE_DESCRIPTION, BigDecimal.valueOf(0.05)
    );

  public static final StringOption OPTION_ADDITIONAL_PARAMS =
    new StringOption(
      PARAM_ADDITIONAL_PARAMS_NAME, PARAM_ADDITIONAL_PARAMS_SHORT_NAME, PARAM_ADDITIONAL_PARAMS_DESCRIPTION, true, true,
      true
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_NUM_THREADS =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_NUM_THREADS_NAME, PARAM_NUM_THREADS_SHORT_NAME, PARAM_NUM_THREADS_DESCRIPTION, 1
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
      OPTION_STORE_DATABASE,
      OPTION_DATABASE_DIRECTORY,
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
    provider.setQueryFile(this.getQueryFile(parameters));
    provider.setQueryMode(this.getQueryMode(parameters));

    Optional<File> databasesDirectory = this.getDatabasesDirectory(parameters);
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
    return new JsonObjectReader<BlastTransformationProvider>()
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

  private File getQueryFile(Parameters parameters) {
    File queryFile = parameters.getSingleValue(OPTION_QUERY_FILE);

    if (!queryFile.exists()) {
      formattedValidationError("Invalid path. The path to the query file must be valid and exist.");
    } else {
      return queryFile;
    }
    throw new IllegalStateException();
  }

  private Optional<File> getDatabasesDirectory(Parameters parameters) {
    if (!parameters.hasOption(OPTION_STORE_DATABASE)) {
      return Optional.empty();
    }

    File databasesDirectory = parameters.getSingleValue(OPTION_DATABASE_DIRECTORY);
    if (!databasesDirectory.exists() || !databasesDirectory.isDirectory()) {
      formattedValidationError("Invalid path. The path to the databases directory must be valid and exist.");
    } else {
      return Optional.of(databasesDirectory);
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
