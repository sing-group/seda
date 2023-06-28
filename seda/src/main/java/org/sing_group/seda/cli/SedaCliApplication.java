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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
  
  @Override
  protected String getDescription() {
    return "seda is a CLI application for running all SEDA (SEquence DAtaset builder) operations for processing FASTA files "
      + "containing DNA and protein sequences. The SEDA CLI has over 30 commands, which are listed below along with their "
      + "usage and arguments.";
  }

  public static void main(String[] args) {
    new SedaCliApplication().run(args);
  }

  protected CommandPrinterConfiguration getCommandPrinterConfiguration() {
    return new CommandPrinterConfiguration(
      true, " This parameter can be specified multiple times.", "Command options:", 120
    );
  };

  @Override
  protected void printCommandsInHelp(PrintStream out, List<Command> commands) {
    out.println();
    Map<String, List<Command>> commandsMap = new HashMap<String, List<Command>>();
    for (Command command : commands) {
      if (command instanceof SedaCommand) {
        String group = ((SedaCommand) command).getSedaGroup();
        commandsMap.putIfAbsent(group, new ArrayList<Command>());
        commandsMap.get(group).add(command);
      }
    }

    List<String> groups = new LinkedList<>(commandsMap.keySet());
    Collections.sort(groups, new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
      }
    });

    for (String groupName : groups) {
      out.println(groupName + " commands:");
      List<Command> operations = commandsMap.get(groupName);
      Collections.sort(operations, new Comparator<Command>() {

        @Override
        public int compare(Command o1, Command o2) {
          return o1.getName().compareToIgnoreCase(o2.getName());
        }
      });
      for (Command command : operations) {
        printCommandInHelp(out, command);
      }
      out.println();
    }
  }
}
