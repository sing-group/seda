/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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
package org.sing_group.seda.blast.plugin.cli;

import org.sing_group.seda.blast.cli.twowayblast.TwoWayBlastCommand;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;

import es.uvigo.ei.sing.yacli.command.Command;

public class TwoWayBlastSedaCliPlugin implements SedaCliPlugin {
  private final TwoWayBlastCommand twoWayBlastCommand;

  public TwoWayBlastSedaCliPlugin() {
    this.twoWayBlastCommand = new TwoWayBlastCommand();
  }

  @Override
  public Command getCommand() {
    return this.twoWayBlastCommand;
  }

}
