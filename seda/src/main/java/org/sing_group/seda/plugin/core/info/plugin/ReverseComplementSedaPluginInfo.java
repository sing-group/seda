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

public class ReverseComplementSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Reverse Complement";
  public static final String SHORT_NAME = "reverse-complement";
  public static final String DESCRIPTION =
    "Convert the sequences into the reverse, complement, or reverse complement counterparts.";
  public static final String GROUP = Group.GROUP_GENERAL.getName();

  public static final String PARAM_REVERSE_NAME = "reverse";
  public static final String PARAM_REVERSE_SHORT_NAME = "r";
  public static final String PARAM_REVERSE_DESCRIPTION = "Reverse";
  public static final String PARAM_REVERSE_HELP = "Reverse sequences.";
  public static final String PARAM_REVERSE_HELP_GUI = toHtml(PARAM_REVERSE_HELP);

  public static final String PARAM_COMPLEMENT_NAME = "complement";
  public static final String PARAM_COMPLEMENT_SHORT_NAME = "c";
  public static final String PARAM_COMPLEMENT_DESCRIPTION = "Complement";
  public static final String PARAM_COMPLEMENT_HELP = "Complement sequences.";
  public static final String PARAM_COMPLEMENT_HELP_GUI = toHtml(PARAM_COMPLEMENT_HELP);

  public static final String PARAM_RENAME_SEQUENCE_HEADERS_NAME = "rename-sequence-headers";
  public static final String PARAM_RENAME_SEQUENCE_HEADERS_SHORT_NAME = "rsh";
  public static final String PARAM_RENAME_SEQUENCE_HEADERS_DESCRIPTION = "Rename sequence headers";
  public static final String PARAM_RENAME_SEQUENCE_HEADERS_HELP =
    "Use this option to rename sequence headers using the specified configuration to add a prefix/suffix.";
  public static final String PARAM_RENAME_SEQUENCE_HEADERS_HELP_GUI =
    toHtml("Check this option to rename sequence headers using the configuration below.");
}
