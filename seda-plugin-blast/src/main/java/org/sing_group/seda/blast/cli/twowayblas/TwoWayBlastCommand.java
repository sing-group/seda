package org.sing_group.seda.blast.cli.twowayblas;

import static java.util.Arrays.asList;
import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.*;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.DEFAULT_QUERY_MODE;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.GROUP;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_DATABASE_DIRECTORY_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_DATABASE_DIRECTORY_NAME;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.PARAM_DATABASE_DIRECTORY_SHORT_NAME;
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

import org.sing_group.seda.blast.gui.twowayblast.TwoWayBlastTransformationProvider;
import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.BigDecimalDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.DefaultValueBooleanOption;
import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
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
      OPTION_STORE_DATABASE,
      OPTION_DATABASE_DIRECTORY,
      OPTION_QUERY_MODE,
      OPTION_QUERY_BLAST_TYPE,
      OPTION_QUERY_FILE,
      OPTION_EXPECTATION_VALUE,
      OPTION_ADDITIONAL_PARAMS
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    TwoWayBlastTransformationProvider provider = new TwoWayBlastTransformationProvider();
    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return null;
  }
}
