package org.sing_group.seda.emboss.plugin.cli;

import org.sing_group.seda.emboss.getorf.cli.GetOrfCommand;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;

import es.uvigo.ei.sing.yacli.command.Command;

public class EmbossGetOrfSedaCliPlugin implements SedaCliPlugin {
  private final GetOrfCommand command;

  public EmbossGetOrfSedaCliPlugin() {
    this.command = new GetOrfCommand();
  }

  @Override
  public Command getCommand() {
    return this.command;
  }
}
