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
package org.sing_group.seda.blast.gui;

import java.awt.Component;

import org.sing_group.gc4s.visualization.VisualizationUtils;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;

public class BlastTransformationConfigurationPanelTest {
  
  public static void main(String[] args) {
    VisualizationUtils.showComponent(getBlastTransformationConfigurationPanel());
  }

  private static Component getBlastTransformationConfigurationPanel() {
    BlastTransformationConfigurationPanel panel = new BlastTransformationConfigurationPanel();
    panel.getModel().addTransformationChangeListener(new TransformationChangeListener() {
      
      @Override
      public void onTransformationChange(TransformationChangeEvent event) {
        System.err.println("Transformation changed: " + event.getType() + ". Is valid configuration? " + panel.getModel().isValidTransformation());
      }
    });
    return panel;
  }
}
