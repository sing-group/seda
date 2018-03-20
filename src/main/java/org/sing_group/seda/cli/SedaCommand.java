/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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

import java.util.ArrayList;
import java.util.List;

import es.uvigo.ei.sing.yaacli.command.AbstractCommand;
import es.uvigo.ei.sing.yaacli.command.option.Option;

public abstract class SedaCommand extends AbstractCommand {
  //TODO Define the options for file input and output
  
  @Override
  protected List<Option<?>> createOptions() {
    final List<Option<?>> options = new ArrayList<>();
    
    //TODO: Add this options to "options".
    
    options.addAll(this.createSedaOptions());
    
    return options;
  }
  
  protected abstract List<Option<?>> createSedaOptions();
}
