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

import static org.sing_group.seda.cli.SedaCommand.checkMandatoryOption;
import static org.sing_group.seda.cli.SedaCommand.invalidEnumValue;
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
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderSedaPluginInfo.PARAM_TARGET_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderSedaPluginInfo.PARAM_TARGET_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderSedaPluginInfo.PARAM_TARGET_SHORT_NAME;

import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.command.RenameHeaderAddWordCommand;
import org.sing_group.seda.cli.command.ReverseComplementCommand;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

/**
 * This class is to be reused by commands that have these parameters in common.
 *
 * @see RenameHeaderAddWordCommand
 * @see ReverseComplementCommand
 */
public class AddStringHeaderRenameCliParameter {

  public static final DefaultValuedStringOption OPTION_TARGET =
    new DefaultValuedStringOption(
      PARAM_TARGET_NAME, PARAM_TARGET_SHORT_NAME, PARAM_TARGET_HELP, HeaderTarget.ALL.name().toLowerCase()
    );

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

  private final Parameters parameters;
  private final boolean checkAddIndex;
  private final boolean useExternalTargetOption;

  /**
   * Creates a new {@code AddStringHeaderRenameCliParameter} instance, allowing
   * the AddIndex option and using an external HeaderTarget option.
   *
   * @param parameters
   *          the command parameters
   */
  public AddStringHeaderRenameCliParameter(Parameters parameters) {
    this.parameters = parameters;
    this.checkAddIndex = true;
    this.useExternalTargetOption = true;
  }

  /**
   * Creates a new {@code AddStringHeaderRenameCliParameter} instance.
   *
   * @param parameters
   *          the command parameters
   * @param checkAddIndex
   *          if the AddIndex option should be checked
   * @param useExternalTargetOption
   *          if the HeaderTarget is assigned externally or taken from the
   *          parameters
   */
  public AddStringHeaderRenameCliParameter(
    Parameters parameters, boolean checkAddIndex, boolean useExternalTargetOption
  ) {
    this.parameters = parameters;
    this.checkAddIndex = checkAddIndex;
    this.useExternalTargetOption = useExternalTargetOption;
  }

  /**
   * Lists the available command-line options for creating *
   * {@code AddStringHeaderRenamer } objects.
   *
   * @return the available command-line options for creating *
   *         {@code AddStringHeaderRenamer } objects.
   */
  public static List<Option<?>> getOptionList() {
    return getOptionList(true, true);
  }

  /**
   * Lists the available command-line options for creating *
   * {@code AddStringHeaderRenamer } objects.
   *
   * @param showAddIndex
   *          if the AddIndex option should be listed
   * @param showHeaderTargetOption
   *          if the HeaderTarget option should be listed
   * @return the available command-line options for creating *
   *         {@code AddStringHeaderRenamer } objects.
   */
  public static List<Option<?>> getOptionList(boolean showAddIndex, boolean showHeaderTargetOption) {
    final List<Option<?>> options = new ArrayList<>();

    if (!showHeaderTargetOption) {
      options.add(OPTION_TARGET);
    }

    options.add(OPTION_POSITION);
    options.add(OPTION_STRING);
    options.add(OPTION_DELIMITER);
    if (showAddIndex) {
      options.add(OPTION_ADD_INDEX);
      options.add(OPTION_INDEX_DELIMITER);
      options.add(OPTION_START_INDEX);
    }

    return options;
  }

  /**
   * Creates a new {@code AddStringHeaderRenamer} with the parameters provided,
   * taking the HeaderTarget from them.
   *
   * @return a new {@code AddStringHeaderRenamer} with the parameters provided,
   *         taking the HeaderTarget from them
   */
  public AddStringHeaderRenamer getAddStringHeaderRenamer() {
    if (this.useExternalTargetOption) {
      throw new IllegalStateException("External header target option is selected but not provided");
    }
    return this.getAddStringHeaderRenamer(getHeaderTarget(this.parameters));
  }

  /**
   * Creates a new {@code AddStringHeaderRenamer} with the parameters provided.
   *
   * @param headerTarget
   *          the header target to set in the {@code AddStringHeaderRenamer}
   * @return a new {@code AddStringHeaderRenamer} with the parameters provided
   */
  public AddStringHeaderRenamer getAddStringHeaderRenamer(HeaderTarget headerTarget) {
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
    String indexDelimiter = "";
    int startIndex = 0;
    boolean hasIndex = checkAddIndex;

    if (this.checkAddIndex) {
      hasIndex = parameters.hasFlag(OPTION_ADD_INDEX);
      startIndex = parameters.getSingleValue(OPTION_START_INDEX);

      if (hasIndex) {
        indexDelimiter = parameters.getSingleValue(OPTION_INDEX_DELIMITER);
      }
    }

    return new AddStringHeaderRenamer(
      headerTarget, stringToAdd, delimiter, position, hasIndex, indexDelimiter, startIndex
    );
  }

  private HeaderTarget getHeaderTarget(Parameters parameters) {
    HeaderTarget target = null;

    try {
      target = HeaderTarget.valueOf(parameters.getSingleValue(OPTION_TARGET).toUpperCase());

    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_TARGET);
    }

    return target;
  }
}
