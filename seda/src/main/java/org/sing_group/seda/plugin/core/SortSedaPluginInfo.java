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

import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.SequenceTarget;

public class SortSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Sort";
  public static final String SHORT_NAME = "sort";
  public static final String DESCRIPTION = "Sort sequences by sequence length or sequence header (alphabetically).";

  public static final String PARAM_SORT_ON_NAME = "sort-on";
  public static final String PARAM_SORT_ON_SHORT_NAME = "so";
  public static final String PARAM_SORT_ON_DESCRIPTION = "Sort on";
  public static final String PARAM_SORT_ON_HELP =
    "Whether sort must be applied on sequence headers or sequences themselves. One of: " + Stream
      .of(SequenceTarget.values()).map(SequenceTarget::name).map(String::toLowerCase).collect(joining(", ", "", "."));
  public static final String PARAM_SORT_ON_HELP_GUI = toHtml(PARAM_SORT_ON_HELP);

  public static final String PARAM_DESCENDING_NAME = "descending";
  public static final String PARAM_DESCENDING_SHORT_NAME = "des";
  public static final String PARAM_DESCENDING_DESCRIPTION = "Descending";
  public static final String PARAM_DESCENDING_HELP = "Use this option to sort in descending order.";
  public static final String PARAM_DESCENDING_HELP_GUI = toHtml(PARAM_DESCENDING_HELP);

  public static final String PARAM_CRITERIA_NAME = "criteria";
  public static final String PARAM_CRITERIA_SHORT_NAME = "c";
  public static final String PARAM_CRITERIA_DESCRIPTION = "Criteria";
  public static final String PARAM_CRITERIA_HELP =
    "The sort criteria to be applied. One of: " + Stream.of(SequenceComparator.values()).map(SequenceComparator::name)
      .map(String::toLowerCase).collect(joining(", ", "", "."));
  public static final String PARAM_CRITERIA_HELP_GUI = toHtml(PARAM_CRITERIA_HELP);
}
