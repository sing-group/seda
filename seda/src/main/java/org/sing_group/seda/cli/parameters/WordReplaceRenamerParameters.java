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
package org.sing_group.seda.cli.parameters;

import static java.util.Arrays.asList;
import static org.sing_group.seda.cli.SedaCommand.checkMandatoryOption;
import static org.sing_group.seda.plugin.core.WordReplaceInfo.PARAM_REGEX_HELP;
import static org.sing_group.seda.plugin.core.WordReplaceInfo.PARAM_REGEX_NAME;
import static org.sing_group.seda.plugin.core.WordReplaceInfo.PARAM_REGEX_SHORT_NAME;
import static org.sing_group.seda.plugin.core.WordReplaceInfo.PARAM_REPLACEMENT_HELP;
import static org.sing_group.seda.plugin.core.WordReplaceInfo.PARAM_REPLACEMENT_NAME;
import static org.sing_group.seda.plugin.core.WordReplaceInfo.PARAM_REPLACEMENT_SHORT_NAME;
import static org.sing_group.seda.plugin.core.WordReplaceInfo.PARAM_TARGET_WORDS_HELP;
import static org.sing_group.seda.plugin.core.WordReplaceInfo.PARAM_TARGET_WORDS_NAME;
import static org.sing_group.seda.plugin.core.WordReplaceInfo.PARAM_TARGET_WORDS_SHORT_NAME;

import java.util.List;

import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.WordReplaceRenamer;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class WordReplaceRenamerParameters {
  public static final StringOption OPTION_TARGET_WORDS = new StringOption(
    PARAM_TARGET_WORDS_NAME, PARAM_TARGET_WORDS_SHORT_NAME, PARAM_TARGET_WORDS_HELP, true, true, true
  );

  public static final FlagOption OPTION_REGEX = new FlagOption(
    PARAM_REGEX_NAME, PARAM_REGEX_SHORT_NAME, PARAM_REGEX_HELP
  );

  public static final DefaultValuedStringOption OPTION_REPLACEMENT = new DefaultValuedStringOption(
    PARAM_REPLACEMENT_NAME, PARAM_REPLACEMENT_SHORT_NAME, PARAM_REPLACEMENT_HELP, ""
  );

  public static List<Option<?>> getOptionList() {
    return asList(OPTION_TARGET_WORDS, OPTION_REGEX, OPTION_REPLACEMENT);
  }

  public static HeaderRenamer getHeaderRenamer(HeaderTarget headerTarget, Parameters parameters)
    throws IllegalArgumentException {

    checkMandatoryOption(parameters, OPTION_TARGET_WORDS);

    List<String> targetWordsList = parameters.getAllValues(OPTION_TARGET_WORDS);
    boolean isRegex = parameters.hasFlag(OPTION_REGEX);
    String replacement = parameters.getSingleValue(OPTION_REPLACEMENT);

    return new WordReplaceRenamer(headerTarget, replacement, isRegex, targetWordsList);
  }
}
