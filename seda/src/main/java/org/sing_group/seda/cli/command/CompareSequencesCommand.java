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

import static org.sing_group.seda.plugin.core.info.plugin.CompareSequencesSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.CompareSequencesSedaPluginInfo.GROUP;
import static org.sing_group.seda.plugin.core.info.plugin.CompareSequencesSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.CompareSequencesSedaPluginInfo.PARAM_SEQUENCE_TARGET_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.CompareSequencesSedaPluginInfo.PARAM_SEQUENCE_TARGET_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.CompareSequencesSedaPluginInfo.PARAM_SEQUENCE_TARGET_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.CompareSequencesSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.provider.compare.CompareSequencesGroupDatasetTransformationProvider;
import org.sing_group.seda.transformation.provider.reformat.ReformatFastaTransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class CompareSequencesCommand extends ReformatFastaCommand {

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
  protected String getSedaGroup() {
    return GROUP;
  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    final List<Option<?>> options = new ArrayList<>();

    options.add(OPTION_SEQUENCE_TARGET);
    options.addAll(super.createSedaOptions());

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    CompareSequencesGroupDatasetTransformationProvider provider =
      new CompareSequencesGroupDatasetTransformationProvider();

    SequenceTarget sequenceTarget = null;
    try {
      sequenceTarget = SequenceTarget.valueOf(parameters.getSingleValue(OPTION_SEQUENCE_TARGET).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_SEQUENCE_TARGET);
    }
    provider.setSequenceTarget(sequenceTarget);
    provider.setReformatFastaTransformationProvider(
      (ReformatFastaTransformationProvider) super.getTransformation(parameters)
    );

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<CompareSequencesGroupDatasetTransformationProvider>()
      .read(parametersFile, CompareSequencesGroupDatasetTransformationProvider.class);
  }
}
