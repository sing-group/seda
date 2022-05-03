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
import static org.sing_group.seda.plugin.core.BasePresenceSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.BasePresenceSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.BasePresenceSedaPluginInfo.PARAM_BASE_FILTER_HELP_WITH_CONFIG;
import static org.sing_group.seda.plugin.core.BasePresenceSedaPluginInfo.PARAM_BASE_FILTER_NAME;
import static org.sing_group.seda.plugin.core.BasePresenceSedaPluginInfo.PARAM_BASE_FILTER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.BasePresenceSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.gui.filtering.base.FilterByBasePresenceTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.FilterByBasePresenceTransformation;

import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class FilterByBasePresenceCommand extends SedaCommand {

  public static final StringOption OPTION_BASE_FILTERING =
    new StringOption(
      PARAM_BASE_FILTER_NAME, PARAM_BASE_FILTER_SHORT_NAME, PARAM_BASE_FILTER_HELP_WITH_CONFIG, true, true, true
    );

  private final double MIN_PRESENCE = 0.0;
  private final double MAX_PRESENCE = 1.0;
  private final String CONFIG_BASE_FILTER_REGEX = "config\\((1|0.[0-9]+)/(1|0.[0-9]+)\\):[a-zA-Z]+";

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
    return asList(OPTION_BASE_FILTERING);
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    FilterByBasePresenceTransformationProvider provider = new FilterByBasePresenceTransformationProvider();

    if (!parameters.hasOption(OPTION_BASE_FILTERING)) {
      formattedValidationError("Base filtering option is mandatory");
    }

    provider.setBasePresences(
      parameters.getAllValues(OPTION_BASE_FILTERING).stream().map(this::getBasePresences).collect(
        Collectors.toList()
      )
    );

    return provider;
  }

  protected FilterByBasePresenceTransformation.BasePresence getBasePresences(String basePresence) {

    double minimumPresence = MIN_PRESENCE;
    double maximumPresence = MAX_PRESENCE;
    String base = basePresence;

    if (basePresence.contains("config")) {
      if (!basePresence.matches(CONFIG_BASE_FILTER_REGEX)) {
        formattedValidationError("Wrong base filtering config. Check help command.");
      }
      String config =
        basePresence
          .split(":")[0]
            .replace("config", "")
            .replace("(", "")
            .replace(")", "");
      minimumPresence = Double.parseDouble(config.split("/")[0]);
      maximumPresence = Double.parseDouble(config.split("/")[1]);
      base = basePresence.split(":")[1];
    }

    return new FilterByBasePresenceTransformation.BasePresence(
      minimumPresence,
      maximumPresence,
      base.toCharArray()
    );
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<FilterByBasePresenceTransformationProvider>()
      .read(parametersFile, FilterByBasePresenceTransformationProvider.class);
  }

}