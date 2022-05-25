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

import java.util.Collections;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class RegexHeaderMatcherInfo extends AbstractInfo {
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
    shortEnumString("The part of the sequence header where the string must be found. ", HeaderTarget.class);
  public static final String PARAM_REGEX_HEADER_TARGET_HELP_GUI =
    toHtml(PARAM_REGEX_HEADER_TARGET_HELP);
}
