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

import static java.util.stream.Collectors.joining;

import java.util.stream.Stream;

import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;

public class ReallocateReferenceSequencesSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Reallocate reference sequences";
  public static final String SHORT_NAME = "reallocate";
  public static final String DESCRIPTION =
    "Find one or more sequences (i.e. the reference sequences) using a pattern filtering option and reallocate them at the beginning of the file.";

  public static final String PARAM_SEQUENCE_TARGET_NAME = "sequence-target";
  public static final String PARAM_SEQUENCE_TARGET_SHORT_NAME = "st";
  public static final String PARAM_SEQUENCE_TARGET_DESCRIPTION = "Sequence Target";
  public static final String PARAM_SEQUENCE_TARGET_HELP =
    "One of: " + Stream.of(SequenceTarget.values()).map(SequenceTarget::name).map(String::toLowerCase)
      .collect(joining(", ", "", "."));
  public static final String PARAM_SEQUENCE_TARGET_HELP_GUI = toHtml(PARAM_SEQUENCE_TARGET_HELP);

  public static final String PARAM_WITH_PATTERN_NAME = "with-pattern";
  public static final String PARAM_WITH_PATTERN_SHORT_NAME = "wp";
  public static final String PARAM_WITH_PATTERN_DESCRIPTION = "Pattern";
  public static final String PARAM_WITH_PATTERN_HELP =
    "Contains the pattern in the sequence. Accept Config. \n" +
      "\t\tConfig structure: config(group/case_sensitive/min_ocurrences) where:\n" +
      "\t\t\tgroup: Group number. \n" +
      "\t\t\tcase_sensitive: <true/false> Whether the regular expression must be applied as case sensitive or not. \n" +
      "\t\t\tmin_ocurrences: <Number> The minimum number of occurrences that the pattern must be found. \n" +
      "\t\t\tExample: --with-pattern config(1/true/2):<pattern_1_group_1>";

  public static final String PARAM_WITHOUT_PATTERN_NAME = "without-pattern";
  public static final String PARAM_WITHOUT_PATTERN_SHORT_NAME = "wop";
  public static final String PARAM_WITHOUT_PATTERN_DESCRIPTION = "Pattern";
  public static final String PARAM_WITHOUT_PATTERN_HELP =
    "Not contains the pattern in the sequence. Accept Config.\n" +
      "\t\tConfig structure: config(group/case_sensitive/min_ocurrences) where:\n" +
      "\t\t\tgroup: Group number. \n" +
      "\t\t\tcase_sensitive: <true/false> Whether the regular expression must be applied as case sensitive or not. \n" +
      "\t\t\tmin_ocurrences: <Number> The minimum number of occurrences that the pattern must be found. \n" +
      "\t\t\tExample: --with-pattern config(1/true/2):<pattern_1_group_1>";

  public static final String PARAM_GROUP_MODE_NAME = "group-mode";
  public static final String PARAM_GROUP_MODE_SHORT_NAME = "gm";
  public static final String PARAM_GROUP_MODE_DESCRIPTION = "Group mode";
  public static final String PARAM_GROUP_MODE_HELP =
    "Select the mode to group the sequences. Accept config to set mode on each group.\n" +
      "\t\tConfig structure: <group_number>:<mode> where:\n" +
      "\t\t\tgroup_number: <Number> The group number. \n" +
      "\t\t\tmode: "
      + Stream.of(EvaluableSequencePattern.GroupMode.values()).map(EvaluableSequencePattern.GroupMode::name)
        .map(String::toLowerCase)
        .collect(joining("/", "<", ">"))
      + " The mode to apply the patterns on the group. \n" +
      "\t\t\tExample: --group-mode 1:ALL --group-mode 2:ANY";
}
