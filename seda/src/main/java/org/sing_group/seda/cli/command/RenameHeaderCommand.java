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

import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderSedaPluginInfo.PARAM_TARGET_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderSedaPluginInfo.PARAM_TARGET_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderSedaPluginInfo.PARAM_TARGET_SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.gui.rename.RenameHeaderTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public abstract class RenameHeaderCommand extends SedaCommand {

  public static final DefaultValuedStringOption OPTION_TARGET =
    new DefaultValuedStringOption(
      PARAM_TARGET_NAME, PARAM_TARGET_SHORT_NAME, PARAM_TARGET_HELP,
      HeaderTarget.ALL.name().toLowerCase()
    );

  @Override
  protected List<Option<?>> createSedaOptions() {
    final List<Option<?>> options = new ArrayList<>();
    options.add(OPTION_TARGET);

    return options;
  }

  protected HeaderTarget getHeaderTarget(Parameters parameters) {
    HeaderTarget target = null;

    try {
      target = HeaderTarget.valueOf(parameters.getSingleValue(OPTION_TARGET).toUpperCase());

    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_TARGET);
    }

    return target;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<RenameHeaderTransformationProvider>()
      .read(parametersFile, RenameHeaderTransformationProvider.class);
  }
}
