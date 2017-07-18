package org.sing_group.seda.plugin.spi;

import es.uvigo.ei.sing.yaacli.command.Command;

public interface SedaCliPlugin {
  public Command getCommand();
}
