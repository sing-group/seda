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

import static org.sing_group.seda.plugin.core.info.plugin.PatternFilteringSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.PatternFilteringSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.PatternFilteringSedaPluginInfo.PARAM_SEQUENCE_TARGET_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.PatternFilteringSedaPluginInfo.PARAM_SEQUENCE_TARGET_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.PatternFilteringSedaPluginInfo.PARAM_SEQUENCE_TARGET_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.PatternFilteringSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.cli.parameters.MultipleSequencePatternCliParameters;
import org.sing_group.seda.cli.parameters.SequenceTranslationSedaParameters;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.gui.pattern.PatternFilteringTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class PatternFilteringCommand extends SedaCommand {

  public static final DefaultValuedStringOption OPTION_SEQUENCE_TARGET =
    new DefaultValuedStringOption(
      PARAM_SEQUENCE_TARGET_NAME, PARAM_SEQUENCE_TARGET_SHORT_NAME, PARAM_SEQUENCE_TARGET_HELP,
      SequenceTarget.SEQUENCE.toString().toLowerCase()
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
    List<Option<?>> optionList = new ArrayList<>();

    optionList.add(OPTION_SEQUENCE_TARGET);
    optionList.addAll(MultipleSequencePatternCliParameters.getOptionList());
    optionList.addAll(SequenceTranslationSedaParameters.getOptionList());

    return optionList;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    PatternFilteringTransformationProvider provider =
      new PatternFilteringTransformationProvider();

    SequenceTarget headerTarget = null;
    try {
      headerTarget = SequenceTarget.valueOf(parameters.getSingleValue(OPTION_SEQUENCE_TARGET).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_SEQUENCE_TARGET);
    }
    provider.setTarget(headerTarget);

    SequenceTranslationSedaParameters translationParameters = new SequenceTranslationSedaParameters(parameters);
    if (headerTarget.isSequence() && translationParameters.hasConvertAminoAcid()) {
      try {
        provider.setTranslationConfiguration(translationParameters.getSequenceTranslationConfiguration());
      } catch (IllegalArgumentException e) {
        formattedValidationError(e.getMessage());
      }
    }

    MultipleSequencePatternCliParameters patternParameters = new MultipleSequencePatternCliParameters(parameters);

    try {
      provider.setPattern(patternParameters.getSequencePatternGroup());
    } catch (IllegalArgumentException e) {
      formattedValidationError(e.getMessage());
    }

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<PatternFilteringTransformationProvider>()
      .read(parametersFile, PatternFilteringTransformationProvider.class);
  }

}
