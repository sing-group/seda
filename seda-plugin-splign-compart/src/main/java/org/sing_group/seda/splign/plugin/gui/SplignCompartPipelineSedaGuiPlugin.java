/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.splign.plugin.gui;

import java.awt.Component;

import org.sing_group.seda.plugin.core.gui.AbstractSedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.splign.gui.SplignCompartPipelineTransformationConfigurationPanel;

public class SplignCompartPipelineSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private SplignCompartPipelineTransformationConfigurationPanel splignCompartPanel =
    new SplignCompartPipelineTransformationConfigurationPanel();

  @Override
  public String getName() {
    return "Splign/Compart Pipeline";
  }
  
  @Override
  public String getGroupName() {
    return GROUP_GENE_ANNOTATION;
  }

  @Override
  public Component getEditor() {
    return this.splignCompartPanel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.splignCompartPanel.getModel();
  }
}
