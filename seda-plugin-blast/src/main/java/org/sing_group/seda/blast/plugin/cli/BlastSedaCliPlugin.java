package org.sing_group.seda.blast.plugin.cli;

import org.sing_group.seda.blast.cli.blast.BlastCommand;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;

import es.uvigo.ei.sing.yacli.command.Command;

public class BlastSedaCliPlugin implements SedaCliPlugin {
  private final BlastCommand blastCommand;
  
  public BlastSedaCliPlugin() {
    this.blastCommand = new BlastCommand();
  }

  @Override
  public Command getCommand() {
    return this.blastCommand;
  }

}
