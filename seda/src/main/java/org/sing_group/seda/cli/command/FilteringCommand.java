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

import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_IN_FRAME_STOP_CODONS_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_IN_FRAME_STOP_CODONS_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_IN_FRAME_STOP_CODONS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCES_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCES_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCES_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCE_LENGTH_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCE_LENGTH_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCE_LENGTH_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MAX_SIZE_DIFFERENCE_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MAX_SIZE_DIFFERENCE_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MAX_SIZE_DIFFERENCE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCES_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCES_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCES_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCE_LENGTH_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCE_LENGTH_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCE_LENGTH_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_NON_MULTIPLE_3_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_NON_MULTIPLE_3_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_NON_MULTIPLE_3_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_REMOVE_SIZE_DIFFERENCE_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_REMOVE_SIZE_DIFFERENCE_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_REMOVE_SIZE_DIFFERENCE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_SEQUENCE_FILE_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_SEQUENCE_FILE_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_SEQUENCE_FILE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_SEQUENCE_INDEX_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_SEQUENCE_INDEX_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_SEQUENCE_INDEX_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_STARTING_CODON_HELP;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_STARTING_CODON_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.PARAM_STARTING_CODON_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.FilteringSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.cli.parameters.HeaderCountFilteringParameters;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.gui.filtering.FilteringConfigurationTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class FilteringCommand extends SedaCommand {

  public static final StringOption OPTION_STARTING_CODON = new StringOption(
    PARAM_STARTING_CODON_NAME, PARAM_STARTING_CODON_SHORT_NAME, PARAM_STARTING_CODON_HELP, true, true, true
  );

  public static final FlagOption OPTION_NON_MULTIPLE_3 = new FlagOption(
    PARAM_NON_MULTIPLE_3_NAME, PARAM_NON_MULTIPLE_3_SHORT_NAME, PARAM_NON_MULTIPLE_3_HELP
  );

  public static final FlagOption OPTION_IN_FRAME_STOP_CODONS = new FlagOption(
    PARAM_IN_FRAME_STOP_CODONS_NAME, PARAM_IN_FRAME_STOP_CODONS_SHORT_NAME, PARAM_IN_FRAME_STOP_CODONS_HELP
  );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_MINIMUM_SEQUENCE_LENGTH = new IntegerDefaultValuedStringConstructedOption(
    PARAM_MINIMUM_SEQUENCE_LENGTH_NAME, PARAM_MINIMUM_SEQUENCE_LENGTH_SHORT_NAME, PARAM_MINIMUM_SEQUENCE_LENGTH_HELP, 0
  );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_MAXIMUM_SEQUENCE_LENGTH = new IntegerDefaultValuedStringConstructedOption(
    PARAM_MAXIMUM_SEQUENCE_LENGTH_NAME, PARAM_MAXIMUM_SEQUENCE_LENGTH_SHORT_NAME, PARAM_MAXIMUM_SEQUENCE_LENGTH_HELP, 0
  );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_MINIMUM_SEQUENCES = new IntegerDefaultValuedStringConstructedOption(
    PARAM_MINIMUM_SEQUENCES_NAME, PARAM_MINIMUM_SEQUENCES_SHORT_NAME, PARAM_MINIMUM_SEQUENCES_HELP, 1
  );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_MAXIMUM_SEQUENCES = new IntegerDefaultValuedStringConstructedOption(
    PARAM_MAXIMUM_SEQUENCES_NAME, PARAM_MAXIMUM_SEQUENCES_SHORT_NAME, PARAM_MAXIMUM_SEQUENCES_HELP, 0
  );

  public static final FlagOption OPTION_REMOVE_SIZE_DIFFERENCE = new FlagOption(
    PARAM_REMOVE_SIZE_DIFFERENCE_NAME, PARAM_REMOVE_SIZE_DIFFERENCE_SHORT_NAME, PARAM_REMOVE_SIZE_DIFFERENCE_HELP
  );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_MAX_SIZE_DIFFERENCE = new IntegerDefaultValuedStringConstructedOption(
    PARAM_MAX_SIZE_DIFFERENCE_NAME, PARAM_MAX_SIZE_DIFFERENCE_SHORT_NAME, PARAM_MAX_SIZE_DIFFERENCE_HELP, 10
  );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_SEQUENCE_INDEX = new IntegerDefaultValuedStringConstructedOption(
    PARAM_SEQUENCE_INDEX_NAME, PARAM_SEQUENCE_INDEX_SHORT_NAME, PARAM_SEQUENCE_INDEX_HELP, 1
  );

  public static final FileOption OPTION_SEQUENCE_FILE = new FileOption(
    PARAM_SEQUENCE_FILE_NAME, PARAM_SEQUENCE_FILE_SHORT_NAME, PARAM_SEQUENCE_FILE_HELP, true, true
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
  protected List<Option<?>> createSedaOptions() {
    final List<Option<?>> options = new ArrayList<>();
    options.add(OPTION_STARTING_CODON);
    options.add(OPTION_NON_MULTIPLE_3);
    options.add(OPTION_IN_FRAME_STOP_CODONS);
    options.add(OPTION_MINIMUM_SEQUENCE_LENGTH);
    options.add(OPTION_MAXIMUM_SEQUENCE_LENGTH);
    options.add(OPTION_MINIMUM_SEQUENCES);
    options.add(OPTION_MAXIMUM_SEQUENCES);
    options.add(OPTION_REMOVE_SIZE_DIFFERENCE);
    options.add(OPTION_MAX_SIZE_DIFFERENCE);
    options.add(OPTION_SEQUENCE_INDEX);
    options.add(OPTION_SEQUENCE_FILE);
    options.addAll(HeaderCountFilteringParameters.getOptionList());
    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    FilteringConfigurationTransformationProvider provider = new FilteringConfigurationTransformationProvider();

    if (parameters.hasOption(OPTION_STARTING_CODON)) {
      parameters.getAllValues(OPTION_STARTING_CODON).forEach(provider::addStartingCodon);
    }

    provider.setRemoveNonMultipleOfThree(parameters.hasFlag(OPTION_NON_MULTIPLE_3));
    provider.setRemoveIfInFrameStopCodon(parameters.hasFlag(OPTION_IN_FRAME_STOP_CODONS));
    provider.setMinSequenceLength(parameters.getSingleValue(OPTION_MINIMUM_SEQUENCE_LENGTH));
    provider.setMaxSequenceLength(parameters.getSingleValue(OPTION_MAXIMUM_SEQUENCE_LENGTH));
    provider.setMinNumOfSequences(parameters.getSingleValue(OPTION_MINIMUM_SEQUENCES));
    provider.setMaxNumOfSequences(parameters.getSingleValue(OPTION_MAXIMUM_SEQUENCES));

    if (parameters.hasFlag(OPTION_REMOVE_SIZE_DIFFERENCE)) {
      provider.setRemoveBySizeDifference(parameters.hasFlag(OPTION_REMOVE_SIZE_DIFFERENCE));
      provider.setSizeDifference(parameters.getSingleValue(OPTION_MAX_SIZE_DIFFERENCE));
      provider.setReferenceIndex(parameters.getSingleValue(OPTION_SEQUENCE_INDEX));

      if (parameters.hasOption(OPTION_SEQUENCE_FILE)) {
        File sequenceFile = parameters.getSingleValue(OPTION_SEQUENCE_FILE);

        if (!sequenceFile.isFile()) {
          formattedValidationError("Sequence file does not exist");
        }
        provider.setReferenceFile(sequenceFile);
      }
    }

    try {
      provider
        .setHeaderFilteringConfiguration(HeaderCountFilteringParameters.getHeaderFilteringConfiguration(parameters));
    } catch (IllegalArgumentException e) {
      formattedValidationError(e.getMessage());
    }

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<FilteringConfigurationTransformationProvider>()
      .read(parametersFile, FilteringConfigurationTransformationProvider.class);
  }
}
