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
package org.sing_group.seda.plugin.core.info.common;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

import java.util.stream.Stream;

import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class HeaderFilteringInfo extends AbstractInfo {
  public static final String PARAM_USE_FILTER_NAME = "use-filter";
  public static final String PARAM_USE_FILTER_SHORT_NAME = "uf";
  public static final String PARAM_USE_FILTER_DESCRIPTION = "Use this filter";
  public static final String PARAM_USE_FILTER_HELP =
    "Filters sequences or files so that only those meeting the specified criteria regarding counts on their headers are kept.One of:"
      + Stream.of(HeaderFilteringConfiguration.FilterType.values()).map(HeaderFilteringConfiguration.FilterType::name)
        .map(String::toLowerCase).collect(joining(", ", "\n\t\t", "\n\t\t"));

  public static final String PARAM_HEADER_MODE_NAME = "header-mode";
  public static final String PARAM_HEADER_MODE_SHORT_NAME = "hm";
  public static final String PARAM_HEADER_MODE_DESCRIPTION = "Mode";
  public static final String PARAM_HEADER_MODE_HELP =
    "Whether sequences or files meeting the criteria should be removed or kept.";
  public static final String PARAM_HEADER_MODE_HELP_GUI = toHtml(PARAM_HEADER_MODE_HELP);

  public static final String PARAM_HEADER_LEVEL_NAME = "header-level";
  public static final String PARAM_HEADER_LEVEL_SHORT_NAME = "hl";
  public static final String PARAM_HEADER_LEVEL_DESCRIPTION = "Level";
  public static final String PARAM_HEADER_LEVEL_HELP =
    "Whether the filter should be applied to sequences or files. One of:"
      + Stream.of(HeaderFilteringConfiguration.Level.values()).map(HeaderFilteringConfiguration.Level::name)
        .map(String::toLowerCase).collect(joining(", ", "\n\t\t", "\n\t\t"));
  public static final String PARAM_HEADER_LEVEL_HELP_GUI = toHtml(PARAM_HEADER_LEVEL_HELP);

  public static final String PARAM_HEADER_RANGE_MIN_NAME = "header-range-min";
  public static final String PARAM_HEADER_RANGE_MIN_SHORT_NAME = "hrmin";
  public static final String PARAM_HEADER_RANGE_MIN_DESCRIPTION = "Min.";
  public static final String PARAM_HEADER_RANGE_MIN_HELP =
    "The minimum number of sequences that must contain the specified filter.";

  public static final String PARAM_HEADER_RANGE_MAX_NAME = "header-range-max";
  public static final String PARAM_HEADER_RANGE_MAX_SHORT_NAME = "hrmax";
  public static final String PARAM_HEADER_RANGE_MAX_DESCRIPTION = "Max.";
  public static final String PARAM_HEADER_RANGE_MAX_HELP =
    "The maximum number of sequences that must contain the specified filter.";

  public static final String PARAM_HEADER_FILTER_NAME = "header-filter";
  public static final String PARAM_HEADER_FILTER_SHORT_NAME = "hf";
  public static final String PARAM_HEADER_FILTER_DESCRIPTION = "Filter type";
  public static final String PARAM_HEADER_FILTER_HELP =
    "The sequence matching mode. One of: "
      + of(HeaderFilteringConfiguration.FilterType.values()).map(HeaderFilteringConfiguration.FilterType::name)
        .map(String::toLowerCase).collect(joining(", ", "", "."))
      + "\n\t\tSequence name means that the count is done by using sequence identifiers (or names)."
      + "\n\t\tRegular expression means that the count is done by matching headers using the regular expression specified below.\n\t\t";
  public static final String PARAM_HEADER_FILTER_HELP_GUI =
    toHtml(
      PARAM_HEADER_FILTER_HELP, asList("Sequence name", "Regular expression"),
      asList("Sequence name", "Regular expression"), true
    );
}
