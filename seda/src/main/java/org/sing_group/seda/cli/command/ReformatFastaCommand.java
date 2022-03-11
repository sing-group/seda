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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.datatype.SequenceCase;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.io.LineBreakType;
import org.sing_group.seda.plugin.core.ReformatFastaSedaPluginInfo;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValueBooleanOption;
import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class ReformatFastaCommand extends SedaCommand {

  public static final String OPTION_REMOVE_LINE_BREAKS_NAME = ReformatFastaSedaPluginInfo.PARAM_REMOVE_LINE_BREAKS_NAME;
  public static final String OPTION_FRAGMENT_LENGHT_NAME = ReformatFastaSedaPluginInfo.PARAM_FRAGMENT_LENGHT_NAME;
  public static final String OPTION_LINE_BREAKS_NAME = ReformatFastaSedaPluginInfo.PARAM_LINE_BREAKS_NAME;
  public static final String OPTION_SEQUENCE_CASE_NAME = ReformatFastaSedaPluginInfo.PARAM_SEQUENCE_CASE_NAME;

  public static final DefaultValueBooleanOption OPTION_REMOVE_LINE_BREAKS =
    new DefaultValueBooleanOption(
      ReformatFastaSedaPluginInfo.PARAM_REMOVE_LINE_BREAKS_NAME,
      ReformatFastaSedaPluginInfo.PARAM_REMOVE_LINE_BREAKS_SHORT_NAME,
      ReformatFastaSedaPluginInfo.PARAM_REMOVE_LINE_BREAKS_DESCRIPTION, false
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_FRAGMENT_LENGHT =
    new IntegerDefaultValuedStringConstructedOption(
      ReformatFastaSedaPluginInfo.PARAM_FRAGMENT_LENGHT_NAME,
      ReformatFastaSedaPluginInfo.PARAM_FRAGMENT_LENGHT_SHORT_NAME,
      ReformatFastaSedaPluginInfo.PARAM_FRAGMENT_LENGHT_DESCRIPTION, 80
    );

  public static final DefaultValuedStringOption OPTION_LINE_BREAKS =
    new DefaultValuedStringOption(
      ReformatFastaSedaPluginInfo.PARAM_LINE_BREAKS_NAME, ReformatFastaSedaPluginInfo.PARAM_LINE_BREAKS_SHORT_NAME,
      ReformatFastaSedaPluginInfo.PARAM_LINE_BREAKS_DESCRIPTION, LineBreakType.defaultType().getLineBreak()
    );

  public static final DefaultValuedStringOption OPTION_SEQUENCE_CASE =
    new DefaultValuedStringOption(
      ReformatFastaSedaPluginInfo.PARAM_SEQUENCE_CASE_NAME, ReformatFastaSedaPluginInfo.PARAM_SEQUENCE_CASE_SHORT_NAME,
      ReformatFastaSedaPluginInfo.PARAM_SEQUENCE_CASE_HELP, SequenceCase.defaultType().name()
    );

  @Override
  public String getName() {
    return ReformatFastaSedaPluginInfo.SHORT_NAME;
  }

  @Override
  public String getDescriptiveName() {
    return ReformatFastaSedaPluginInfo.NAME;
  }

  @Override
  public String getDescription() {
    return ReformatFastaSedaPluginInfo.DESCRIPTION;
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

    provider.setRemoveLineBreaks(parameters.getSingleValue(OPTION_REMOVE_LINE_BREAKS));
    provider.setFragmentLength(parameters.getSingleValue(OPTION_FRAGMENT_LENGHT));

    try {
      provider.setLineBreakType(
        LineBreakType.forString(parameters.getSingleValueString(OPTION_LINE_BREAKS))
      );
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid line break for the transformation.");
    }

    try {
      provider.setSequenceCase(
        SequenceCase.valueOf(parameters.getSingleValueString(OPTION_SEQUENCE_CASE).toUpperCase())
      );
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid sequence case for the transformation.");
    }

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<ReformatFastaTransformationProvider>()
      .read(parametersFile, ReformatFastaTransformationProvider.class);
  }
}
