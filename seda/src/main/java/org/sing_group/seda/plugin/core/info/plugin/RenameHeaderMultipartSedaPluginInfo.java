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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collections;

import org.sing_group.seda.core.rename.FieldSplitRenamer;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class RenameHeaderMultipartSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Rename header (Multipart)";
  public static final String SHORT_NAME = "rename-header-multipart";
  public static final String DESCRIPTION = "Modify sequence headers by keeping or removing fields.";

  public static final String PARAM_FIELD_DELIMITER_NAME = "field-delimiter";
  public static final String PARAM_FIELD_DELIMITER_SHORT_NAME = "fd";
  public static final String PARAM_FIELD_DELIMITER_DESCRIPTION = "Field delimiter";
  public static final String PARAM_FIELD_DELIMITER_HELP = "The field delimiter.";
  public static final String PARAM_FIELD_DELIMITER_HELP_GUI = toHtml(PARAM_FIELD_DELIMITER_HELP);

  public static final String PARAM_JOIN_DELIMITER_NAME = "join-delimiter";
  public static final String PARAM_JOIN_DELIMITER_SHORT_NAME = "jd";
  public static final String PARAM_JOIN_DELIMITER_DESCRIPTION = "Join delimiter";
  public static final String PARAM_JOIN_DELIMITER_HELP = "The join delimiter.";
  public static final String PARAM_JOIN_DELIMITER_HELP_GUI = toHtml(PARAM_JOIN_DELIMITER_HELP);

  public static final String PARAM_FIELD_MODE_NAME = "field-mode";
  public static final String PARAM_FIELD_MODE_SHORT_NAME = "fm";
  public static final String PARAM_FIELD_MODE_DESCRIPTION = "Mode";
  public static final String PARAM_FIELD_MODE_HELP =
    shortEnumString("Wether to keep or remove fields.", FieldSplitRenamer.Mode.class);
  public static final String PARAM_FIELD_MODE_HELP_GUI = toHtml(PARAM_FIELD_MODE_HELP);

  public static final String PARAM_FIELDS_NAME = "fields";
  public static final String PARAM_FIELDS_SHORT_NAME = "f";
  public static final String PARAM_FIELDS_DESCRIPTION = "Fields";
  public static final String PARAM_FIELDS_HELP = "The comma-separated list of fields starting at 1. "
    + "Note that when the keep mode is used, then the order of the fields is preserved in the output, meaning that it is possible to swap fields.";
  public static final String PARAM_FIELDS_HELP_GUI = toHtml(PARAM_FIELDS_HELP, emptyList(), asList("keep"), true);
}
