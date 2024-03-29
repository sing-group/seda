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

import org.sing_group.seda.datatype.SequenceCase;
import org.sing_group.seda.io.LineBreakType;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class ReformatFastaSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Reformat file";
  public static final String SHORT_NAME = "reformat";
  public static final String DESCRIPTION =
    "Change the format of a FASTA file (sequence fragment length, line break type, and sequence case).";
  public static final String GROUP = Group.GROUP_REFORMATTING.getName();

  public static final String PARAM_REMOVE_LINE_BREAKS_NAME = "remove-line-breaks";
  public static final String PARAM_REMOVE_LINE_BREAKS_SHORT_NAME = "rlb";
  public static final String PARAM_REMOVE_LINE_BREAKS_DESCRIPTION =
    "Remove line breaks";
  public static final String PARAM_REMOVE_LINE_BREAKS_HELP =
    "Whether line breaks in sequences must be removed or not. This option overrides the 'Fragment length' option.";
  public static final String PARAM_REMOVE_LINE_BREAKS_HELP_GUI = toHtml(PARAM_REMOVE_LINE_BREAKS_HELP);

  public static final String PARAM_FRAGMENT_LENGHT_NAME = "fragment-length";
  public static final String PARAM_FRAGMENT_LENGHT_SHORT_NAME = "fl";
  public static final String PARAM_FRAGMENT_LENGHT_DESCRIPTION = "Fragment length";
  public static final String PARAM_FRAGMENT_LENGHT_HELP =
    "The length of the sequence fragments. This option is ignored if the 'Remove line breaks' option is used.";
  public static final String PARAM_FRAGMENT_LENGHT_HELP_GUI = toHtml(PARAM_FRAGMENT_LENGHT_HELP);

  public static final String PARAM_LINE_BREAKS_NAME = "line-breaks";
  public static final String PARAM_LINE_BREAKS_SHORT_NAME = "lb";
  public static final String PARAM_LINE_BREAKS_DESCRIPTION = "Line breaks";
  public static final String PARAM_LINE_BREAKS_HELP =
    shortEnumString("The type of the line breaks.", LineBreakType.class);
  public static final String PARAM_LINE_BREAKS_HELP_GUI = toHtml(PARAM_LINE_BREAKS_HELP);

  public static final String PARAM_SEQUENCE_CASE_NAME = "sequence-case";
  public static final String PARAM_SEQUENCE_CASE_SHORT_NAME = "sc";
  public static final String PARAM_SEQUENCE_CASE_DESCRIPTION = "Case";
  public static final String PARAM_SEQUENCE_CASE_HELP =
    shortEnumString("The case of the sequences.", SequenceCase.class);
  public static final String PARAM_SEQUENCE_CASE_HELP_GUI = toHtml(PARAM_SEQUENCE_CASE_HELP);
}
