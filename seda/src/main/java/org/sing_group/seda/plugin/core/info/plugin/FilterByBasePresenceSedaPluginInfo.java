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

public class FilterByBasePresenceSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Base presence filtering";
  public static final String SHORT_NAME = "base-filtering";
  public static final String DESCRIPTION = "Filter sequences based on the percentages of their bases (nucleotides or amino acids).";
  public static final String GROUP = Group.GROUP_FILTERING.getName();

  public static final String PARAM_BASE_FILTER_NAME = "base-filter";
  public static final String PARAM_BASE_FILTER_SHORT_NAME = "bf";
  public static final String PARAM_BASE_FILTER_DESCRIPTION = "Base(s)";
  public static final String PARAM_BASE_FILTER_HELP = "The base(s) whose percentage must be between the specified limits. "
    + "If multiple bases are specified, the sum of each single base percentage is used.";
  public static final String PARAM_BASE_FILTER_HELP_GUI = toHtml(PARAM_BASE_FILTER_HELP);

  public static final String PARAM_BASE_FILTER_HELP_WITH_CONFIG = PARAM_BASE_FILTER_HELP
    + "\n\t\tBy default, each base(s) filter uses a minimum of 0 and a maximum of 1. "
    + "These limits can be specified adding 'config(min_presence/max_presence)' before the base(s), where:\n"
    + "\t\t\t- min_presence (<Number with decimals between 0 and 1>): Minimum % required for the base(s).\n"
    + "\t\t\t- max_presence (<Number with decimals between 0 and 1>): Maximum % allowed for the base(s).\n"
    + "\t\tExample: --base-filtering config(0.35/0.4):TG\n\t\t";
}
