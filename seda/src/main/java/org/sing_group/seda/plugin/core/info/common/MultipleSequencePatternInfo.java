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

import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class MultipleSequencePatternInfo extends AbstractInfo {
  public static final String PARAM_WITH_PATTERN_NAME = "with-pattern";
  public static final String PARAM_WITH_PATTERN_SHORT_NAME = "wp";
  public static final String PARAM_WITH_PATTERN_DESCRIPTION = "Pattern";
  public static final String PARAM_WITH_PATTERN_HELP =
    "A pattern (it can be regular expression) that must be present in the sequences.\n"
      + "\t\tIt can be configured adding 'config(group/case_sensitive/min_ocurrences)' before the pattern string, where:\n"
      + "\t\t\t- group (<number>): The group number of the pattern (default is 0). \n"
      + "\t\t\t- case_sensitive (<true/false>): Whether the regular expression must be applied as case sensitive or not (default is false). \n"
      + "\t\t\t- min_ocurrences (<Number>): The minimum number of occurrences that the pattern must be found (default is 1). \n"
      + "\t\tExample: --with-pattern config(1/true/2):<pattern_1_group_1>";

  public static final String PARAM_WITHOUT_PATTERN_NAME = "without-pattern";
  public static final String PARAM_WITHOUT_PATTERN_SHORT_NAME = "wop";
  public static final String PARAM_WITHOUT_PATTERN_DESCRIPTION = "Pattern";
  public static final String PARAM_WITHOUT_PATTERN_HELP =
    "A pattern (it can be regular expression) that is not allowed be present in the sequences.\n"
      + "\t\tIt can be configured adding 'config(group/case_sensitive/min_ocurrences)' before the pattern string, where:\n"
      + "\t\t\t- group (<number>): The group number of the pattern (default is 0). \n"
      + "\t\t\t- case_sensitive (<true/false>): Whether the regular expression must be applied as case sensitive or not (default is false). \n"
      + "\t\t\t- min_ocurrences (<Number>): The minimum number of occurrences that the pattern must be found (default is 1). \n"
      + "\t\tExample: --without-pattern config(1/true/2):<pattern_1_group_1>";

  public static final String PARAM_GROUP_MODE_NAME = "group-mode";
  public static final String PARAM_GROUP_MODE_SHORT_NAME = "gm";
  public static final String PARAM_GROUP_MODE_DESCRIPTION = "Group mode";
  public static final String PARAM_GROUP_MODE_HELP =
    "Select the mode to group the sequence patterns and/or groups.\n"
      + "\t\tWhen no groups are specified, all patterns are added to the same group in 'any' mode, which means that only one pattern of the "
      + "group must be present to obtain a match. Using '--group-mode all' means that all patterns must be present at the same time to obtain a match.\n"
      + "\t\tWhen patterns are assigned into groups, the default group mode of each pattern is 'any'. Using '--group-mode <group_number>:all' "
      + "changes this behaviour. At the same time, all groups are grouped in 'any' mode, which means that only one of the groups must make a match to "
      + "obtain a global match. Using '--group-mode all' means that all pattern groups must make a match at the same time to obtain a global match.\n\t\t";
}
