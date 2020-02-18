/*
 * #%L
 * SEquence DAtaset builder Blast plugin
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
package org.sing_group.seda.blast.gui;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;

import java.util.Optional;

import javax.swing.JOptionPane;

import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DockerBlastBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.gui.execution.AbstractDockerExecutionConfigurationPanel;

public class DockerExecutionConfigurationPanel
  extends AbstractDockerExecutionConfigurationPanel<BlastBinariesExecutor> {
  private static final long serialVersionUID = 1L;

  private static final String HELP_BLAST_PATH =
    "<html>The BLAST docker image.<br/> By default, the official SEDA image for BLAST is used.<br/>"
      + "If you provide a custom image, it should have the BLAST commands available in the path.</html>";

  public DockerExecutionConfigurationPanel() {
    super(DockerBlastBinariesExecutor.getDefaultDockerImage(), HELP_BLAST_PATH);
  }

  @Override
  protected void checkBinary() {
    try {
      Optional<BlastBinariesExecutor> executor = getBinariesExecutor();
      if (executor.isPresent()) {
        getBinariesExecutor().get().checkBinary();
        showMessageDialog(
          getParentForDialogs(),
          "Blast checked successfully.",
          "Check Blast",
          JOptionPane.INFORMATION_MESSAGE
        );
      }
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking Blast: " + e.getCommand() + ".",
        "Error checking Blast",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  @Override
  public Optional<BlastBinariesExecutor> getBinariesExecutor() {
    String selectedDockerImage = getSelectedDockerImage();
    if (selectedDockerImage.isEmpty()) {
      return empty();
    }

    return of(new DockerBlastBinariesExecutor(selectedDockerImage));
  }
}
