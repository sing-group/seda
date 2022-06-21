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

import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.GROUP;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_CONVERT_AMINO_ACID_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_MERGE_HEADERS_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_MERGE_HEADERS_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_MERGE_HEADERS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_REMOVE_SUBSEQUENCE_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_REMOVE_SUBSEQUENCE_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_REMOVE_SUBSEQUENCE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_SAVE_MERGED_HEADERS_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_SAVE_MERGED_HEADERS_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_SAVE_MERGED_HEADERS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.cli.parameters.SequenceTranslationCliParameters;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.gui.redundant.RemoveRedundantSequencesTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;

import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class RemoveRedundantSequencesCommand extends SedaCommand {

  public static final FlagOption OPTION_REMOVE_SUBSEQUENCE =
    new FlagOption(PARAM_REMOVE_SUBSEQUENCE_NAME, PARAM_REMOVE_SUBSEQUENCE_SHORT_NAME, PARAM_REMOVE_SUBSEQUENCE_HELP);

  public static final FlagOption OPTION_MERGE_HEADERS =
    new FlagOption(PARAM_MERGE_HEADERS_NAME, PARAM_MERGE_HEADERS_SHORT_NAME, PARAM_MERGE_HEADERS_HELP);

  public static final FileOption OPTION_SAVE_MERGED_HEADERS =
    new FileOption(
      PARAM_SAVE_MERGED_HEADERS_NAME, PARAM_SAVE_MERGED_HEADERS_SHORT_NAME, PARAM_SAVE_MERGED_HEADERS_HELP, true, true
    );

  private SequenceTranslationCliParameters sequenceTranslationCliParameters;

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
    this.sequenceTranslationCliParameters =
      new SequenceTranslationCliParameters(
        true, false, PARAM_CONVERT_AMINO_ACID_HELP
      );

    final List<Option<?>> options = new ArrayList<>();

    options.add(OPTION_REMOVE_SUBSEQUENCE);
    options.add(OPTION_MERGE_HEADERS);
    options.add(OPTION_SAVE_MERGED_HEADERS);
    options.addAll(this.sequenceTranslationCliParameters.getOptionList());

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    RemoveRedundantSequencesTransformationProvider provider = new RemoveRedundantSequencesTransformationProvider();

    boolean mergeHeaders = parameters.hasFlag(OPTION_MERGE_HEADERS);
    File mergedHeadersFile = null;
    if (parameters.hasOption(OPTION_SAVE_MERGED_HEADERS)) {
      mergedHeadersFile = parameters.getSingleValue(OPTION_SAVE_MERGED_HEADERS);
      if (!mergedHeadersFile.isDirectory()) {
        formattedValidationError(formatParam(OPTION_SAVE_MERGED_HEADERS) + " option must be a directory");
      }
      provider.setConfiguration(
        new RemoveRedundantSequencesTransformationConfiguration(
          getRemoveRedundantMode(parameters), mergeHeaders, mergedHeadersFile, getTranslationConfiguration(parameters)
        )
      );
    } else {
      provider.setConfiguration(
        new RemoveRedundantSequencesTransformationConfiguration(
          getRemoveRedundantMode(parameters), mergeHeaders, getTranslationConfiguration(parameters)
        )
      );
    }

    return provider;
  }

  private SequenceTranslationConfiguration getTranslationConfiguration(Parameters parameters) {
    SequenceTranslationConfiguration translationConfiguration = null;

    if (this.sequenceTranslationCliParameters.hasConvertAminoAcid(parameters)) {
      try {
        translationConfiguration =
          this.sequenceTranslationCliParameters
            .getSequenceTranslationConfiguration(parameters);
      } catch (IllegalArgumentException e) {
        formattedValidationError(e.getMessage());
      }
    }

    return translationConfiguration;
  }

  private RemoveRedundantSequencesTransformation.Mode getRemoveRedundantMode(Parameters parameters) {
    if (parameters.hasFlag(OPTION_REMOVE_SUBSEQUENCE)) {
      return RemoveRedundantSequencesTransformation.Mode.CONTAINED_SEQUENCES;
    } else {
      return RemoveRedundantSequencesTransformation.Mode.EXACT_DUPLICATES;
    }
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<RemoveRedundantSequencesTransformationProvider>()
      .read(parametersFile, RemoveRedundantSequencesTransformationProvider.class);
  }

}
