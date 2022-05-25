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

import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class RenameHeaderReplaceIntervalSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Rename header (Replace interval)";
  public static final String SHORT_NAME = "rename-header-replace-interval";
  public static final String DESCRIPTION = "Modify sequence headers by replacing information (target interval).";
  public static final String PARAM_FROM_NAME = "from";
  public static final String PARAM_FROM_SHORT_NAME = "fr";
  public static final String PARAM_FROM_DESCRIPTION = "From";
  public static final String PARAM_FROM_HELP = "The starting string of the interval.";
  public static final String PARAM_FROM_HELP_GUI = toHtml(PARAM_FROM_HELP);

  public static final String PARAM_TO_DELIMITER_NAME = "to";
  public static final String PARAM_TO_DELIMITER_SHORT_NAME = "to";
  public static final String PARAM_TO_DELIMITER_DESCRIPTION = "Delimiter";
  public static final String PARAM_TO_DELIMITER_HELP = "The ending string of the interval.";
  public static final String PARAM_TO_DELIMITER_HELP_GUI = toHtml(PARAM_TO_DELIMITER_HELP);

  public static final String PARAM_INTERVAL_REPLACEMENT_NAME = "interval-replacement";
  public static final String PARAM_INTERVAL_REPLACEMENT_SHORT_NAME = "ir";
  public static final String PARAM_INTERVAL_REPLACEMENT_DESCRIPTION = "Replacement";
  public static final String PARAM_INTERVAL_REPLACEMENT_HELP = "The interval replacement.";
  public static final String PARAM_INTERVAL_REPLACEMENT_HELP_GUI = toHtml(PARAM_INTERVAL_REPLACEMENT_HELP);
}
