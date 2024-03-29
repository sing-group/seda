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

import static java.lang.System.getProperty;

import java.io.File;

import javax.swing.JFileChooser;

import org.sing_group.seda.util.SedaProperties;

public class CommonFileChooser {

  private static final JFileChooser DEFAULT_FILECHOOSER = new JFileChooser(getInitialInputDirectory()) {
    private static final long serialVersionUID = 1L;

    @Override
    public void setSelectedFile(File file) {
      if (file != null) {
        super.setSelectedFile(file);
      }
    };
  };

  private static CommonFileChooser INSTANCE = null;
  private JFileChooser filechooser;

  private CommonFileChooser() {
    filechooser = DEFAULT_FILECHOOSER;
  }

  private synchronized static void createInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CommonFileChooser();
    }
  }

  public static CommonFileChooser getInstance() {
    if (INSTANCE == null) {
      createInstance();
    }
    return INSTANCE;
  }

  public JFileChooser getFilechooser() {
    return filechooser;
  }

  public void setFilechooser(JFileChooser filechooser) {
    this.filechooser = filechooser;
  }

  private static String getInitialInputDirectory() {
    return getProperty(SedaProperties.PROPERTY_INPUT_DIRECTORY, getProperty("user.home"));
  }
}
