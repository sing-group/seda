package org.sing_group.seda.blast.cli.twowayblas;

import static org.sing_group.seda.blast.plugin.core.BlastSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST;
import static org.sing_group.seda.blast.plugin.core.TwoBlastSedaPluginInfo.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class TwoWayBlastCommand extends ExternalSoftwareExecutionCommand {

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
    return null;
  }

  @Override
  protected List<Option<?>> createExternalSedaOptions() {
    return null;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    return null;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return null;
  }
}
