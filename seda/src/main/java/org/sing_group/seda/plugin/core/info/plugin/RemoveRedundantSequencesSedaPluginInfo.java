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

  public static final String PARAM_REMOVE_SUBSEQUENCE_NAME = "remove-subsequences";
  public static final String PARAM_REMOVE_SUBSEQUENCE_SHORT_NAME = "rs";
  public static final String PARAM_REMOVE_SUBSEQUENCE_DESCRIPTION = "Remove also subsequences";
  public static final String PARAM_REMOVE_SUBSEQUENCE_HELP = "Remove also sequences contained within larger sequences.";
  public static final String PARAM_REMOVE_SUBSEQUENCE_HELP_GUI = toHtml(PARAM_REMOVE_SUBSEQUENCE_HELP);

  public static final String PARAM_MERGE_HEADERS_NAME = "merge-headers";
  public static final String PARAM_MERGE_HEADERS_SHORT_NAME = "mh";
  public static final String PARAM_MERGE_HEADERS_DESCRIPTION = "Merge headers";
  public static final String PARAM_MERGE_HEADERS_HELP = "Use this option to specify that headers of new sequences must be created by concatenating the headers of the "
    + "two sequences being merged. By default, if not specified, the headers of the new sequences are the header of one of the two being merged.";
  public static final String PARAM_MERGE_HEADERS_HELP_GUI = toHtml(PARAM_MERGE_HEADERS_HELP);

  public static final String PARAM_SAVE_MERGED_HEADERS_NAME = "save-merged-headers";
  public static final String PARAM_SAVE_MERGED_HEADERS_SHORT_NAME = "smh";
  public static final String PARAM_SAVE_MERGED_HEADERS_DESCRIPTION = "Merged headers reports directory";
  public static final String PARAM_SAVE_MERGED_HEADERS_HELP  =
    "Whether report files of the merged headers must be created or not. "
      + "If you do not want to save them, leave this file empty. "
      + "Otherwise, choose the directory where such files should be created.";
  public static final String PARAM_SAVE_MERGED_HEADERS_HELP_GUI = toHtml(PARAM_SAVE_MERGED_HEADERS_HELP);
  

  public static final String PARAM_CONVERT_AMINO_ACID_DESCRIPTION = "Convert to amino acid sequences before sequence comparison";
  public static final String PARAM_CONVERT_AMINO_ACID_HELP = "If this option is selected, then input nucleic acid sequences are translated into amino acid "
    + "sequences before applying the sequence comparison. In this case, note that the input nucleic acid sequences are reported.";
  public static final String PARAM_CONVERT_AMINO_ACID_HELP_GUI = toHtml(PARAM_CONVERT_AMINO_ACID_HELP);
}
