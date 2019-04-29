/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.plugin.core.gui;

import java.util.Optional;

import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;

public abstract class AbstractSedaGuiPlugin implements SedaGuiPlugin {

  private SedaContext context;

  @Override
  public void setSedaContext(SedaContext context) {
    this.context = context;
  }

  protected SedaContext getSedaContext() {
    return context;
  }

  @Override
  public Optional<String> getProcessDatasetButtonTooltipMessage() {
    return Optional.empty();
  }
}
