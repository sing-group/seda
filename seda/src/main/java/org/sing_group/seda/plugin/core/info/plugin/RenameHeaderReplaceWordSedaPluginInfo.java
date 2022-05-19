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

public class RenameHeaderReplaceWordSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Rename header (Replace Word)";
  public static final String SHORT_NAME = "rename-header-replace-word";
  public static final String DESCRIPTION = "Modify sequence headers by adding, deleting or replacing information.";
  public static final String PARAM_TARGET_WORDS_NAME = "target-word";
  public static final String PARAM_TARGET_WORDS_SHORT_NAME = "tw";
  public static final String PARAM_TARGET_WORDS_DESCRIPTION = "Targets";
  public static final String PARAM_TARGET_WORDS_HELP = "The target words.";
  public static final String PARAM_TARGET_WORDS_HELP_GUI = toHtml(PARAM_TARGET_WORDS_HELP);

  public static final String PARAM_REGEX_NAME = "regex";
  public static final String PARAM_REGEX_SHORT_NAME = "r";
  public static final String PARAM_REGEX_DESCRIPTION = "Regex";
  public static final String PARAM_REGEX_HELP = "Whether targets must be applied as regex or not.";
  public static final String PARAM_REGEX_HELP_GUI = toHtml(PARAM_REGEX_HELP);

  public static final String PARAM_REPLACEMENT_NAME = "replacement";
  public static final String PARAM_REPLACEMENT_SHORT_NAME = "rp";
  public static final String PARAM_REPLACEMENT_DESCRIPTION = "Replacement";
  public static final String PARAM_REPLACEMENT_HELP = "The replacement.";
  public static final String PARAM_REPLACEMENT_HELP_GUI = toHtml(PARAM_REPLACEMENT_HELP);
}
