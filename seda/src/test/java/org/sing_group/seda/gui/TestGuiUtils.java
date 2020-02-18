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
package org.sing_group.seda.gui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;

public class TestGuiUtils {

  public static final TransformationChangeListener TRANSFORMATION_CHANGE_LISTENER =
    new TransformationChangeListener() {

      @Override
      public void onTransformationChange(TransformationChangeEvent event) {
        System.err.println(
          "Transformation changed: " + event.getType() + ". Is valid? " + event.getProvider().isValidTransformation()
        );
      }
    };

  public static final PropertyChangeListener PROPERTY_CHANGE_LISTENER = new PropertyChangeListener() {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      System.err.println("Property changed:");
      System.err.println("\t- " + evt.getPropertyName());
      System.err.println("\t- " + evt.getNewValue());
    }
  };

  public static final void showComponent(Component component) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(component);
    frame.pack();
    frame.setVisible(true);
  }
}
