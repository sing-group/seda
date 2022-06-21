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
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.SHORT_NAME;

import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.parameters.AddStringHeaderRenamerCliParameters;
import org.sing_group.seda.gui.rename.RenameHeaderTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class RenameHeaderAddWordCommand extends RenameHeaderCommand {

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

  private AddStringHeaderRenamerCliParameters addStringHeader;

  @Override
  protected List<Option<?>> createSedaOptions() {
    final List<Option<?>> options = new ArrayList<>();

    this.addStringHeader = new AddStringHeaderRenamerCliParameters(true, OPTION_TARGET);

    options.addAll(addStringHeader.getOptionList());

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    RenameHeaderTransformationProvider provider = new RenameHeaderTransformationProvider();

    provider.setHeaderRenamer(this.addStringHeader.getAddStringHeaderRenamer(parameters));

    return provider;
  }
}
