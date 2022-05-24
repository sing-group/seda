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

import static java.util.stream.Collectors.joining;

import java.util.stream.Stream;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.gui.rename.RenameHeaderTransformationConfigurationPanel;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class RenameHeaderSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Rename header";
  public static final String SHORT_NAME = "rename-header";
  public static final String DESCRIPTION = "Modify sequence headers by adding, deleting or replacing information.";
  public static final String GROUP = Group.GROUP_REFORMATTING.getName();

  public static final String PARAM_TARGET_NAME = "header-target";
  public static final String PARAM_TARGET_SHORT_NAME = "ht";
  public static final String PARAM_TARGET_DESCRIPTION = "Target";
  public static final String PARAM_TARGET_HELP =
    "The header target. One of: " + Stream.of(HeaderTarget.values()).map(HeaderTarget::name).map(String::toLowerCase)
      .collect(joining(", ", "", "."));
  public static final String PARAM_TARGET_HELP_GUI = toHtml(PARAM_TARGET_HELP);

  public static final String PARAM_RENAME_TYPE_NAME = "rename-type";
  public static final String PARAM_RENAME_TYPE_SHORT_NAME = "rt";
  public static final String PARAM_RENAME_TYPE_DESCRIPTION = "Rename type";
  public static final String PARAM_RENAME_TYPE_HELP =
    "One of: " + Stream.of(RenameHeaderTransformationConfigurationPanel.Rename.values())
      .map(RenameHeaderTransformationConfigurationPanel.Rename::name).map(String::toLowerCase)
      .collect(joining(", ", "", "."));
  public static final String PARAM_RENAME_TYPE_HELP_GUI = toHtml(PARAM_RENAME_TYPE_HELP);
}
