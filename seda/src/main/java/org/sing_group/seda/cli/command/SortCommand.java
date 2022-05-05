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

import static java.util.Arrays.asList;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_CRITERIA_HELP;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_CRITERIA_NAME;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_CRITERIA_SHORT_NAME;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_DESCENDING_HELP;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_DESCENDING_NAME;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_DESCENDING_SHORT_NAME;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_SORT_ON_HELP;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_SORT_ON_NAME;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.PARAM_SORT_ON_SHORT_NAME;
import static org.sing_group.seda.plugin.core.SortSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.gui.sort.SortTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class SortCommand extends SedaCommand {

  public static final DefaultValuedStringOption OPTION_SORT_ON =
    new DefaultValuedStringOption(
      PARAM_SORT_ON_NAME, PARAM_SORT_ON_SHORT_NAME, PARAM_SORT_ON_HELP, SequenceTarget.HEADER.name().toLowerCase()
    );

  public static final FlagOption OPTION_DESCENDING =
    new FlagOption(PARAM_DESCENDING_NAME, PARAM_DESCENDING_SHORT_NAME, PARAM_DESCENDING_HELP);

  public static final DefaultValuedStringOption OPTION_CRITERIA =
    new DefaultValuedStringOption(
      PARAM_CRITERIA_NAME, PARAM_CRITERIA_SHORT_NAME, PARAM_CRITERIA_HELP,
      SequenceComparator.LENGTH.name().toLowerCase()
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
    return asList(
      OPTION_SORT_ON,
      OPTION_DESCENDING,
      OPTION_CRITERIA
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    SortTransformationProvider provider = new SortTransformationProvider();

    SequenceTarget sequenceTarget = null;
    SequenceComparator sequenceComparator = null;

    List<String> errorList = new ArrayList<>();

    try {
      sequenceTarget = SequenceTarget.valueOf(parameters.getSingleValue(OPTION_SORT_ON).toUpperCase());
    } catch (IllegalArgumentException e) {
      errorList.add("Invalid value for " + PARAM_SORT_ON_NAME + " (" + PARAM_SORT_ON_HELP + ")");
    }

    try {
      sequenceComparator = SequenceComparator.valueOf(parameters.getSingleValue(OPTION_CRITERIA).toUpperCase());
    } catch (IllegalArgumentException e) {
      errorList.add("Invalid value for " + PARAM_CRITERIA_NAME + " (" + PARAM_CRITERIA_HELP + ")");
    }

    if (!errorList.isEmpty()) {
      formattedValidationErrors(errorList);
    }

    provider.setSequenceTarget(sequenceTarget);
    provider.setSequenceComparator(sequenceComparator);
    provider.setDescendingSort(parameters.hasFlag(OPTION_DESCENDING));

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<SortTransformationProvider>()
      .read(parametersFile, SortTransformationProvider.class);
  }

}
