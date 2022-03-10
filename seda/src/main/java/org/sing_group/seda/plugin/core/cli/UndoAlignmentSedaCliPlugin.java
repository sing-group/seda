package org.sing_group.seda.plugin.core.cli;

import org.sing_group.seda.cli.command.UndoAlignmentCommand;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;

import es.uvigo.ei.sing.yacli.command.Command;

public class UndoAlignmentSedaCliPlugin implements SedaCliPlugin {
  private final UndoAlignmentCommand command;

  public UndoAlignmentSedaCliPlugin() {
    this.command = new UndoAlignmentCommand();
  }

  @Override
  public Command getCommand() {
    return this.command;
  }
}
