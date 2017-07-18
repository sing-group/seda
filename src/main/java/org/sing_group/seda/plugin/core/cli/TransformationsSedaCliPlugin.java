package org.sing_group.seda.plugin.core.cli;

import org.sing_group.seda.cli.command.GenerateCommand;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;

import es.uvigo.ei.sing.yaacli.command.Command;

public class TransformationsSedaCliPlugin implements SedaCliPlugin {
  private final GenerateCommand command;
  
  public TransformationsSedaCliPlugin() {
    this.command = new GenerateCommand();
  }
  
  @Override
  public Command getCommand() {
    return this.command;
  }
}
