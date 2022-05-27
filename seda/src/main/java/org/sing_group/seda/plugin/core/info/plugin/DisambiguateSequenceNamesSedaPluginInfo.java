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

import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;
import org.sing_group.seda.transformation.sequencesgroup.DisambiguateSequenceNamesTransformation;

public class DisambiguateSequenceNamesSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Disambiguate sequence names";
  public static final String SHORT_NAME = "disambiguate";
  public static final String DESCRIPTION =
    "Disambiguate duplicated sequence identifiers by adding a prefix or removing sequences.";
  public static final String GROUP = Group.GROUP_REFORMATTING.getName();

  public static final String PARAM_MODE_NAME = "mode";
  public static final String PARAM_MODE_SHORT_NAME = "m";
  public static final String PARAM_MODE_DESCRIPTION = "Disambiguation mode";
  private static final String[] PARAM_MODE_HELP_ENUM = { 
    "Add a numeric prefix to disambiguate duplicate identifiers.",
    "Remove sequences with duplicate identifiers, keeping the first occurrence."
  };
  public static final String PARAM_MODE_HELP_BASE = "The method to disambiguate sequences with duplicated identifiers.";
  public static final String PARAM_MODE_HELP = longEnumStringForCli(
    PARAM_MODE_HELP_BASE, cliMap(DisambiguateSequenceNamesTransformation.Mode.values(), PARAM_MODE_HELP_ENUM)
  );
  public static final String PARAM_MODE_HELP_GUI = toHtml(
    longEnumStringForGui(
      PARAM_MODE_HELP_BASE, guiMap(DisambiguateSequenceNamesTransformation.Mode.values(), PARAM_MODE_HELP_ENUM)
    )
  );
}
