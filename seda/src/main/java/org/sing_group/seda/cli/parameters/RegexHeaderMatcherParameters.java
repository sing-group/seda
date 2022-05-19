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
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_CASE_SENSITIVE_HELP;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_CASE_SENSITIVE_NAME;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_CASE_SENSITIVE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_GROUP_HELP;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_GROUP_NAME;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_GROUP_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_HEADER_TARGET_HELP;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_HEADER_TARGET_NAME;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_HEADER_TARGET_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_QUOTE_PATTERN_HELP;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_QUOTE_PATTERN_NAME;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_QUOTE_PATTERN_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_STRING_HELP;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_STRING_NAME;
import static org.sing_group.seda.plugin.core.info.common.RegexHeaderMatcherInfo.PARAM_REGEX_STRING_SHORT_NAME;

import java.util.List;

import org.sing_group.seda.core.filtering.RegexConfiguration;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.rename.HeaderTarget;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class RegexHeaderMatcherParameters {

  public static final DefaultValuedStringOption OPTION_STRING =
    new DefaultValuedStringOption(
      PARAM_REGEX_STRING_NAME, PARAM_REGEX_STRING_SHORT_NAME, PARAM_REGEX_STRING_HELP, ".*"
    );

  public static final FlagOption OPTION_QUOTE_PATTERN =
    new FlagOption(
      PARAM_REGEX_QUOTE_PATTERN_NAME, PARAM_REGEX_QUOTE_PATTERN_SHORT_NAME, PARAM_REGEX_QUOTE_PATTERN_HELP
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_GROUP =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_REGEX_GROUP_NAME, PARAM_REGEX_GROUP_SHORT_NAME, PARAM_REGEX_GROUP_HELP, 0
    );

  public static final FlagOption OPTION_CASE_SENSITIVE =
    new FlagOption(
      PARAM_REGEX_CASE_SENSITIVE_NAME, PARAM_REGEX_CASE_SENSITIVE_SHORT_NAME, PARAM_REGEX_CASE_SENSITIVE_HELP
    );

  public static final DefaultValuedStringOption OPTION_HEADER_TARGET =
    new DefaultValuedStringOption(
      PARAM_REGEX_HEADER_TARGET_NAME, PARAM_REGEX_HEADER_TARGET_SHORT_NAME, PARAM_REGEX_HEADER_TARGET_HELP,
      HeaderTarget.NAME.name().toLowerCase()
    );

  public static List<Option<?>> getOptionList() {
    return asList(
      OPTION_STRING,
      OPTION_QUOTE_PATTERN,
      OPTION_GROUP,
      OPTION_CASE_SENSITIVE,
      OPTION_HEADER_TARGET
    );
  }

  public static RegexHeaderMatcher getRegexHeaderMatcher(Parameters parameters) {
    if (!parameters.hasOption(OPTION_STRING)) {
      throw new IllegalArgumentException("String option is mandatory");
    }

    String regex = parameters.getSingleValueString(OPTION_STRING);

    if (regex.isEmpty()) {
      throw new IllegalArgumentException("String option cant be empty");
    }

    HeaderTarget headerTarget = null;
    try {
      headerTarget = HeaderTarget.valueOf(parameters.getSingleValueString(OPTION_HEADER_TARGET).toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
        "Invalid value for " + PARAM_REGEX_HEADER_TARGET_NAME + " (" + PARAM_REGEX_HEADER_TARGET_HELP + ")"
      );
    }

    RegexConfiguration regexConfiguration =
      new RegexConfiguration(
        parameters.hasFlag(OPTION_CASE_SENSITIVE), parameters.getSingleValue(OPTION_GROUP),
        parameters.hasFlag(OPTION_QUOTE_PATTERN)
      );

    return new RegexHeaderMatcher(regex, headerTarget, regexConfiguration);
  }
}
