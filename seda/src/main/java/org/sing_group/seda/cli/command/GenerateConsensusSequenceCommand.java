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

import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_CONSENSUS_BASE_HELP;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_CONSENSUS_BASE_NAME;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_CONSENSUS_BASE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_MINIMUM_PRESENCE_HELP;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_MINIMUM_PRESENCE_NAME;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_MINIMUM_PRESENCE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_SEQUENCE_TYPE_HELP;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_SEQUENCE_TYPE_NAME;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_SEQUENCE_TYPE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_VERBOSE_HELP;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_VERBOSE_NAME;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.PARAM_VERBOSE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.GenerateConsensusSequenceSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.bio.SequenceType;
import org.sing_group.seda.bio.consensus.ConsensusBaseStrategy;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.gui.consensus.GenerateConsensusSequenceTransformationProvider;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.BigDecimalDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class GenerateConsensusSequenceCommand extends ReformatFastaCommand {

  public static final DefaultValuedStringOption OPTION_SEQUENCE_TYPE =
    new DefaultValuedStringOption(
      PARAM_SEQUENCE_TYPE_NAME, PARAM_SEQUENCE_TYPE_SHORT_NAME, PARAM_SEQUENCE_TYPE_HELP,
      SequenceType.NUCLEOTIDE.name().toLowerCase()
    );

  public static final DefaultValuedStringOption OPTION_CONSENSUS_BASE =
    new DefaultValuedStringOption(
      PARAM_CONSENSUS_BASE_NAME, PARAM_CONSENSUS_BASE_SHORT_NAME, PARAM_CONSENSUS_BASE_HELP,
      ConsensusBaseStrategy.MOST_FREQUENT.name().toLowerCase()
    );

  public static final BigDecimalDefaultValuedStringConstructedOption OPTION_MINIMUM_PRESENCE =
    new BigDecimalDefaultValuedStringConstructedOption(
      PARAM_MINIMUM_PRESENCE_NAME, PARAM_MINIMUM_PRESENCE_SHORT_NAME,
      PARAM_MINIMUM_PRESENCE_HELP, BigDecimal.valueOf(0.5)
    );

  public static final FlagOption OPTION_VERBOSE =
    new FlagOption(PARAM_VERBOSE_NAME, PARAM_VERBOSE_SHORT_NAME, PARAM_VERBOSE_HELP);

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

    options.add(OPTION_SEQUENCE_TYPE);
    options.add(OPTION_CONSENSUS_BASE);
    options.add(OPTION_MINIMUM_PRESENCE);
    options.add(OPTION_VERBOSE);
    options.addAll(super.createSedaOptions());

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    GenerateConsensusSequenceTransformationProvider provider = new GenerateConsensusSequenceTransformationProvider();

    SequenceType sequenceType = null;
    ConsensusBaseStrategy consensusBaseStrategy = null;

    List<String> errorList = new ArrayList<>();

    try {
      sequenceType = SequenceType.valueOf(parameters.getSingleValueString(OPTION_SEQUENCE_TYPE).toUpperCase());
    } catch (IllegalArgumentException e) {
      errorList.add("Invalid value for " + PARAM_SEQUENCE_TYPE_NAME + " (" + PARAM_SEQUENCE_TYPE_HELP + ")");
    }

    try {
      consensusBaseStrategy =
        ConsensusBaseStrategy.valueOf(parameters.getSingleValueString(OPTION_CONSENSUS_BASE).toUpperCase());
    } catch (IllegalArgumentException e) {
      errorList.add("Invalid value for " + PARAM_CONSENSUS_BASE_NAME + " (" + PARAM_CONSENSUS_BASE_HELP + ")");
    }

    if (!errorList.isEmpty()) {
      formattedValidationErrors(errorList);
    }

    provider.setSequenceType(sequenceType);
    provider.setConsensusBaseStrategy(consensusBaseStrategy);
    provider.setMinimumPresence(parameters.getSingleValue(OPTION_MINIMUM_PRESENCE).doubleValue());
    provider.setVerbose(parameters.hasFlag(OPTION_VERBOSE));

    provider.setReformatFastaTransformationProvider(
      (ReformatFastaTransformationProvider) super.getTransformation(parameters)
    );

    return provider;
  }

  @Override
  protected GenerateConsensusSequenceTransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<GenerateConsensusSequenceTransformationProvider>()
      .read(parametersFile, GenerateConsensusSequenceTransformationProvider.class);
  }
}
