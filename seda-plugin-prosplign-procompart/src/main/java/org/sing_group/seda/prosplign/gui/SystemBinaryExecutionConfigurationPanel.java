/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
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
package org.sing_group.seda.prosplign.gui;

import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.sing_group.gc4s.input.filechooser.SelectionMode.DIRECTORIES;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.PARAM_LOCAL_MODE_HELP_GUI;

import java.util.Optional;

import javax.swing.JOptionPane;

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.gui.execution.AbstractSystemBinaryExecutionConfigurationPanel;
import org.sing_group.seda.prosplign.execution.DefaultProSplignCompartBinariesExecutor;
import org.sing_group.seda.prosplign.execution.ProSplignCompartBinariesExecutor;

public class SystemBinaryExecutionConfigurationPanel extends AbstractSystemBinaryExecutionConfigurationPanel<ProSplignCompartBinariesExecutor> {
  private static final long serialVersionUID = 1L;

  public SystemBinaryExecutionConfigurationPanel() {
    super(DIRECTORIES, "ProSplign/ProCompart executables directory: ", PARAM_LOCAL_MODE_HELP_GUI);
  }

  @Override
  protected void checkBinary() {
    try {
      getBinariesExecutor().get().checkBinary();
      showMessageDialog(
        getParentForDialogs(),
        "ProSplign/ProCompart binaries checked successfully.",
        "Check ProSplign/ProCompart binaries",
        JOptionPane.INFORMATION_MESSAGE
      );
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking ProSplign/ProCompart: " + e.getCommand() + ".",
        "Error checking ProSplign/ProCompart",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  @Override
  public Optional<ProSplignCompartBinariesExecutor> getBinariesExecutor() {
    return of(new DefaultProSplignCompartBinariesExecutor(getSelectedBinaryPath()));
  }
}
