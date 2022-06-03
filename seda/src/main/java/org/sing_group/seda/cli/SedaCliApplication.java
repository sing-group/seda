/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.cli;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.sing_group.seda.plugin.SedaPluginManager;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;
import org.sing_group.seda.plugin.spi.SedaPluginFactory;
import org.sing_group.seda.util.SedaApplicationInfo;

import es.uvigo.ei.sing.yacli.CLIApplication;
import es.uvigo.ei.sing.yacli.command.Command;
import es.uvigo.ei.sing.yacli.command.CommandPrinterConfiguration;

public class SedaCliApplication extends CLIApplication {

  @Override
  protected List<Command> buildCommands() {
    return new SedaPluginManager().getFactories()
      .flatMap(SedaPluginFactory::getCliPlugins)
      .map(SedaCliPlugin::getCommand)
      .collect(toList());
  }

  @Override
  protected String getApplicationName() {
    return SedaApplicationInfo.getName();
  }

  @Override
  protected String getApplicationCommand() {
    return "seda";
  }

  @Override
  protected String getApplicationVersion() {
    return SedaApplicationInfo.getVersion();
  }

  public static void main(String[] args) {
    new SedaCliApplication().run(args);
  }

  protected CommandPrinterConfiguration getCommandPrinterConfiguration() {
    return new CommandPrinterConfiguration(true, "This parameter can be specified multiple times.", "Command options:");
  };
}
