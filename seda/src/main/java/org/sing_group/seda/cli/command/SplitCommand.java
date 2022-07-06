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
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.GROUP;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_INDEPENDENT_EXTRACTIONS_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_INDEPENDENT_EXTRACTIONS_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_INDEPENDENT_EXTRACTIONS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_NUM_FILES_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_NUM_FILES_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_NUM_FILES_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_NUM_SEQUENCES_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_NUM_SEQUENCES_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_NUM_SEQUENCES_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_RANDOMIZE_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_RANDOMIZE_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_RANDOMIZE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_SEED_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_SEED_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_SEED_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_SPLIT_MODE_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_SPLIT_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.PARAM_SPLIT_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.SplitSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.split.SequencesGroupSplitMode;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.provider.split.SplitConfigurationTransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class SplitCommand extends SedaCommand {

  public static final FlagOption OPTION_RANDOMIZE =
    new FlagOption(PARAM_RANDOMIZE_NAME, PARAM_RANDOMIZE_SHORT_NAME, PARAM_RANDOMIZE_HELP);

  public static final IntegerDefaultValuedStringConstructedOption OPTION_SEED =
    new IntegerDefaultValuedStringConstructedOption(PARAM_SEED_NAME, PARAM_SEED_SHORT_NAME, PARAM_SEED_HELP, 1);

  public static final DefaultValuedStringOption OPTION_SPLIT_MODE =
    new DefaultValuedStringOption(
      PARAM_SPLIT_MODE_NAME, PARAM_SPLIT_MODE_SHORT_NAME, PARAM_SPLIT_MODE_HELP,
      SequencesGroupSplitMode.FIXED_SEQUENCES_PER_FILE.name().toLowerCase()
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_NUM_FILES =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_NUM_FILES_NAME, PARAM_NUM_FILES_SHORT_NAME, PARAM_NUM_FILES_HELP, 1
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_NUM_SEQUENCES =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_NUM_SEQUENCES_NAME, PARAM_NUM_SEQUENCES_SHORT_NAME, PARAM_NUM_SEQUENCES_HELP, 1
    );

  public static final FlagOption OPTION_INDEPENDENT_EXTRACTIONS =
    new FlagOption(
      PARAM_INDEPENDENT_EXTRACTIONS_NAME, PARAM_INDEPENDENT_EXTRACTIONS_SHORT_NAME, PARAM_INDEPENDENT_EXTRACTIONS_HELP
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
    return asList(
      OPTION_RANDOMIZE,
      OPTION_SEED,
      OPTION_SPLIT_MODE,
      OPTION_NUM_FILES,
      OPTION_NUM_SEQUENCES,
      OPTION_INDEPENDENT_EXTRACTIONS
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    boolean hasRandomize = parameters.hasFlag(OPTION_RANDOMIZE);
    int seed = parameters.getSingleValue(OPTION_SEED);
    int numSequences = parameters.getSingleValue(OPTION_NUM_SEQUENCES);
    int numFiles = parameters.getSingleValue(OPTION_NUM_FILES);
    boolean hasIndependentExtractions = parameters.hasFlag(OPTION_INDEPENDENT_EXTRACTIONS);

    SequencesGroupSplitMode splitMode = null;
    try {
      splitMode = SequencesGroupSplitMode.valueOf(parameters.getSingleValue(OPTION_SPLIT_MODE).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_SPLIT_MODE);
    }

    return new SplitConfigurationTransformationProvider(
      splitMode, hasRandomize, seed, hasIndependentExtractions, numFiles, numSequences
    );
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<SplitConfigurationTransformationProvider>()
      .read(parametersFile, SplitConfigurationTransformationProvider.class);
  }
}
