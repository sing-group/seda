package org.sing_group.seda.blast.cli.blast;

import static java.util.Arrays.asList;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.DEFAULT_BLAST_TYPE;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.DEFAULT_DATABAE_QUERY_MODE;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.DEFAULT_EVALUE;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.DEFAULT_MAX_TARGET_SEQS;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.GROUP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_DATABASE_QUERY_MODE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_DATABASE_QUERY_MODE_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_DATABASE_QUERY_MODE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_DOCKER_MODE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EVALUE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EVALUE_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_EVALUE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_HIT_REGION_WINDOW_SIZE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_HIT_REGION_WINDOW_SIZE_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_HIT_REGION_WINDOW_SIZE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_LOCAL_MODE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_MAX_TARGET_SEQS_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_MAX_TARGET_SEQS_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_MAX_TARGET_SEQS_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_BLAST_TYPE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_BLAST_TYPE_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_BLAST_TYPE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_FILE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_FILE_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_QUERY_FILE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_ALIAS_FILE_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_ALIAS_FILE_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_ALIAS_FILE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_DIRECTORY_HELP;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_DIRECTORY_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PARAM_STORE_DATABASES_DIRECTORY_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.sing_group.seda.blast.datatype.DatabaseQueryMode;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DefaultBlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DockerBlastBinariesExecutor;
import org.sing_group.seda.blast.transformation.provider.blast.BlastTransformationProvider;
import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class BlastCommand extends ExternalSoftwareExecutionCommand {

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
  
  public static final FileOption OPTION_QUERY_FILE =
    new FileOption(
      PARAM_QUERY_FILE_NAME, PARAM_QUERY_FILE_SHORT_NAME, PARAM_QUERY_FILE_HELP, true, true
    );

  public static final DefaultValuedStringOption OPTION_DATABASE_QUERY_MODE =
    new DefaultValuedStringOption(
      PARAM_DATABASE_QUERY_MODE_NAME, PARAM_DATABASE_QUERY_MODE_SHORT_NAME, PARAM_DATABASE_QUERY_MODE_HELP,
      DEFAULT_DATABAE_QUERY_MODE.name().toLowerCase()
    );
  
  public static final DefaultValuedStringOption OPTION_QUERY_BLAST_TYPE_MODE =
    new DefaultValuedStringOption(
      PARAM_QUERY_BLAST_TYPE_NAME, PARAM_QUERY_BLAST_TYPE_SHORT_NAME, PARAM_QUERY_BLAST_TYPE_HELP,
      DEFAULT_BLAST_TYPE.name().toLowerCase()
    );
  
  public static final FileOption OPTION_STORE_DATABASES_DIRECTORY =
    new FileOption(
      PARAM_STORE_DATABASES_DIRECTORY_NAME, PARAM_STORE_DATABASES_DIRECTORY_SHORT_NAME,
      PARAM_STORE_DATABASES_DIRECTORY_HELP, true, true
    );

  public static final FileOption OPTION_STORE_ALIAS_FILE =
    new FileOption(
      PARAM_STORE_ALIAS_FILE_NAME, PARAM_STORE_ALIAS_FILE_SHORT_NAME,
      PARAM_STORE_ALIAS_FILE_HELP, true, true
    );
  
  public static final DefaultValuedStringOption OPTION_EVALUE =
    new DefaultValuedStringOption(
      PARAM_EVALUE_NAME, PARAM_EVALUE_SHORT_NAME, PARAM_EVALUE_HELP,
      Double.toString(DEFAULT_EVALUE)
    );

  public static final DefaultValuedStringOption OPTION_MAX_TARGET_SEQS =
    new DefaultValuedStringOption(
      PARAM_MAX_TARGET_SEQS_NAME, PARAM_MAX_TARGET_SEQS_SHORT_NAME, PARAM_MAX_TARGET_SEQS_HELP,
      Integer.toString(DEFAULT_MAX_TARGET_SEQS)
    );

  public static final StringOption OPTION_HIT_REGION_WINDOW_SIZE =
    new StringOption(
      PARAM_HIT_REGION_WINDOW_SIZE_NAME, PARAM_HIT_REGION_WINDOW_SIZE_SHORT_NAME, PARAM_HIT_REGION_WINDOW_SIZE_HELP,
      true, true
    );
  
  public static final StringOption OPTION_ADDITIONAL_PARAMS =
    new StringOption(
      PARAM_ADDITIONAL_PARAMS_NAME, PARAM_ADDITIONAL_PARAMS_SHORT_NAME, PARAM_ADDITIONAL_PARAMS_HELP,
      true, true
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
  protected String getPropertyEnableLocalExecution() {
    return PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST;
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
      OPTION_QUERY_FILE,
      OPTION_DATABASE_QUERY_MODE,
      OPTION_QUERY_BLAST_TYPE_MODE,
      OPTION_STORE_DATABASES_DIRECTORY,
      OPTION_STORE_ALIAS_FILE,
      OPTION_EVALUE,
      OPTION_MAX_TARGET_SEQS,
      OPTION_HIT_REGION_WINDOW_SIZE,
      OPTION_ADDITIONAL_PARAMS
    );
  }
  
  @Override
  protected List<Option<?>> getMandatoryOptions() {
    return Arrays.asList(OPTION_QUERY_FILE);
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    validateExecutionMode(parameters);
    
    BlastTransformationProvider provider = new BlastTransformationProvider();
    
    provider.setBlastType(getBlastType(parameters));
    provider.setQueryFile(getQueryFile(parameters));
    provider.setDatabaseQueryMode(getDatabaseQueryMode(parameters));
    provider.setEvalue(getEvalue(parameters));
    provider.setMaxTargetSeqs(getMaxTargetSeqs(parameters));
    
    Optional<File> databasesDirectory = getDatabasesDirectory(parameters);
    if (databasesDirectory.isPresent()) {
      provider.setStoreDatabases(true);
      provider.setDatabasesDirectory(databasesDirectory.get());
    }

    Optional<File> aliasFile = getAliasFile(parameters);
    if (aliasFile.isPresent()) {
      provider.setStoreAlias(true);
      provider.setAliasFile(aliasFile.get());
    }

    Optional<Integer> hitRegionWindowSize = getHitRegionWindowSize(parameters);
    if (hitRegionWindowSize.isPresent()) {
      provider.setExtractOnlyHitRegions(true);
      provider.setHitRegionsWindowSize(hitRegionWindowSize.get());
    }

    Optional<String> additionalParameters = getAdditionalParameters(parameters);
    if (additionalParameters.isPresent()) {
      provider.setAdditionalParameters(additionalParameters.get());
    }

    BlastBinariesExecutor executor =
      new DockerBlastBinariesExecutor(DockerBlastBinariesExecutor.getDefaultDockerImage());

    if (parameters.hasOption(OPTION_LOCAL_MODE)) {

      File blastBinariesDirectory = new File(parameters.getSingleValueString(OPTION_LOCAL_MODE));

      if (blastBinariesDirectory.isDirectory()) {
        executor = new DefaultBlastBinariesExecutor(blastBinariesDirectory);
      } else {
        formattedValidationError("The specified BLAST directory does not exist or it is not a directory.");
      }
    }

    if (parameters.hasOption(OPTION_DOCKER_MODE)) {
      executor = new DockerBlastBinariesExecutor(parameters.getSingleValue(OPTION_DOCKER_MODE));
    }

    provider.setBlastBinariesExecutor(Optional.of(executor));

    return provider;
  }

  private void validateExecutionMode(Parameters parameters) {
    if (parameters.hasOption(OPTION_DOCKER_MODE) && parameters.hasOption(OPTION_LOCAL_MODE)) {
      formattedValidationError("Only one execution mode can be specified");
    }
  }

  private Optional<String> getAdditionalParameters(Parameters parameters) {
    if (!parameters.hasOption(OPTION_ADDITIONAL_PARAMS)) {
      return Optional.empty();
    } else {
      return Optional.of(parameters.getSingleValue(OPTION_ADDITIONAL_PARAMS));
    }
  }

  private Optional<Integer> getHitRegionWindowSize(Parameters parameters) {
    if (!parameters.hasOption(OPTION_HIT_REGION_WINDOW_SIZE)) {
      return Optional.empty();
    }

    try {
      return Optional.of(Integer.valueOf(parameters.getSingleValue(OPTION_HIT_REGION_WINDOW_SIZE)));
    } catch (NumberFormatException ex) {
      formattedValidationError(
        "Invalid value for " + formatParam(OPTION_HIT_REGION_WINDOW_SIZE) + " ("
          + parameters.getSingleValue(OPTION_HIT_REGION_WINDOW_SIZE) + "). It must be a number."
      );
    }
    throw new IllegalStateException();
  }

  private int getMaxTargetSeqs(Parameters parameters) {
    try {
      return Integer.valueOf(parameters.getSingleValue(OPTION_MAX_TARGET_SEQS));
    } catch (NumberFormatException ex) {
      formattedValidationError(
        "Invalid value for " + formatParam(OPTION_MAX_TARGET_SEQS) + " ("
          + parameters.getSingleValue(OPTION_MAX_TARGET_SEQS) + "). It must be a number."
      );
    }
    throw new IllegalStateException();
  }

  private double getEvalue(Parameters parameters) {
    try {
      return Double.valueOf(parameters.getSingleValue(OPTION_EVALUE));
    } catch (NumberFormatException ex) {
      formattedValidationError(
        "Invalid value for " + formatParam(OPTION_EVALUE) + " (" + parameters.getSingleValue(OPTION_EVALUE)
          + "). It must be a number."
      );
    }
    throw new IllegalStateException();
  }

  private Optional<File> getAliasFile(Parameters parameters) {
    if (!parameters.hasOption(OPTION_STORE_ALIAS_FILE)) {
      return Optional.empty();
    }

    File aliasFile = parameters.getSingleValue(OPTION_STORE_ALIAS_FILE);
    if (!aliasFile.getParentFile().canWrite()) {
      formattedValidationError("Invalid path. The path to the alias file must be writable.");
    } else {
      return Optional.of(aliasFile);
    }
    throw new IllegalStateException();
  }

  private Optional<File> getDatabasesDirectory(Parameters parameters) {
    if (!parameters.hasOption(OPTION_STORE_DATABASES_DIRECTORY)) {
      return Optional.empty();
    }

    File databasesDirectory = parameters.getSingleValue(OPTION_STORE_DATABASES_DIRECTORY);
    if (!databasesDirectory.exists() || !databasesDirectory.isDirectory()) {
      formattedValidationError("Invalid path. The path to the databases directory must be valid and exist.");
    } else {
      return Optional.of(databasesDirectory);
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

  private DatabaseQueryMode getDatabaseQueryMode(Parameters parameters) {
    try {
      return DatabaseQueryMode.valueOf(parameters.getSingleValueString(OPTION_DATABASE_QUERY_MODE).toUpperCase());
    } catch (IllegalArgumentException exc) {
      invalidEnumValue(OPTION_DATABASE_QUERY_MODE);
    }
    throw new IllegalStateException();
  }

  private BlastType getBlastType(Parameters parameters) {
    try {
      return BlastType.valueOf(parameters.getSingleValueString(OPTION_QUERY_BLAST_TYPE_MODE).toUpperCase());
    } catch (IllegalArgumentException exc) {
      invalidEnumValue(OPTION_DATABASE_QUERY_MODE);
    }
    throw new IllegalStateException();
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<BlastTransformationProvider>()
      .read(parametersFile, BlastTransformationProvider.class);
  }
}
