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

public class RemoveRedundantSequencesSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Remove redundant sequences";
  public static final String SHORT_NAME = "remove-redundant";
  public static final String DESCRIPTION =
    "If several identical sequences (or subsequences, if chosen) are found, keep only one (user may choose to merge headers).";
  public static final String GROUP = Group.GROUP_FILTERING.getName();

  public static final String PARAM_REMOVE_SUBSEQUENCE_NAME = "remove-subsequence";
  public static final String PARAM_REMOVE_SUBSEQUENCE_SHORT_NAME = "rs";
  public static final String PARAM_REMOVE_SUBSEQUENCE_DESCRIPTION = "Remove also subsequences";
  public static final String PARAM_REMOVE_SUBSEQUENCE_HELP = "";
  public static final String PARAM_REMOVE_SUBSEQUENCE_HELP_GUI = toHtml(PARAM_REMOVE_SUBSEQUENCE_HELP);

  public static final String PARAM_MERGE_HEADERS_NAME = "merge-headers";
  public static final String PARAM_MERGE_HEADERS_SHORT_NAME = "mh";
  public static final String PARAM_MERGE_HEADERS_DESCRIPTION = "Merge headers";
  public static final String PARAM_MERGE_HEADERS_HELP = "";
  public static final String PARAM_MERGE_HEADERS_HELP_GUI = toHtml(PARAM_MERGE_HEADERS_HELP);

  public static final String PARAM_SAVE_MERGED_HEADERS_NAME = "save-merged-headers";
  public static final String PARAM_SAVE_MERGED_HEADERS_SHORT_NAME = "smh";
  public static final String PARAM_SAVE_MERGED_HEADERS_DESCRIPTION = "Save merged headers into a file";
  public static final String PARAM_SAVE_MERGED_HEADERS_HELP = "";
  public static final String PARAM_SAVE_MERGED_HEADERS_HELP_GUI = toHtml(PARAM_SAVE_MERGED_HEADERS_HELP);
}
