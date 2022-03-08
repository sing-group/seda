/*
 * #%L
 * SEquence DAtaset builder Clustal Omega plugin
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
package org.sing_group.seda.clustalomega.gui;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.sing_group.seda.clustalomega.execution.DockerClustalOmegaBinariesExecutor.getDefaultDockerImage;

import java.util.Optional;

import javax.swing.JOptionPane;

import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DockerClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.gui.execution.AbstractDockerExecutionConfigurationPanel;

public class DockerExecutionConfigurationPanel
  extends AbstractDockerExecutionConfigurationPanel<ClustalOmegaBinariesExecutor> {
  private static final long serialVersionUID = 1L;

  private static final String HELP_CLUSTAL_OMEGA_PATH = ClustalOmegaAlignmentSedaPluginInfo.PARAM_DOCKER_MODE_HELP_GUI;

  public DockerExecutionConfigurationPanel() {
    super(getDefaultDockerImage(), HELP_CLUSTAL_OMEGA_PATH);
  }

  @Override
  protected void checkBinary() {
    try {
      Optional<ClustalOmegaBinariesExecutor> executor = getBinariesExecutor();
      if (executor.isPresent()) {
        getBinariesExecutor().get().checkBinary();
        showMessageDialog(
          getParentForDialogs(),
          "Clustal Omega checked successfully.",
          "Check Clustal Omega",
          JOptionPane.INFORMATION_MESSAGE
        );
      }
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking Clustal Omega: " + e.getCommand() + ".",
        "Error checking Clustal Omega",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  @Override
  public Optional<ClustalOmegaBinariesExecutor> getBinariesExecutor() {
    String selectedDockerImage = getSelectedDockerImage();
    if (selectedDockerImage.isEmpty()) {
      return empty();
    }

    return of(new DockerClustalOmegaBinariesExecutor(selectedDockerImage));
  }
}
