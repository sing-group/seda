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
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.GROUP;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_FRAGMENT_LENGHT_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_FRAGMENT_LENGHT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_FRAGMENT_LENGHT_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_LINE_BREAKS_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_LINE_BREAKS_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_LINE_BREAKS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_REMOVE_LINE_BREAKS_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_REMOVE_LINE_BREAKS_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_REMOVE_LINE_BREAKS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_SEQUENCE_CASE_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_SEQUENCE_CASE_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_SEQUENCE_CASE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.datatype.SequenceCase;
import org.sing_group.seda.io.LineBreakType;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.provider.reformat.ReformatFastaTransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class ReformatFastaCommand extends SedaCommand {

  public static final FlagOption OPTION_REMOVE_LINE_BREAKS =
    new FlagOption(
      PARAM_REMOVE_LINE_BREAKS_NAME,
      PARAM_REMOVE_LINE_BREAKS_SHORT_NAME,
      PARAM_REMOVE_LINE_BREAKS_HELP
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_FRAGMENT_LENGHT =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_FRAGMENT_LENGHT_NAME,
      PARAM_FRAGMENT_LENGHT_SHORT_NAME,
      PARAM_FRAGMENT_LENGHT_HELP, 80
    );

  public static final DefaultValuedStringOption OPTION_LINE_BREAKS =
    new DefaultValuedStringOption(
      PARAM_LINE_BREAKS_NAME, PARAM_LINE_BREAKS_SHORT_NAME,
      PARAM_LINE_BREAKS_HELP, LineBreakType.defaultType().name()
    );

  public static final DefaultValuedStringOption OPTION_SEQUENCE_CASE =
    new DefaultValuedStringOption(
      PARAM_SEQUENCE_CASE_NAME, PARAM_SEQUENCE_CASE_SHORT_NAME,
      PARAM_SEQUENCE_CASE_HELP, SequenceCase.defaultType().name()
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
      OPTION_REMOVE_LINE_BREAKS,
      OPTION_FRAGMENT_LENGHT,
      OPTION_LINE_BREAKS,
      OPTION_SEQUENCE_CASE
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    ReformatFastaTransformationProvider provider = new ReformatFastaTransformationProvider();

    LineBreakType lineBreakType = null;
    SequenceCase sequenceCase = null;

    try {
      lineBreakType = LineBreakType.valueOf(parameters.getSingleValueString(OPTION_LINE_BREAKS).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_LINE_BREAKS);
    }

    try {
      sequenceCase = SequenceCase.valueOf(parameters.getSingleValueString(OPTION_SEQUENCE_CASE).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_SEQUENCE_CASE);
    }

    provider.setRemoveLineBreaks(parameters.hasFlag(OPTION_REMOVE_LINE_BREAKS));
    provider.setFragmentLength(parameters.getSingleValue(OPTION_FRAGMENT_LENGHT));
    provider.setLineBreakType(lineBreakType);
    provider.setSequenceCase(sequenceCase);

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<ReformatFastaTransformationProvider>()
      .read(parametersFile, ReformatFastaTransformationProvider.class);
  }
}
