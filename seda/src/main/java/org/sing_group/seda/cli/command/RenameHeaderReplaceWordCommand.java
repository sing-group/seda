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

import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REGEX_HELP;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REGEX_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REGEX_SHORT_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REPLACEMENT_HELP;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REPLACEMENT_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REPLACEMENT_SHORT_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.PARAM_TARGET_WORDS_HELP;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.PARAM_TARGET_WORDS_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.PARAM_TARGET_WORDS_SHORT_NAME;
import static org.sing_group.seda.plugin.core.RenameHeaderReplaceWordSedaPluginInfo.SHORT_NAME;

import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.WordReplaceRenamer;
import org.sing_group.seda.gui.rename.RenameHeaderTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class RenameHeaderReplaceWordCommand extends RenameHeaderCommand {
  public static final StringOption OPTION_TARGET_WORDS =
    new StringOption(
      PARAM_TARGET_WORDS_NAME, PARAM_TARGET_WORDS_SHORT_NAME, PARAM_TARGET_WORDS_HELP, true, true, true
    );

  public static final FlagOption OPTION_REGEX =
    new FlagOption(
      PARAM_REGEX_NAME, PARAM_REGEX_SHORT_NAME, PARAM_REGEX_HELP
    );

  public static final DefaultValuedStringOption OPTION_REPLACEMENT =
    new DefaultValuedStringOption(
      PARAM_REPLACEMENT_NAME, PARAM_REPLACEMENT_SHORT_NAME, PARAM_REPLACEMENT_HELP, ""
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

    options.add(OPTION_TARGET_WORDS);
    options.add(OPTION_REGEX);
    options.add(OPTION_REPLACEMENT);
    options.addAll(super.createSedaOptions());

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    RenameHeaderTransformationProvider provider = new RenameHeaderTransformationProvider();
    checkMandatoryOption(parameters, OPTION_TARGET_WORDS);

    List<String> targetWordsList = parameters.getAllValues(OPTION_TARGET_WORDS);
    boolean isRegex = parameters.hasFlag(OPTION_REGEX);
    String replacement = parameters.getSingleValue(OPTION_REPLACEMENT);

    HeaderTarget headerTarget = getHeaderTarget(parameters);

    provider.setHeaderRenamer(new WordReplaceRenamer(headerTarget, replacement, isRegex, targetWordsList));

    return provider;
  }
}
