/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.gui.filtering;

import org.sing_group.seda.gui.TestGuiUtils;
import org.sing_group.seda.gui.configuration_panel.filtering.FilteringConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;

public class FilteringConfigurationPanelTest {

  public static void main(String[] args) {
    FilteringConfigurationPanel panel = new FilteringConfigurationPanel();
    panel.getTransformationProvider().addTransformationChangeListener(new TransformationChangeListener() {
      
      @Override
      public void onTransformationChange(TransformationChangeEvent event) {
        System.err.println(
          "Changed. Is valid configuration? " + panel.getTransformationProvider().validate().isValid()
        );
      }
    });
    TestGuiUtils.showComponent(panel);
  }
}
