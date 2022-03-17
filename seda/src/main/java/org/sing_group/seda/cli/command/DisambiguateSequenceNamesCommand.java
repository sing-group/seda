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
import static org.sing_group.seda.plugin.core.DisambiguateSequenceNamesSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.DisambiguateSequenceNamesSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.DisambiguateSequenceNamesSedaPluginInfo.PARAM_MODE_HELP;
import static org.sing_group.seda.plugin.core.DisambiguateSequenceNamesSedaPluginInfo.PARAM_MODE_NAME;
import static org.sing_group.seda.plugin.core.DisambiguateSequenceNamesSedaPluginInfo.PARAM_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.DisambiguateSequenceNamesSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.gui.disambiguate.DisambiguateSequenceNamesTransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.DisambiguateSequenceNamesTransformation.Mode;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class DisambiguateSequenceNamesCommand extends SedaCommand {

  public static final DefaultValuedStringOption OPTION_MODE =
    new DefaultValuedStringOption(
      PARAM_MODE_NAME,
      PARAM_MODE_SHORT_NAME,
      PARAM_MODE_HELP, "rename"
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
  public DisambiguateSequenceNamesTransformationProvider getTransformation(Parameters parameters) {
    DisambiguateSequenceNamesTransformationProvider provider = new DisambiguateSequenceNamesTransformationProvider();
    try {
      provider.setMode(Mode.valueOf(parameters.getSingleValueString(OPTION_MODE).toUpperCase()));
    } catch (IllegalArgumentException exc) {
      validationError("Invalid mode for the transformation.");
    }

    return provider;

  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    return asList(OPTION_MODE);
  }

  @Override
  protected DisambiguateSequenceNamesTransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<DisambiguateSequenceNamesTransformationProvider>()
      .read(parametersFile, DisambiguateSequenceNamesTransformationProvider.class);
  }
}
