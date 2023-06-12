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
package org.sing_group.seda.cli;

import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sing_group.seda.util.SedaProperties;

import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.OptionCategory;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

/**
 * An abstract class to control local execution on external software commands.
 * This class must be extended by these commands.
 */
public abstract class ExternalSoftwareExecutionCommand extends SedaCommand {
  
  public static final List<OptionCategory> SOFTWARE_EXECUTION_CATEGORY = asList(
    new OptionCategory("External dependencies options")
  );

  /**
   * List of options related to the local execution of the command.
   *
   * @return the list of options related to the local execution of the command.
   */
  protected abstract List<Option<?>> getLocalOptionsList();
  
  /**
   * Returns a map that allows mapping a local option to a property that indicates
   * if it is enabled or not. This allows enabling and disabling command options
   * (the so called "local options") at runtime by using environment variables.
   * 
   * @return the map from options to property names.
   */
  protected abstract Map<Option<?>, String> getLocalOptionsToEnablePropertyMap();
  
  protected static Map<Option<?>, String> fromLists(List<Option<?>> keys, List<String> values) {
    Map<Option<?>, String> toret = new HashMap<Option<?>, String>();

    for (int i = 0; i < keys.size(); i++) {
      toret.put(keys.get(i), values.get(i));
    }

    return toret;
  }

  protected static Map<Option<?>, String> fromKeyValue(Option<?> key, String value) {
    return fromLists(asList(key), asList(value));
  }

  /**
   * List with all options of the command.
   *
   * @return the list with all options of the command.
   */
  protected abstract List<Option<?>> createExternalSedaOptions();

  /**
   * List with all options of the command filtering the local options if the
   * local execution is disabled.
   *
   * @return the list with all options of the command filtering the local
   *         options if the local execution is disabled.
   */
  @Override
  protected List<Option<?>> createSedaOptions() {
    return this.createExternalSedaOptions().stream().filter(this::filterOption).collect(toList());
  }

  private boolean filterOption(Option<?> option) {
    return !getLocalOptionsList().contains(option) || isLocalExecutionEnabledForOption(option);
  }

  private boolean isLocalExecutionEnabledForOption(Option<?> option) {
    return !getProperty(SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
      && !getProperty(getLocalOptionsToEnablePropertyMap().get(option), "true").equals("false");
  }

  protected static void validateSingleExecutionMode(Parameters parameters, StringOption localMode, StringOption dockerMode) {
    validateSingleExecutionMode(
      parameters, localMode, dockerMode, ", chose between " + formatParam(localMode) + " or " + formatParam(dockerMode)
    );
  }

  /**
   * Validates that only one execution mode (local or Docker) has been passed in
   * the user options, raising an exception in that case and finishing the program
   * execution.
   * 
   * @param parameters
   *          the list of user options
   * @param localMode
   *          the local mode option
   * @param dockerMode
   *          the docker mode option
   * @param customMessage
   *          a custom message to add to the default error message
   */
  public static void validateSingleExecutionMode(
    Parameters parameters, StringOption localMode, StringOption dockerMode, String customMessage
  ) {
    if (parameters.hasOption(localMode) && parameters.hasOption(dockerMode)) {
      formattedValidationError("Only one execution mode can be specified" + customMessage);
    }
  }
}
