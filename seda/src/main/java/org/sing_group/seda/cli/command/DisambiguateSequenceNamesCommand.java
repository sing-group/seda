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
import org.sing_group.seda.core.io.JsonObjectWriter;
import org.sing_group.seda.gui.disambiguate.DisambiguateSequenceNamesTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.DisambiguateSequenceNamesTransformation.Mode;

import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class DisambiguateSequenceNamesCommand extends SedaCommand {
  private static final String OPTION_RENAME_NAME = "rename";
  private static final String OPTION_REMOVE_NAME = "remove";

  public static final FlagOption OPTION_RENAME =
    new FlagOption(
      OPTION_RENAME_NAME, "rn",
      "[DEFAULT] Rename: add a numeric prefix to disambiguate duplicate identifiers."
    );

  public static final FlagOption OPTION_REMOVE =
    new FlagOption(
      OPTION_REMOVE_NAME, "rm",
      "Remove: remove sequences with duplicate identifiers, keeping the first occurrence."
    );

  @Override
  public String getName() {
    return "disambiguate";
  }

  @Override
  public String getDescriptiveName() {
    return "The method to disambiguate sequences.";
  }

  @Override
  public String getDescription() {
    return "The method to disambiguate sequences with duplicated identifiers.";
  }

  @Override
  public DisambiguateSequenceNamesTransformationProvider getTransformation(Parameters parameters) {
    DisambiguateSequenceNamesTransformationProvider provider = new DisambiguateSequenceNamesTransformationProvider();

    if (parameters.hasFlag(OPTION_REMOVE) && parameters.hasFlag(OPTION_RENAME)) {
      throw new IllegalArgumentException("Only one execution mode can be specified");
    }

    if (parameters.hasFlag(OPTION_REMOVE)) {
      provider.setMode(Mode.REMOVE);

    } else {
      provider.setMode(Mode.RENAME);
    }

    return provider;

  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    return asList(OPTION_RENAME, OPTION_REMOVE);
  }

  @Override
  protected DisambiguateSequenceNamesTransformationProvider getTransformation(File parametersFile) throws IOException {
    DisambiguateSequenceNamesTransformationProvider provider =
      loadTransformation(parametersFile);

    return provider;
  }

  protected DisambiguateSequenceNamesTransformationProvider loadTransformation(File file) throws IOException {
    return new JsonObjectReader<DisambiguateSequenceNamesTransformationProvider>()
      .read(file, DisambiguateSequenceNamesTransformationProvider.class);
  }

  @Override
  protected void saveTransformation(TransformationProvider provider, File file)
    throws IOException {
    new JsonObjectWriter<TransformationProvider>()
      .write(provider, file);
  }

}
