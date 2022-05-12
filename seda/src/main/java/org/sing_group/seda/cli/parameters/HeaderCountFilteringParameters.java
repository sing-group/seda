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

import static org.sing_group.seda.cli.SedaCommand.invalidEnumValue;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_FILTER_HELP;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_FILTER_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_FILTER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_LEVEL_HELP;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_LEVEL_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_LEVEL_SHORT_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_MODE_HELP;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_MODE_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_RANGE_MAX_HELP;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_RANGE_MAX_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_RANGE_MAX_SHORT_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_RANGE_MIN_HELP;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_RANGE_MIN_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_HEADER_RANGE_MIN_SHORT_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_USE_FILTER_HELP;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_USE_FILTER_NAME;
import static org.sing_group.seda.plugin.core.HeaderFilteringInfo.PARAM_USE_FILTER_SHORT_NAME;

import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.rename.HeaderTarget;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class HeaderCountFilteringParameters {

  public static final FlagOption OPTION_USE_FILTER =
    new FlagOption(PARAM_USE_FILTER_NAME, PARAM_USE_FILTER_SHORT_NAME, PARAM_USE_FILTER_HELP);
  public static final DefaultValuedStringOption OPTION_MODE =
    new DefaultValuedStringOption(
      PARAM_HEADER_MODE_NAME, PARAM_HEADER_MODE_SHORT_NAME, PARAM_HEADER_MODE_HELP,
      HeaderFilteringConfiguration.Mode.KEEP.toString().toLowerCase()
    );

  public static final DefaultValuedStringOption OPTION_LEVEL =
    new DefaultValuedStringOption(
      PARAM_HEADER_LEVEL_NAME, PARAM_HEADER_LEVEL_SHORT_NAME, PARAM_HEADER_LEVEL_HELP,
      HeaderFilteringConfiguration.Level.SEQUENCE.name().toLowerCase()
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_RANGE_MIN =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_HEADER_RANGE_MIN_NAME, PARAM_HEADER_RANGE_MIN_SHORT_NAME, PARAM_HEADER_RANGE_MIN_HELP, 0
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_RANGE_MAX =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_HEADER_RANGE_MAX_NAME, PARAM_HEADER_RANGE_MAX_SHORT_NAME, PARAM_HEADER_RANGE_MAX_HELP, 10
    );

  public static final DefaultValuedStringOption OPTION_FILTER =
    new DefaultValuedStringOption(
      PARAM_HEADER_FILTER_NAME, PARAM_HEADER_FILTER_SHORT_NAME, PARAM_HEADER_FILTER_HELP,
      HeaderFilteringConfiguration.FilterType.SEQUENCE_NAME.name().toLowerCase()
    );

  public static HeaderFilteringConfiguration getHeaderFilteringConfiguration(Parameters parameters)
    throws IllegalArgumentException {

    boolean useFilter = parameters.hasFlag(OPTION_USE_FILTER);
    int min = parameters.getSingleValue(OPTION_RANGE_MIN);
    int max = parameters.getSingleValue(OPTION_RANGE_MAX);

    HeaderFilteringConfiguration.Mode mode = null;
    HeaderFilteringConfiguration.Level level = null;
    HeaderFilteringConfiguration.FilterType filterType = null;

    String filterString = null;
    boolean quotePattern = false;
    int regexGroup = 0;
    boolean caseSensitive = false;
    HeaderTarget headerTarget = HeaderTarget.ALL;

    try {
      mode = HeaderFilteringConfiguration.Mode.valueOf(parameters.getSingleValue(OPTION_MODE).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_MODE);
    }

    try {
      level = HeaderFilteringConfiguration.Level.valueOf(parameters.getSingleValue(OPTION_LEVEL).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_LEVEL);
    }

    try {
      filterType =
        HeaderFilteringConfiguration.FilterType.valueOf(parameters.getSingleValue(OPTION_FILTER).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_FILTER);
    }

    if (HeaderFilteringConfiguration.FilterType.REGEX.equals(filterType)) {
      RegexHeaderMatcher regexHeaderMatcher = RegexHeaderMatcherParameters.getRegexHeaderMatcher(parameters);
      filterString = regexHeaderMatcher.getString();
      quotePattern = regexHeaderMatcher.getRegexConfig().isQuotePattern();
      regexGroup = regexHeaderMatcher.getRegexConfig().getGroup();
      caseSensitive = regexHeaderMatcher.getRegexConfig().isCaseSensitive();
      headerTarget = regexHeaderMatcher.getHeaderTarget();

    }

    return new HeaderFilteringConfiguration(
      useFilter, mode, level, min, max, filterType, filterString, quotePattern, regexGroup, caseSensitive, headerTarget
    );
  }

  public static List<Option<?>> getOptionList() {
    final List<Option<?>> options = new ArrayList<>();
    options.add(OPTION_USE_FILTER);
    options.add(OPTION_MODE);
    options.add(OPTION_LEVEL);
    options.add(OPTION_RANGE_MIN);
    options.add(OPTION_RANGE_MAX);
    options.add(OPTION_FILTER);
    options.addAll(RegexHeaderMatcherParameters.getOptionList());
    return options;
  }
}
