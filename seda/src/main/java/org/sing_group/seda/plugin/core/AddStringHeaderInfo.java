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

import static java.util.stream.Collectors.joining;

import java.util.stream.Stream;

import org.sing_group.seda.core.rename.AddStringHeaderRenamer;

public class AddStringHeaderInfo extends AbstractInfo {
  public static final String PARAM_POSITION_NAME = "position";
  public static final String PARAM_POSITION_SHORT_NAME = "p";
  public static final String PARAM_POSITION_DESCRIPTION = "Position";
  public static final String PARAM_POSITION_HELP =
    "The position where the string must be added. One of: "
      + Stream.of(AddStringHeaderRenamer.Position.values()).map(AddStringHeaderRenamer.Position::name)
        .map(String::toLowerCase).collect(joining(", ", "", "."));
  public static final String PARAM_POSITION_HELP_GUI = toHtml(PARAM_POSITION_HELP);

  public static final String PARAM_STRING_NAME = "string";
  public static final String PARAM_STRING_SHORT_NAME = "s";
  public static final String PARAM_STRING_DESCRIPTION = "String";
  public static final String PARAM_STRING_HELP = "The string to add.";
  public static final String PARAM_STRING_HELP_GUI = toHtml(PARAM_STRING_HELP);

  public static final String PARAM_DELIMITER_NAME = "delimiter";
  public static final String PARAM_DELIMITER_SHORT_NAME = "dl";
  public static final String PARAM_DELIMITER_DESCRIPTION = "Delimiter";
  public static final String PARAM_DELIMITER_HELP = "The string delimiter.";
  public static final String PARAM_DELIMITER_HELP_GUI = toHtml(PARAM_DELIMITER_HELP);

  public static final String PARAM_ADD_INDEX_NAME = "add-index";
  public static final String PARAM_ADD_INDEX_SHORT_NAME = "ai";
  public static final String PARAM_ADD_INDEX_DESCRIPTION = "Add index";
  public static final String PARAM_ADD_INDEX_HELP = "Whether an index must be added or not.";
  public static final String PARAM_ADD_INDEX_HELP_GUI = toHtml(PARAM_ADD_INDEX_HELP);

  public static final String PARAM_INDEX_DELIMITER_NAME = "index-delimiter";
  public static final String PARAM_INDEX_DELIMITER_SHORT_NAME = "idl";
  public static final String PARAM_INDEX_DELIMITER_DESCRIPTION = "Index delimiter";
  public static final String PARAM_INDEX_DELIMITER_HELP = "The index delimiter.";
  public static final String PARAM_INDEX_DELIMITER_HELP_GUI = toHtml(PARAM_INDEX_DELIMITER_HELP);
}
