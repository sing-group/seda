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
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.sing_group.seda.util.SedaProperties;

import es.uvigo.ei.sing.yacli.command.option.Option;

/**
 * An abstract class to control local execution on external software commands.
 * This class must be extended by these commands.
 */
public abstract class ExternalSoftwareExecutionCommand extends SedaCommand {

  /**
   * The name of the property to check is local execution is enabled for the
   * command.
   *
   * @return the name of the property to check is local execution is enabled for
   *         the command.
   */
  protected abstract String getPropertyEnableLocalExecution();

  /**
   * List of options related to the local execution of the command.
   *
   * @return the list of options related to the local execution of the command.
   */
  protected abstract List<Option<?>> getLocalOptionsList();

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
    return this.isLocalExecutionEnabled() ? 
      this.createExternalSedaOptions() : 
      this.createExternalSedaOptions().stream()
        .filter(option -> !this.getLocalOptionsList().contains(option))
        .collect(toList());
  }

  private boolean isLocalExecutionEnabled() {
    return !getProperty(SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
      && !getProperty(getPropertyEnableLocalExecution(), "true").equals("false");
  }
}
