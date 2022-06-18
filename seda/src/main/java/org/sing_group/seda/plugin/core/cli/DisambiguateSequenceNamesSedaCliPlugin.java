/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.plugin.core.cli;

import org.sing_group.seda.cli.command.DisambiguateSequenceNamesCommand;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;

import es.uvigo.ei.sing.yacli.command.Command;

public class DisambiguateSequenceNamesSedaCliPlugin implements SedaCliPlugin {
  private final DisambiguateSequenceNamesCommand command;

  public DisambiguateSequenceNamesSedaCliPlugin() {
    this.command = new DisambiguateSequenceNamesCommand();
  }

  @Override
  public Command getCommand() {
    return this.command;
  }

}
