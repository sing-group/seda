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
import static org.sing_group.seda.plugin.core.info.plugin.MergeSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.MergeSedaPluginInfo.GROUP;
import static org.sing_group.seda.plugin.core.info.plugin.MergeSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.MergeSedaPluginInfo.PARAM_NAME_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.MergeSedaPluginInfo.PARAM_NAME_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.MergeSedaPluginInfo.PARAM_NAME_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.MergeSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.gui.merge.MergeTransformationProvider;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class MergeCommand extends ReformatFastaCommand {

  public static final StringOption OPTION_NAME =
    new StringOption(PARAM_NAME_NAME, PARAM_NAME_SHORT_NAME, PARAM_NAME_HELP, true, true);

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
  protected String getSedaGroup() {
    return GROUP;
  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    final List<Option<?>> options = new ArrayList<>();

    options.add(OPTION_NAME);
    options.addAll(super.createSedaOptions());

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    MergeTransformationProvider provider = new MergeTransformationProvider();

    provider.setName(parameters.getSingleValue(OPTION_NAME));

    provider.setReformatFastaTransformationProvider(
      (ReformatFastaTransformationProvider) super.getTransformation(parameters)
    );

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<MergeTransformationProvider>()
      .read(parametersFile, MergeTransformationProvider.class);
  }

  @Override
  protected List<Option<?>> getMandatoryOptions() {
    return asList(OPTION_NAME);
  }
}
