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

public class RegexSplitSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Regular Expression Split";
  public static final String SHORT_NAME = "split-regex";
  public static final String DESCRIPTION =
    "Split each input FASTA file based on regular expression patterns.";
  public static final String GROUP = Group.GROUP_GENERAL.getName();

  public static final String PARAM_FILES_DIRECTORY_NAME = "files-directory";
  public static final String PARAM_FILES_DIRECTORY_SHORT_NAME = "fd";
  public static final String PARAM_FILES_DIRECTORY_DESCRIPTION = "Group names files directory";
  public static final String PARAM_GROUP_NAMES_FILES_DIRECTORY_HELP =
    "Whether the groups created for each file should be saved into a TXT file or not. "
      + "This allows an easy identification of the sequence groups that have been created. "
      + "If you do not want to save them, leave this file empty. Otherwise, choose the directory where such files should be created.";
  public static final String PARAM_GROUP_NAMES_FILES_DIRECTORY_HELP_GUI =
    toHtml(PARAM_GROUP_NAMES_FILES_DIRECTORY_HELP);
}
