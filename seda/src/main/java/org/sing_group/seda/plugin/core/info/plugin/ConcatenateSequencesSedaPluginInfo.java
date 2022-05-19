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
import org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo;

public class ConcatenateSequencesSedaPluginInfo extends RegexHeaderMatcherInfo {
  public static final String NAME = "Concatenate sequences";
  public static final String SHORT_NAME = "concatenate";
  public static final String DESCRIPTION =
    "Concatenate sequences from selected files, according to FASTA header information, into a single output FASTA.";
  public static final String GROUP = Group.GROUP_ALIGNMENT.getName();

  public static final String PARAM_NAME_NAME = "name";
  public static final String PARAM_NAME_SHORT_NAME = "n";
  public static final String PARAM_NAME_DESCRIPTION = "Name";
  public static final String PARAM_NAME_HELP = "The name of the merged file.";
  public static final String PARAM_NAME_HELP_GUI = toHtml(PARAM_NAME_HELP);

  public static final String PARAM_MERGE_NAME = "merge";
  public static final String PARAM_MERGE_SHORT_NAME = "m";
  public static final String PARAM_MERGE_DESCRIPTION = "Merge descriptions";
  public static final String PARAM_MERGE_HELP =
    "Whether the sequence descriptions must be added to the concatenated sequences or not.";
  public static final String PARAM_MERGE_HELP_GUI = toHtml(PARAM_MERGE_HELP);
}
