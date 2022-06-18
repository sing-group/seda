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

import static org.sing_group.seda.gui.isoforms.DefaultSequenceIsoformConfigurationPanel.DEFAULT_REFERENCE_SIZE;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.cli.parameters.RegexHeaderMatcherParameters;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector;
import org.sing_group.seda.core.rename.EmptySequenceHeadersJoiner;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.SequenceHeadersJoiner;
import org.sing_group.seda.gui.isoforms.RemoveIsoformsTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class RemoveIsoformsCommand extends SedaCommand {

  public static final IntegerDefaultValuedStringConstructedOption OPTION_MINIMUM_WORD_LENGH =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_MINIMUM_WORD_LENGTH_NAME, PARAM_MINIMUM_WORD_LENGTH_SHORT_NAME, PARAM_MINIMUM_WORD_LENGTH_HELP, 250
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_REFERENCE_SIZE =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_REFERENCE_SIZE_NAME, PARAM_REFERENCE_SIZE_SHORT_NAME, PARAM_REFERENCE_SIZE_HELP, DEFAULT_REFERENCE_SIZE
    );

  public static final DefaultValuedStringOption OPTION_TIE_BREAK =
    new DefaultValuedStringOption(
      PARAM_TIE_BREAK_NAME, PARAM_TIE_BREAK_SHORT_NAME, PARAM_TIE_BREAK_HELP,
      DEFAULT_REMOVE_ISOFORM_TIE_BREAK_OPTION.name().toLowerCase()
    );

  public static final FlagOption OPTION_GROUP_SEQUENCES_REGEX =
    new FlagOption(
      PARAM_GROUP_SEQUENCES_REGEX_NAME, PARAM_GROUP_SEQUENCES_REGEX_SHORT_NAME, PARAM_GROUP_SEQUENCES_REGEX_HELP
    );

  public static final FlagOption OPTION_REMOVE_ISOFORM_HEADER =
    new FlagOption(
      PARAM_REMOVE_ISOFORM_HEADER_NAME, PARAM_REMOVE_ISOFORM_HEADER_SHORT_NAME, PARAM_REMOVE_ISOFORM_HEADER_HELP
    );

  public static final DefaultValuedStringOption OPTION_HEADER_TARGET =
    new DefaultValuedStringOption(
      PARAM_HEADER_TARGET_NAME, PARAM_HEADER_TARGET_SHORT_NAME, PARAM_HEADER_TARGET_HELP,
      DEFAULT_REMOVE_ISOFORM_HEADER_TARGET.name().toLowerCase()
    );

  public static final FileOption OPTION_FILE_NAME =
    new FileOption(
      PARAM_ISOFORM_FILE_NAME, PARAM_ISOFORM_FILE_SHORT_NAME, PARAM_ISOFORM_FILE_HELP, true, true
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

    optionList.add(OPTION_MINIMUM_WORD_LENGH);
    optionList.add(OPTION_REFERENCE_SIZE);
    optionList.add(OPTION_TIE_BREAK);
    optionList.add(OPTION_REMOVE_ISOFORM_HEADER);
    optionList.add(OPTION_HEADER_TARGET);
    optionList.add(OPTION_FILE_NAME);
    optionList.add(OPTION_GROUP_SEQUENCES_REGEX);
    optionList.addAll(RegexHeaderMatcherParameters.getOptionList());

    return optionList;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    RemoveIsoformsTransformationProvider provider = new RemoveIsoformsTransformationProvider();

    provider.setMinimumWordLength(parameters.getSingleValue(OPTION_MINIMUM_WORD_LENGH));

    provider.setIsoformSelector(getIsoformSelector(parameters));

    if (parameters.hasFlag(OPTION_GROUP_SEQUENCES_REGEX)) {
      try {
        provider.setHeaderMatcher(RegexHeaderMatcherParameters.getRegexHeaderMatcher(parameters));
      } catch (IllegalArgumentException e) {
        formattedValidationError(e.getMessage());
      }
    }

    provider.setSequenceHeaderJoiner(getSequenceHeadersJoiner(parameters));

    if (parameters.hasOption(OPTION_FILE_NAME)) {
      File isoformsFilesDirectory = parameters.getSingleValue(OPTION_FILE_NAME);

      if (!isoformsFilesDirectory.isDirectory()) {
        formattedValidationError(formatParam(OPTION_FILE_NAME) + " must be a directory");
      }

      provider.setRemovedIsoformFilesDirectory(isoformsFilesDirectory);
    }

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<RemoveIsoformsTransformationProvider>()
      .read(parametersFile, RemoveIsoformsTransformationProvider.class);
  }

  private DefaultSequenceIsoformSelector getIsoformSelector(Parameters parameters) {
    DefaultSequenceIsoformSelector.TieBreakOption tieBreak = null;
    try {
      tieBreak =
        DefaultSequenceIsoformSelector.TieBreakOption
          .valueOf(parameters.getSingleValue(OPTION_TIE_BREAK).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_TIE_BREAK);
    }

    return new DefaultSequenceIsoformSelector(parameters.getSingleValue(OPTION_REFERENCE_SIZE), tieBreak);
  }

  public SequenceHeadersJoiner getSequenceHeadersJoiner(Parameters parameters) {
    if (parameters.hasFlag(OPTION_REMOVE_ISOFORM_HEADER)) {
      HeaderTarget headerTarget = null;
      try {
        headerTarget = HeaderTarget.valueOf(parameters.getSingleValue(OPTION_HEADER_TARGET).toUpperCase());
      } catch (IllegalArgumentException e) {
        invalidEnumValue(OPTION_HEADER_TARGET);
      }
      return new SequenceHeadersJoiner(
        headerTarget, DEFAULT_REMOVE_ISOFORM_HEADER_DELIMITER, DEFAULT_REMOVE_ISOFORM_HEADER_PREFIX,
        DEFAULT_REMOVE_ISOFORM_HEADER_SUFFIX
      );
    } else {
      return new EmptySequenceHeadersJoiner();
    }
  }
}
