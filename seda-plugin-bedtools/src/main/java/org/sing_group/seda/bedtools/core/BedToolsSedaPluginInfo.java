/*
 * #%L
 * SEquence DAtaset builder bedtools plugin
 * %%
 * Copyright (C) 2017 - 2023 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.bedtools.core;

import org.sing_group.seda.plugin.core.info.AbstractInfo;
import org.sing_group.seda.util.SedaProperties;

public class BedToolsSedaPluginInfo extends AbstractInfo {
  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION_BEDTOOLS =
    SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION + ".bedtools";
  
  public static final String PARAM_DOCKER_MODE_HELP =
    "The bedtools docker image. By default, the official SEDA image for bedtools is used. " +
      "If you provide a custom image, it should have the bedtools command available in the path.";
  public static final String PARAM_DOCKER_MODE_HELP_GUI =
    toHtml(PARAM_DOCKER_MODE_HELP);
  
  public static final String PARAM_LOCAL_MODE_HELP = 
    "The bedtools binary file. If the bedtools binary is in the path, then this can be empty.";
  public static final String PARAM_LOCAL_MODE_HELP_GUI = toHtml(PARAM_LOCAL_MODE_HELP);
}