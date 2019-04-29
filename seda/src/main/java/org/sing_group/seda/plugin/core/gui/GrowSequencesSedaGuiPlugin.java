/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.grow.GrowSequencesConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class GrowSequencesSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final GrowSequencesConfigurationPanel panel;

  public GrowSequencesSedaGuiPlugin() {
    this.panel = new GrowSequencesConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Grow sequences";
  }
  
  @Override
  public String getGroupName() {
    return GROUP_GENERAL;
  }

  @Override
  public Component getEditor() {
    return this.panel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.panel.getModel();
  }
}
