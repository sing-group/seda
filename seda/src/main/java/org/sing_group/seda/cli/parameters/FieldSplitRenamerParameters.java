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
package org.sing_group.seda.cli.parameters;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.cli.SedaCommand.checkMandatoryOption;
import static org.sing_group.seda.cli.SedaCommand.invalidEnumValue;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_FIELDS_HELP;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_FIELDS_NAME;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_FIELDS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_FIELD_DELIMITER_HELP;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_FIELD_DELIMITER_NAME;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_FIELD_DELIMITER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_FIELD_MODE_HELP;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_FIELD_MODE_NAME;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_FIELD_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_JOIN_DELIMITER_HELP;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_JOIN_DELIMITER_NAME;
import static org.sing_group.seda.plugin.core.FieldSplitRenameInfo.PARAM_JOIN_DELIMITER_SHORT_NAME;

import java.util.List;

import org.sing_group.seda.core.rename.FieldSplitRenamer;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class FieldSplitRenamerParameters {
  public static final StringOption OPTION_FIELD_DELIMITER =
    new StringOption(
      PARAM_FIELD_DELIMITER_NAME, PARAM_FIELD_DELIMITER_SHORT_NAME, PARAM_FIELD_DELIMITER_HELP, true, true
    );

  public static final StringOption OPTION_JOIN_DELIMITER =
    new StringOption(
      PARAM_JOIN_DELIMITER_NAME, PARAM_JOIN_DELIMITER_SHORT_NAME, PARAM_JOIN_DELIMITER_HELP, true, true
    );

  public static final DefaultValuedStringOption OPTION_FIELD_MODE =
    new DefaultValuedStringOption(
      PARAM_FIELD_MODE_NAME, PARAM_FIELD_MODE_SHORT_NAME, PARAM_FIELD_MODE_HELP,
      FieldSplitRenamer.Mode.KEEP.name().toLowerCase()
    );

  public static final IntegerOption OPTION_FIELDS =
    new IntegerOption(PARAM_FIELDS_NAME, PARAM_FIELDS_SHORT_NAME, PARAM_FIELDS_HELP, true, true);

  public static List<Option<?>> getOptionList() {
    return asList(
      OPTION_FIELD_DELIMITER,
      OPTION_JOIN_DELIMITER,
      OPTION_FIELD_MODE,
      OPTION_FIELDS
    );
  }

  public static HeaderRenamer getHeaderRenamer(HeaderTarget headerTarget, Parameters parameters)
    throws IllegalArgumentException {
    checkMandatoryOption(parameters, OPTION_FIELD_DELIMITER);
    checkMandatoryOption(parameters, OPTION_JOIN_DELIMITER);
    checkMandatoryOption(parameters, OPTION_FIELDS);

    String fieldDelimiter = parameters.getSingleValue(OPTION_FIELD_DELIMITER);
    String joinDelimiter = parameters.getSingleValue(OPTION_JOIN_DELIMITER);

    FieldSplitRenamer.Mode mode = null;
    try {
      mode = FieldSplitRenamer.Mode.valueOf(parameters.getSingleValue(OPTION_FIELD_MODE).toUpperCase());

    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_FIELD_MODE);
    }
    List<Integer> fields = parameters.getAllValues(OPTION_FIELDS).stream().map(f -> f - 1).collect(toList());

    return new FieldSplitRenamer(headerTarget, fieldDelimiter, joinDelimiter, mode, fields);
  }
}
