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

import static org.sing_group.seda.plugin.core.info.plugin.TranslateSequenceNamesSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.TranslateSequenceNamesSedaPluginInfo.GROUP;
import static org.sing_group.seda.plugin.core.info.plugin.TranslateSequenceNamesSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.TranslateSequenceNamesSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.cli.parameters.SequenceTranslationCliParameters;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.provider.translation.TranslateSequencesTransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class TranslateSequencesCommand extends SedaCommand {
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
    this.sequenceTranslationCliParameters = new SequenceTranslationCliParameters(false, false, "");

    return this.sequenceTranslationCliParameters.getOptionList();
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    TranslateSequencesTransformationProvider provider = new TranslateSequencesTransformationProvider();

    try {
      provider.setTranslationConfiguration(
        this.sequenceTranslationCliParameters.getSequenceTranslationConfiguration(parameters)
      );
    } catch (IllegalArgumentException e) {
      formattedValidationError(e.getMessage() + ".");
    }

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<TranslateSequencesTransformationProvider>()
      .read(parametersFile, TranslateSequencesTransformationProvider.class);
  }
}
