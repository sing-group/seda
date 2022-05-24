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
package org.sing_group.seda.plugin.core.info.common;

import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class HeaderMatcherInfo extends AbstractInfo {
  public static final String PARAM_SEQUENCE_MATCHING_NAME = "sequence-matching";
  public static final String PARAM_SEQUENCE_MATCHING_SHORT_NAME = "sm";
  public static final String PARAM_SEQUENCE_MATCHING_DESCRIPTION = "Sequence matching mode";
  private static final String[] PARAM_SEQUENCE_MATCHING_HELP_ENUM = {
    "Sequences are concatenated if they have the same sequence names (identifiers).",
    "Sequences are concatenade by matching headers using the regex configuration specified." };
  public static final String PARAM_SEQUENCE_MATCHING_HELP = longEnumStringForCli(
    "The sequence matching mode.",
    cliMap(HeaderFilteringConfiguration.FilterType.values(), PARAM_SEQUENCE_MATCHING_HELP_ENUM)
  );
  public static final String PARAM_SEQUENCE_MATCHING_HELP_GUI = toHtml(
    longEnumStringForGui(
      "The sequence matching mode.",
      guiMap(HeaderFilteringConfiguration.FilterType.values(), PARAM_SEQUENCE_MATCHING_HELP_ENUM)
    )
  );
}
