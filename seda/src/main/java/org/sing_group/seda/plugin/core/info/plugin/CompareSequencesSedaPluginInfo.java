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
package org.sing_group.seda.plugin.core.info.plugin;

import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class CompareSequencesSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Compare";
  public static final String SHORT_NAME = "compare";
  public static final String DESCRIPTION =
    "Make all possible pairwise comparisons of selected files to find common and unique sequences.";
  public static final String GROUP = Group.GROUP_GENERAL.getName();

  public static final String PARAM_SEQUENCE_TARGET_NAME = "sequence-target";
  public static final String PARAM_SEQUENCE_TARGET_SHORT_NAME = "st";
  public static final String PARAM_SEQUENCE_TARGET_DESCRIPTION = "Sequence target";
  public static final String PARAM_SEQUENCE_TARGET_HELP =
    shortEnumString(
      "The part of the sequences that must be compared in order to perform the comparisons.", SequenceTarget.class
    );
  public static final String PARAM_SEQUENCE_TARGET_HELP_GUI = toHtml(PARAM_SEQUENCE_TARGET_HELP);
}
