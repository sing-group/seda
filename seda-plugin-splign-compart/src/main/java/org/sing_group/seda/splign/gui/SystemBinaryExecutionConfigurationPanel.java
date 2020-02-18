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
package org.sing_group.seda.splign.gui;

import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;

import java.util.Optional;

import javax.swing.JOptionPane;

import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.gui.execution.AbstractSystemBinaryExecutionConfigurationPanel;
import org.sing_group.seda.splign.execution.DefaultSplignCompartBinariesExecutor;
import org.sing_group.seda.splign.execution.SplignCompartBinariesExecutor;

public class SystemBinaryExecutionConfigurationPanel extends AbstractSystemBinaryExecutionConfigurationPanel<SplignCompartBinariesExecutor> {
  private static final long serialVersionUID = 1L;

  private static final String HELP_SPLIGN_COMPART_PATH =
    "The directory that contains the Splign/Compart binaries." +
      "Leave it empty if they are in the path. Check the SEDA manual to see how to obtain them.";

  public SystemBinaryExecutionConfigurationPanel() {
    super(SelectionMode.DIRECTORIES, "Splign/Compart executables directory: ", HELP_SPLIGN_COMPART_PATH);
  }

  @Override
  protected void checkBinary() {
    try {
      getBinariesExecutor().get().checkBinary();
      showMessageDialog(
        getParentForDialogs(),
        "Splign/Compart binaries checked successfully.",
        "Check Splign/Compart binaries",
        JOptionPane.INFORMATION_MESSAGE
      );
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking Splign/Compart: " + e.getCommand() + ".",
        "Error checking Splign/Compart",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  @Override
  public Optional<SplignCompartBinariesExecutor> getBinariesExecutor() {
    return of(new DefaultSplignCompartBinariesExecutor(getSelectedBinaryPath()));
  }
}
