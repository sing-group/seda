/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
import java.awt.Window;

import org.sing_group.gc4s.dialog.AbstractInputJDialog;

public abstract class AbstractVisualizationDialog extends AbstractInputJDialog {
  private static final long serialVersionUID = 1L;

  protected AbstractVisualizationDialog(Window parent) {
    super(parent);
    this.configureDialog();
  }

  private void configureDialog() {
    this.setResizable(true);
    this.getDescriptionPane().setVisible(false);
    this.cancelButton.setVisible(false);
    this.okButton.setEnabled(true);
  }

  @Override
  protected String getDescription() {
    return "";
  }

  @Override
  public void setVisible(boolean b) {
    this.pack();
    super.setVisible(b);
  }
  
  public static void visualize(Window parent, Component component, String title) {
    new AbstractVisualizationDialog(parent) {
      private static final long serialVersionUID = 1L;

      @Override
      protected String getDialogTitle() {
        return title;
      }

      @Override
      protected Component getInputComponentsPane() {
        return component;
      }
    }.setVisible(true);
  }
}
