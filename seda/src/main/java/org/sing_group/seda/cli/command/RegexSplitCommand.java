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

import static org.sing_group.seda.plugin.core.info.plugin.RegexSplitSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RegexSplitSedaPluginInfo.GROUP;
import static org.sing_group.seda.plugin.core.info.plugin.RegexSplitSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RegexSplitSedaPluginInfo.PARAM_FILES_DIRECTORY_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RegexSplitSedaPluginInfo.PARAM_FILES_DIRECTORY_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RegexSplitSedaPluginInfo.PARAM_GROUP_NAMES_FILES_DIRECTORY_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RegexSplitSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.cli.parameters.RegexHeaderMatcherParameters;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.gui.split.regex.RegexSplitConfigurationTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class RegexSplitCommand extends SedaCommand {

  public static final FileOption OPTION_FILES_DIRECTORY =
    new FileOption(
      PARAM_FILES_DIRECTORY_NAME, PARAM_FILES_DIRECTORY_SHORT_NAME, PARAM_GROUP_NAMES_FILES_DIRECTORY_HELP, true, true
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
  protected String getSedaGroup() {
    return GROUP;
  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    List<Option<?>> optionList = new ArrayList<>();

    optionList.add(OPTION_FILES_DIRECTORY);
    optionList.addAll(RegexHeaderMatcherParameters.getOptionList());

    return optionList;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    RegexSplitConfigurationTransformationProvider provider = new RegexSplitConfigurationTransformationProvider();

    try {
      provider.setHeaderMatcher(RegexHeaderMatcherParameters.getRegexHeaderMatcher(parameters));
    } catch (IllegalArgumentException e) {
      formattedValidationError(e.getMessage());
    }

    if (parameters.hasOption(OPTION_FILES_DIRECTORY)) {
      File fileDirectory = parameters.getSingleValue(OPTION_FILES_DIRECTORY);
      if (!fileDirectory.isDirectory()) {
        invalidOptionValue(OPTION_FILES_DIRECTORY, "The specified path is not a directory");
      }
      provider.setSaveGroupNamesDirectory(fileDirectory);
    }

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<RegexSplitConfigurationTransformationProvider>()
      .read(parametersFile, RegexSplitConfigurationTransformationProvider.class);
  }
}
