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
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.GROUP;
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.PARAM_MERGE_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.PARAM_MERGE_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.PARAM_MERGE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.PARAM_NAME_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.PARAM_NAME_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.PARAM_NAME_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ConcatenateSequencesSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.parameters.HeaderMatcherParameters;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.gui.concatenate.ConcatenateSequencesTransformationProvider;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class ConcatenateSequencesCommand extends ReformatFastaCommand {

  public static final StringOption OPTION_NAME =
    new StringOption(PARAM_NAME_NAME, PARAM_NAME_SHORT_NAME, PARAM_NAME_HELP, true, true);

  public static final FlagOption OPTION_MERGE_DESCRIPTIONS =
    new FlagOption(PARAM_MERGE_NAME, PARAM_MERGE_SHORT_NAME, PARAM_MERGE_HELP);

  public static final HeaderMatcherParameters OPTIONS_HEADER_MATCHER = new HeaderMatcherParameters();

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
    options.add(OPTION_MERGE_DESCRIPTIONS);
    options.addAll(OPTIONS_HEADER_MATCHER.getOptionList());
    options.addAll(super.createSedaOptions());

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    ConcatenateSequencesTransformationProvider provider = new ConcatenateSequencesTransformationProvider();

    String mergeName = parameters.getSingleValueString(OPTION_NAME);
    if (mergeName.isEmpty()) {
      formattedValidationError("The merge name cant be empty");
    }

    provider.setMergeName(mergeName);

    provider.setMergeDescriptions(parameters.hasFlag(OPTION_MERGE_DESCRIPTIONS));

    provider.setHeaderMatcher(OPTIONS_HEADER_MATCHER.getHeaderMatcher(parameters));

    provider.setReformatFastaTransformationProvider(
      (ReformatFastaTransformationProvider) super.getTransformation(parameters)
    );

    return provider;
  }

  @Override
  protected List<Option<?>> getMandatoryOptions() {
    return asList(OPTION_NAME);
  }

  @Override
  protected ConcatenateSequencesTransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<ConcatenateSequencesTransformationProvider>()
      .read(parametersFile, ConcatenateSequencesTransformationProvider.class);
  }
}
