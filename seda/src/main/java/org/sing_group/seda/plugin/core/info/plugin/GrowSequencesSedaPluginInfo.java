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
package org.sing_group.seda.plugin.core.info.plugin;

import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class GrowSequencesSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Grow sequences";
  public static final String SHORT_NAME = "grow";
  public static final String DESCRIPTION =
    "Grow sequences by merging those that show an overlap longer than that specified.";
  public static final String GROUP = Group.GROUP_GENERAL.getName();

  public static final String PARAM_MINIMUM_OVERLAPPING_NAME = "minimum-overlapping";
  public static final String PARAM_MINIMUM_OVERLAPPING_SHORT_NAME = "mo";
  public static final String PARAM_MINIMUM_OVERLAPPING_DESCRIPTION = "Minimum overlapping";
  public static final String PARAM_MINIMUM_OVERLAPPING_HELP = "The minimum overlapping to merge two sequences.";
  public static final String PARAM_MINIMUM_OVERLAPPING_HELP_GUI = toHtml(PARAM_MINIMUM_OVERLAPPING_HELP);
}