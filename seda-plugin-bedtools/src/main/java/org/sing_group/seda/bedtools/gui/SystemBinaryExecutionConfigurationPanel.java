/*
 * #%L
 * SEquence DAtaset builder bedtools plugin
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
package org.sing_group.seda.bedtools.gui;

import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

import javax.swing.JOptionPane;

import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.bedtools.execution.BedToolsEnvironment;
import org.sing_group.seda.bedtools.execution.DefaultBedToolsBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.gui.execution.AbstractSystemBinaryExecutionConfigurationPanel;

public class SystemBinaryExecutionConfigurationPanel extends AbstractSystemBinaryExecutionConfigurationPanel<BedToolsBinariesExecutor> {
  private static final long serialVersionUID = 1L;

  private static final String HELP_BEDTOOLS_PATH =
    "<html>The bedtools binary file.<br/> If the bedtools binary is in the path, then "
      + "this can be empty and the <i>Check binary</i> would say that it is right.</html>";

  public SystemBinaryExecutionConfigurationPanel() {
    super(SelectionMode.FILES, "Bedtools executable path: ", HELP_BEDTOOLS_PATH);
  }

  @Override
  protected void checkBinary() {
    try {
      getBinariesExecutor().get().checkBinary();
      showMessageDialog(
        getParentForDialogs(),
        "Bedtools checked successfully.",
        "Check bedtools",
        JOptionPane.INFORMATION_MESSAGE
      );
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking bedtools: " + e.getCommand() + ".",
        "Error checking bedtools",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  @Override
  public Optional<BedToolsBinariesExecutor> getBinariesExecutor() {
    return of(new DefaultBedToolsBinariesExecutor(getBedToolsPath()));
  }

  private File getBedToolsPath() {
    File selectedBinaryPath = this.getSelectedBinaryPath();
    if (selectedBinaryPath != null) {
      return selectedBinaryPath;
    } else {
      return Paths.get(BedToolsEnvironment.getInstance().getBedToolsCommand()).toFile();
    }
  }
}
