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
package org.sing_group.seda.cli.command;

import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_ADD_INDEX_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_ADD_INDEX_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_ADD_INDEX_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_DELIMITER_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_DELIMITER_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_DELIMITER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_INDEX_DELIMITER_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_INDEX_DELIMITER_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_INDEX_DELIMITER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_POSITION_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_POSITION_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_POSITION_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_START_INDEX_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_START_INDEX_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_START_INDEX_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_STRING_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_STRING_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_STRING_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.SHORT_NAME;

import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.gui.rename.RenameHeaderTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class RenameHeaderAddWordCommand extends RenameHeaderCommand {
  public static final DefaultValuedStringOption OPTION_POSITION =
    new DefaultValuedStringOption(
      PARAM_POSITION_NAME, PARAM_POSITION_SHORT_NAME, PARAM_POSITION_HELP,
      AddStringHeaderRenamer.Position.PREFIX.name().toLowerCase()
    );

  public static final StringOption OPTION_STRING =
    new StringOption(
      PARAM_STRING_NAME, PARAM_STRING_SHORT_NAME, PARAM_STRING_HELP, true, true
    );

  public static final DefaultValuedStringOption OPTION_DELIMITER =
    new DefaultValuedStringOption(
      PARAM_DELIMITER_NAME, PARAM_DELIMITER_SHORT_NAME, PARAM_DELIMITER_HELP, "_"
    );

  public static final FlagOption OPTION_ADD_INDEX =
    new FlagOption(
      PARAM_ADD_INDEX_NAME, PARAM_ADD_INDEX_SHORT_NAME, PARAM_ADD_INDEX_HELP
    );

  public static final DefaultValuedStringOption OPTION_INDEX_DELIMITER =
    new DefaultValuedStringOption(
      PARAM_INDEX_DELIMITER_NAME, PARAM_INDEX_DELIMITER_SHORT_NAME, PARAM_INDEX_DELIMITER_HELP, "_"
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_START_INDEX =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_START_INDEX_NAME, PARAM_START_INDEX_SHORT_NAME, PARAM_START_INDEX_HELP, 1
    );

  @Override
  public String getName() {
    return SHORT_NAME;
  }

  @Override
  public String getDescriptiveName() {
    return NAME;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    final List<Option<?>> options = new ArrayList<>();

    options.addAll(super.createSedaOptions());
    options.add(OPTION_POSITION);
    options.add(OPTION_STRING);
    options.add(OPTION_DELIMITER);
    options.add(OPTION_ADD_INDEX);
    options.add(OPTION_INDEX_DELIMITER);
    options.add(OPTION_START_INDEX);

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    RenameHeaderTransformationProvider provider = new RenameHeaderTransformationProvider();

    checkMandatoryOption(parameters, OPTION_STRING);

    AddStringHeaderRenamer.Position position = null;

    try {
      position =
        AddStringHeaderRenamer.Position
          .valueOf(parameters.getSingleValueString(OPTION_POSITION).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_POSITION);
    }

    String stringToAdd = parameters.getSingleValue(OPTION_STRING);
    String delimiter = parameters.getSingleValue(OPTION_DELIMITER);
    boolean hasIndex = parameters.hasFlag(OPTION_ADD_INDEX);
    int startIndex = parameters.getSingleValue(OPTION_START_INDEX);
    String indexDelimiter = "";

    if (hasIndex) {
      indexDelimiter = parameters.getSingleValue(OPTION_INDEX_DELIMITER);
    }

    HeaderTarget headerTarget = getHeaderTarget(parameters);

    provider.setHeaderRenamer(
      new AddStringHeaderRenamer(
        headerTarget, stringToAdd, delimiter, position, hasIndex, indexDelimiter, startIndex
      )
    );

    return provider;
  }
}
