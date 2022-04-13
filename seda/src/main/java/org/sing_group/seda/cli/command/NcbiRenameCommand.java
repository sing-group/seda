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
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENTS_MAP_FILE_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENTS_MAP_FILE_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENTS_MAP_FILE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENT_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENT_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_BLANK_SPACES_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_BLANK_SPACES_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_BLANK_SPACES_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_SAVE_REPLACEMENTS_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_SAVE_REPLACEMENTS_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_CONFIG_SAVE_REPLACEMENTS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_FILE_DELIMITER_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_FILE_DELIMITER_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_FILE_DELIMITER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_FILE_POSITION_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_FILE_POSITION_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_FILE_POSITION_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_DELIMITER_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_DELIMITER_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_DELIMITER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_INDEX_DELIMITER_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_INDEX_DELIMITER_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_INDEX_DELIMITER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_INDEX_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_INDEX_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_INDEX_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_POSITION_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_POSITION_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_HEADER_POSITION_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_NCBI_DELIMITER_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_NCBI_DELIMITER_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_NCBI_DELIMITER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_NCBI_FIELDS_HELP;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_NCBI_FIELDS_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.PARAM_NCBI_FIELDS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.NcbiRenameSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyFields;
import org.sing_group.seda.gui.ncbi.NcbiRenameTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class NcbiRenameCommand extends SedaCommand {

  public static final DefaultValuedStringOption OPTION_FILE_POSITION =
    new DefaultValuedStringOption(
      PARAM_FILE_POSITION_NAME, PARAM_FILE_POSITION_SHORT_NAME, PARAM_FILE_POSITION_HELP,
      MapRenameSequencesGroupDatasetTransformation.RenameMode.PREFIX.name().toLowerCase()
    );

  public static final StringOption OPTION_FILE_DELIMITER =
    new StringOption(
      PARAM_FILE_DELIMITER_NAME, PARAM_FILE_DELIMITER_SHORT_NAME, PARAM_FILE_DELIMITER_HELP, true, true
    );

  public static final DefaultValuedStringOption OPTION_HEADER_POSITION =
    new DefaultValuedStringOption(
      PARAM_HEADER_POSITION_NAME, PARAM_HEADER_POSITION_SHORT_NAME, PARAM_HEADER_POSITION_HELP,
      MapRenameSequencesGroupDatasetTransformation.RenameMode.PREFIX.name().toLowerCase()
    );

  public static final StringOption OPTION_HEADER_DELIMITER =
    new StringOption(
      PARAM_HEADER_DELIMITER_NAME, PARAM_HEADER_DELIMITER_SHORT_NAME, PARAM_HEADER_DELIMITER_HELP, true, true
    );

  public static final FlagOption OPTION_HEADER_INDEX =
    new FlagOption(PARAM_HEADER_INDEX_NAME, PARAM_HEADER_INDEX_SHORT_NAME, PARAM_HEADER_INDEX_HELP);

  public static final StringOption OPTION_HEADER_INDEX_DELIMITER =
    new StringOption(
      PARAM_HEADER_INDEX_DELIMITER_NAME, PARAM_HEADER_INDEX_DELIMITER_SHORT_NAME, PARAM_HEADER_INDEX_DELIMITER_HELP,
      true, true
    );

  public static final FlagOption OPTION_CONFIG_REPLACE_BLANK_SPACES =
    new FlagOption(
      PARAM_CONFIG_REPLACE_BLANK_SPACES_NAME, PARAM_CONFIG_REPLACE_BLANK_SPACES_SHORT_NAME,
      PARAM_CONFIG_REPLACE_BLANK_SPACES_HELP
    );

  public static final FlagOption OPTION_CONFIG_REPLACE_SPECIAL_CHARACTERS =
    new FlagOption(
      PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_NAME, PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_SHORT_NAME,
      PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_HELP
    );

  public static final StringOption OPTION_CONFIG_REPLACEMENT =
    new StringOption(
      PARAM_CONFIG_REPLACEMENT_NAME, PARAM_CONFIG_REPLACEMENT_SHORT_NAME, PARAM_CONFIG_REPLACEMENT_HELP, true, true
    );

  public static final FlagOption OPTION_CONFIG_SAVE_REPLACEMENTS =
    new FlagOption(
      PARAM_CONFIG_SAVE_REPLACEMENTS_NAME, PARAM_CONFIG_SAVE_REPLACEMENTS_SHORT_NAME,
      PARAM_CONFIG_SAVE_REPLACEMENTS_HELP
    );

  public static final FileOption OPTION_CONFIG_REPLACEMENTS_MAP_FILE =
    new FileOption(
      PARAM_CONFIG_REPLACEMENTS_MAP_FILE_NAME, PARAM_CONFIG_REPLACEMENTS_MAP_FILE_SHORT_NAME,
      PARAM_CONFIG_REPLACEMENTS_MAP_FILE_HELP, true, true
    );

  public static final StringOption OPTION_NCBI_DELIMITER =
    new StringOption(
      PARAM_NCBI_DELIMITER_NAME, PARAM_NCBI_DELIMITER_SHORT_NAME, PARAM_NCBI_DELIMITER_HELP, true, true
    );

  public static final StringOption OPTION_NCBI_FIELDS =
    new StringOption(PARAM_NCBI_FIELDS_NAME, PARAM_NCBI_FIELDS_SHORT_NAME, PARAM_NCBI_FIELDS_HELP, true, true, true);

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
  protected TransformationProvider getTransformation(
    Parameters parameters
  ) {
    NcbiRenameTransformationProvider provider = new NcbiRenameTransformationProvider();

    provider = this.setFileNameParams(provider, parameters);
    provider = this.setSequenceHeadersParams(provider, parameters);
    provider = this.setConfigurationParams(provider, parameters);
    provider = this.setNCBITaxonomyParams(provider, parameters);

    return provider;
  }

  private NcbiRenameTransformationProvider setFileNameParams(
    NcbiRenameTransformationProvider provider, Parameters parameters
  ) {

    provider.setFilePosition(
      MapRenameSequencesGroupDatasetTransformation.RenameMode
        .valueOf(parameters.getSingleValueString(OPTION_FILE_POSITION).toUpperCase())
    );

    if (parameters.hasOption(OPTION_FILE_DELIMITER)) {
      provider.setFileDelimiter(parameters.getSingleValueString(OPTION_FILE_DELIMITER));
    }

    return provider;
  }

  private NcbiRenameTransformationProvider setSequenceHeadersParams(
    NcbiRenameTransformationProvider provider, Parameters parameters
  ) {

    provider.setSequencePosition(
      MapRenameSequencesGroupDatasetTransformation.RenameMode
        .valueOf(parameters.getSingleValueString(OPTION_HEADER_POSITION).toUpperCase())
    );

    if (parameters.hasOption(OPTION_HEADER_DELIMITER)) {
      provider.setSequenceDelimiter(parameters.getSingleValueString(OPTION_HEADER_DELIMITER));
    }

    provider.setSequenceAddIndex(parameters.hasFlag(OPTION_HEADER_INDEX));

    if (parameters.hasOption(OPTION_HEADER_INDEX_DELIMITER)) {
      provider.setSequenceIndexDelimiter(parameters.getSingleValueString(OPTION_HEADER_INDEX_DELIMITER));
    }

    return provider;
  }

  private NcbiRenameTransformationProvider setConfigurationParams(
    NcbiRenameTransformationProvider provider, Parameters parameters
  ) {

    provider.setReplaceBlankSpaces(parameters.hasFlag(OPTION_CONFIG_REPLACE_BLANK_SPACES));
    provider.setReplaceSpecialCharacters(parameters.hasFlag(OPTION_CONFIG_REPLACE_SPECIAL_CHARACTERS));

    if (parameters.hasOption(OPTION_CONFIG_REPLACEMENT)) {
      provider.setReplacementString(parameters.getSingleValueString(OPTION_CONFIG_REPLACEMENT));
    }

    boolean hasMapFile = parameters.hasFlag(OPTION_CONFIG_SAVE_REPLACEMENTS);

    if (hasMapFile) {
      provider.setSaveReplacementsMap(hasMapFile);

      if (!parameters.hasOption(OPTION_CONFIG_REPLACEMENTS_MAP_FILE)) {
        formattedValidationError("Replacements save map is active but file is not set");
      } else {
        File mapFile = parameters.getSingleValue(OPTION_CONFIG_REPLACEMENTS_MAP_FILE);
        if (!mapFile.getParentFile().isDirectory()) {
          formattedValidationError("The specified replacements file path does not exist");
        }

        if (!mapFile.getParentFile().canWrite()) {
          formattedValidationError("The directory containing the replacements file is not writable.");
        }

        provider.setReplacementsMapFile(mapFile);
      }
    }

    return provider;
  }

  private NcbiRenameTransformationProvider setNCBITaxonomyParams(
    NcbiRenameTransformationProvider provider, Parameters parameters
  ) {

    if (parameters.hasOption(OPTION_NCBI_DELIMITER)) {
      provider.setNcbiTaxonomyDelimiter(parameters.getSingleValueString(OPTION_NCBI_DELIMITER));
    }
    List<NcbiTaxonomyFields> fieldsList = new ArrayList<>();
    if (parameters.hasOption(OPTION_NCBI_FIELDS)) {
      fieldsList =
        parameters.getAllValues(OPTION_NCBI_FIELDS).stream().map(String::toUpperCase).map(NcbiTaxonomyFields::valueOf)
          .collect(Collectors.toList());
    }
    provider.setNcbiTaxonomyFields(fieldsList);

    return provider;
  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    return asList(
      OPTION_FILE_POSITION,
      OPTION_FILE_DELIMITER,
      OPTION_HEADER_POSITION,
      OPTION_HEADER_DELIMITER,
      OPTION_HEADER_INDEX,
      OPTION_HEADER_INDEX_DELIMITER,
      OPTION_CONFIG_REPLACE_BLANK_SPACES,
      OPTION_CONFIG_REPLACE_SPECIAL_CHARACTERS,
      OPTION_CONFIG_REPLACEMENT,
      OPTION_CONFIG_SAVE_REPLACEMENTS,
      OPTION_CONFIG_REPLACEMENTS_MAP_FILE,
      OPTION_NCBI_DELIMITER,
      OPTION_NCBI_FIELDS
    );
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<NcbiRenameTransformationProvider>()
      .read(parametersFile, NcbiRenameTransformationProvider.class);
  }

}
