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
package org.sing_group.seda.clustalomega.plugin;

import java.util.stream.Stream;

import org.sing_group.seda.clustalomega.plugin.gui.ClustalAlignmentSedaGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaPluginFactory;

public class ClustalOmegaSedaPluginFactory implements SedaPluginFactory {

  @Override
  public Stream<SedaGuiPlugin> getGuiPlugins() {
    return Stream.of(new ClustalAlignmentSedaGuiPlugin());
  }

  @Override
  public Stream<SedaCliPlugin> getCliPlugins() {
    return null;
  }
}
