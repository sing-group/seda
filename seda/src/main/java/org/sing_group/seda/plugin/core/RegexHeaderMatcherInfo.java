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
package org.sing_group.seda.plugin.core;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

import java.util.Collections;
import java.util.stream.Stream;

import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration;
import org.sing_group.seda.core.rename.HeaderTarget;

public class RegexHeaderMatcherInfo extends AbstractInfo {
  public static final String PARAM_SEQUENCE_MATCHING_NAME = "sequence-matching";
  public static final String PARAM_SEQUENCE_MATCHING_SHORT_NAME = "sm";
  public static final String PARAM_SEQUENCE_MATCHING_DESCRIPTION = "Sequence matching mode";
  public static final String PARAM_SEQUENCE_MATCHING_HELP =
    "The sequence matching mode. One of: "
      + of(HeaderFilteringConfiguration.FilterType.values()).map(HeaderFilteringConfiguration.FilterType::name)
        .map(String::toLowerCase).collect(joining(", ", "", "."))
      + "\n\t\tSequence name means that the sequences are concatenated if they have the same sequence names (identifiers).\n"
      + "\t\tRegular expression means sequences are concatenade by matching headers using the configuration specified below.\n\t\t";
  public static final String PARAM_SEQUENCE_MATCHING_HELP_GUI =
    toHtml(
      PARAM_SEQUENCE_MATCHING_HELP, asList("Sequence name", "Regular expression"),
      asList("Sequence name", "Regular expression"), true
    );
  public static final String PARAM_REGEX_STRING_NAME = "string-match";
  public static final String PARAM_REGEX_STRING_SHORT_NAME = "rs";
  public static final String PARAM_REGEX_STRING_DESCRIPTION = "String to match";
  public static final String PARAM_REGEX_STRING_HELP =
    "The regular expression that must be matched in the sequence header.";
  public static final String PARAM_REGEX_STRING_HELP_GUI = toHtml(PARAM_REGEX_STRING_HELP);

  public static final String PARAM_REGEX_QUOTE_PATTERN_NAME = "quote-pattern";
  public static final String PARAM_REGEX_QUOTE_PATTERN_SHORT_NAME = "rqp";
  public static final String PARAM_REGEX_QUOTE_PATTERN_DESCRIPTION = "Quote pattern";
  public static final String PARAM_REGEX_QUOTE_PATTERN_HELP =
    "Whether the regular expression pattern must be quoted or not. When the regular expression is quoted, metacharacters or escape sequences in it will be given no special meaning.";
  public static final String PARAM_REGEX_QUOTE_PATTERN_HELP_GUI = toHtml(PARAM_REGEX_QUOTE_PATTERN_HELP);

  public static final String PARAM_REGEX_GROUP_NAME = "regex-group";
  public static final String PARAM_REGEX_GROUP_SHORT_NAME = "rg";
  public static final String PARAM_REGEX_GROUP_DESCRIPTION = "Regex group";
  public static final String PARAM_REGEX_GROUP_HELP =
    "The regular expression group that must be extracted. Default value is 0, meaning that the entire result must be considered. Use values higher than 0 when there are brackets in the regular expression in order to select the desired group.";
  public static final String PARAM_REGEX_GROUP_HELP_GUI =
    toHtml(PARAM_REGEX_GROUP_HELP, asList("0"), Collections.emptyList(), true);

  public static final String PARAM_REGEX_CASE_SENSITIVE_NAME = "case-sensitive";
  public static final String PARAM_REGEX_CASE_SENSITIVE_SHORT_NAME = "rcs";
  public static final String PARAM_REGEX_CASE_SENSITIVE_DESCRIPTION = "Case sensitive";
  public static final String PARAM_REGEX_CASE_SENSITIVE_HELP =
    "Whether the string must be matched as case sensitive or not.";
  public static final String PARAM_REGEX_CASE_SENSITIVE_HELP_GUI =
    toHtml(PARAM_REGEX_CASE_SENSITIVE_HELP);

  public static final String PARAM_REGEX_HEADER_TARGET_NAME = "header-target";
  public static final String PARAM_REGEX_HEADER_TARGET_SHORT_NAME = "rht";
  public static final String PARAM_REGEX_HEADER_TARGET_DESCRIPTION = "Header target";
  public static final String PARAM_REGEX_HEADER_TARGET_HELP =
    "The part of the sequence header where the string must be found. "
      + "One of: "
      + Stream.of(HeaderTarget.values()).map(HeaderTarget::name).map(String::toLowerCase)
        .collect(joining(", ", "", "."));
  public static final String PARAM_REGEX_HEADER_TARGET_HELP_GUI =
    toHtml(PARAM_REGEX_HEADER_TARGET_HELP);
}
