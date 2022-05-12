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

import static org.sing_group.seda.plugin.core.RenameHeaderSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.RenameHeaderSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderSedaPluginInfo.PARAM_RENAME_TYPE_HELP;
import static org.sing_group.seda.plugin.core.RenameHeaderSedaPluginInfo.PARAM_RENAME_TYPE_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderSedaPluginInfo.PARAM_RENAME_TYPE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderSedaPluginInfo.PARAM_TARGET_HELP;
import static org.sing_group.seda.plugin.core.RenameHeaderSedaPluginInfo.PARAM_TARGET_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderSedaPluginInfo.PARAM_TARGET_SHORT_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.cli.parameters.AddStringHeaderParameters;
import org.sing_group.seda.cli.parameters.FieldSplitRenamerParameters;
import org.sing_group.seda.cli.parameters.IntervalReplaceRenamerParameters;
import org.sing_group.seda.cli.parameters.WordReplaceRenamerParameters;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.gui.rename.RenameHeaderTransformationConfigurationPanel;
import org.sing_group.seda.gui.rename.RenameHeaderTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class RenameHeaderCommand extends SedaCommand {

  public static final DefaultValuedStringOption OPTION_TARGET =
    new DefaultValuedStringOption(
      PARAM_TARGET_NAME, PARAM_TARGET_SHORT_NAME, PARAM_TARGET_HELP,
      HeaderTarget.ALL.name().toLowerCase()
    );

  public static final StringOption OPTION_RENAME_TYPE =
    new StringOption(PARAM_RENAME_TYPE_NAME, PARAM_RENAME_TYPE_SHORT_NAME, PARAM_RENAME_TYPE_HELP, true, true);

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
    final List<Option<?>> options = new ArrayList<>();
    options.add(OPTION_TARGET);
    options.add(OPTION_RENAME_TYPE);

    options.addAll(AddStringHeaderParameters.getOptionList());
    options.addAll(FieldSplitRenamerParameters.getOptionList());
    options.addAll(IntervalReplaceRenamerParameters.getOptionList());
    options.addAll(WordReplaceRenamerParameters.getOptionList());

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    RenameHeaderTransformationProvider provider = new RenameHeaderTransformationProvider();

    HeaderTarget target = null;

    List<String> errorList = new ArrayList<>();
    try {
      target = HeaderTarget.valueOf(parameters.getSingleValue(OPTION_TARGET).toUpperCase());

    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_TARGET);
    }

    if (!parameters.hasOption(OPTION_RENAME_TYPE)) {
      errorList.add("Rename type is mandatory");
    }

    RenameHeaderTransformationConfigurationPanel.Rename renameType = null;

    try {
      renameType =
        RenameHeaderTransformationConfigurationPanel.Rename
          .valueOf(parameters.getSingleValue(OPTION_RENAME_TYPE).toUpperCase());
    } catch (IllegalArgumentException e) {
      errorList.add("Invalid value for " + PARAM_RENAME_TYPE_NAME + " (" + PARAM_RENAME_TYPE_HELP + ")");
    }

    HeaderRenamer renamer = null;

    try {
      switch (renameType) {
        case REPLACE_WORD:
          renamer = WordReplaceRenamerParameters.getHeaderRenamer(target, parameters);
          break;
        case ADD_STRING:
          renamer = AddStringHeaderParameters.getHeaderRenamer(target, parameters);
          break;
        case MULTIPART_HEADER:
          renamer = FieldSplitRenamerParameters.getHeaderRenamer(target, parameters);
          break;
        case REPLACE_INTERVAL:
          renamer = IntervalReplaceRenamerParameters.getHeaderRenamer(target, parameters);
          break;
        default:
          errorList.add("Unknown rename type, check command help to see the available options");
      }
    } catch (IllegalArgumentException exception) {
      errorList.add(exception.getMessage());
    }

    if (!errorList.isEmpty()) {
      formattedValidationErrors(errorList);
    }

    provider.setHeaderRenamer(renamer);

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<RenameHeaderTransformationProvider>()
      .read(parametersFile, RenameHeaderTransformationProvider.class);
  }
}
