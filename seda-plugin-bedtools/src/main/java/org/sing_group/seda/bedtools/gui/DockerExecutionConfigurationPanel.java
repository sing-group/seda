/*
 * #%L
 * SEquence DAtaset builder bedtools plugin
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.sing_group.seda.bedtools.execution.DockerBedToolsBinariesExecutor.getDefaultDockerImage;

import java.util.Optional;

import javax.swing.JOptionPane;

import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.bedtools.execution.DockerBedToolsBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.gui.execution.AbstractDockerExecutionConfigurationPanel;

public class DockerExecutionConfigurationPanel
  extends AbstractDockerExecutionConfigurationPanel<BedToolsBinariesExecutor> {
  private static final long serialVersionUID = 1L;

  private static final String HELP_BEDTOOLS_PATH =
    "<html>The bedtools docker image.<br/> By default, the official SEDA image for bedtools is used.<br/>" +
      "If you provide a custom image, it should have the bedtools command available in the path.</html>";

  public DockerExecutionConfigurationPanel() {
    super(getDefaultDockerImage(), HELP_BEDTOOLS_PATH);
  }

  @Override
  protected void checkBinary() {
    try {
      Optional<BedToolsBinariesExecutor> executor = getBinariesExecutor();
      if (executor.isPresent()) {
        getBinariesExecutor().get().checkBinary();
        showMessageDialog(
          getParentForDialogs(),
          "Bedtools successfully.",
          "Check bedtools ",
          JOptionPane.INFORMATION_MESSAGE
        );
      }
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
    String selectedDockerImage = getSelectedDockerImage();
    if (selectedDockerImage.isEmpty()) {
      return empty();
    }

    return of(new DockerBedToolsBinariesExecutor(selectedDockerImage));
  }
}
