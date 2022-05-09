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
package org.sing_group.seda.plugin.core;

public class BasePresenceSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Base presence filtering";
  public static final String SHORT_NAME = "base-filtering";
  public static final String DESCRIPTION =
    "Filter sequences based on the percentages of their bases (nucleotides or amino acids).";

  public static final String PARAM_BASE_FILTER_NAME = "base-filter";
  public static final String PARAM_BASE_FILTER_SHORT_NAME = "bf";
  public static final String PARAM_BASE_FILTER_DESCRIPTION = "Base(s)";
  public static final String PARAM_BASE_FILTER_HELP =
    "The base whose percentage must be between the specified limits.\n\t\t" +
      "If multiple bases are specified, the sum of each base percentage is used.";

  public static final String PARAM_BASE_FILTER_HELP_WITH_CONFIG =
    "The base whose percentage must be between the specified limits.\n\t\t"
      + "If multiple bases are specified, the sum of each base percentage is used. Accept Config.\n"
      + "\t\tConfig structure: config(min_presence/max_presence):<base> where:\n"
      + "\t\t\t- min_presence: <Number with decimals between 0.0 and 1> Minimum % of the base\n"
      + "\t\t\t- max_presence: <Number with decimals between 0.0 and 1> Maximum % of the base\n"
      + "\t\t\t- base: <String> Base to be filtered \n"
      + "\t\t\tExample: --base-filtering config(0.35/0.4):TG";
  public static final String PARAM_BASE_FILTER_HELP_GUI = toHtml(PARAM_BASE_FILTER_HELP);
}
