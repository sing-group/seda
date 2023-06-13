/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.gui.execution;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_DOCKER_MODE_HELP_GUI;

import java.awt.BorderLayout;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.gui.execution.AbstractDockerExecutionConfigurationPanel;
import org.sing_group.seda.sapp.execution.DefaultDockerSappCommands;
import org.sing_group.seda.sapp.execution.DockerSappBinariesExecutor;
import org.sing_group.seda.sapp.execution.DockerSappCommands;
import org.sing_group.seda.sapp.execution.SappBinariesExecutor;

public class DockerExecutionConfigurationPanel
  extends AbstractDockerExecutionConfigurationPanel<SappBinariesExecutor> {

  private static final long serialVersionUID = 1L;

  private DockerSappCommandsConfigurationPanel sappCommandsConfigurationPanel;

  private static final String SAPP_DEFAULT_DOCKER_IMAGE;
  private static final String SAPP_DEFAULT_DOCKER_SAPP_JARS_PATH;

  static {
    DockerSappCommands sappCommands = new DefaultDockerSappCommands();
    SAPP_DEFAULT_DOCKER_IMAGE = sappCommands.dockerImage();
    SAPP_DEFAULT_DOCKER_SAPP_JARS_PATH = sappCommands.jarsPath().replace("\\", "/");
  }

  public DockerExecutionConfigurationPanel() {
    super(SAPP_DEFAULT_DOCKER_IMAGE, PARAM_DOCKER_MODE_HELP_GUI);

    this.init();
  }

  private void init() {
    this.sappCommandsConfigurationPanel =
      new DockerSappCommandsConfigurationPanel("", SAPP_DEFAULT_DOCKER_SAPP_JARS_PATH);

    this.sappCommandsConfigurationPanel.setControlsEnabled(false);
    this.add(this.sappCommandsConfigurationPanel, BorderLayout.CENTER);
    this.dockerImage.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        dockerImageChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        dockerImageChanged();
      }
    });
  }

  private void dockerImageChanged() {
    boolean isDefaultDockerImage = isDefaultDockerImage();

    this.sappCommandsConfigurationPanel.setControlsEnabled(!isDefaultDockerImage);
    if (isDefaultDockerImage) {
      this.sappCommandsConfigurationPanel.setSappCommands(new DefaultDockerSappCommands());
    }
  }

  private boolean isDefaultDockerImage() {
    return this.getSelectedDockerImage().equals(SAPP_DEFAULT_DOCKER_IMAGE);
  }

  @Override
  protected void checkBinary() {
    try {
      Optional<SappBinariesExecutor> executor = getBinariesExecutor();
      if (executor.isPresent()) {
        getBinariesExecutor().get().checkBinary();
        showMessageDialog(
          getParentForDialogs(),
          "SAPP jars checked successfully.",
          "Check SAPP jars",
          JOptionPane.INFORMATION_MESSAGE
        );
      }
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking SAPP jars: " + e.getCommand() + ".",
        "Error checking SAPP jars",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  @Override
  public Optional<SappBinariesExecutor> getBinariesExecutor() {
    String selectedDockerImage = getSelectedDockerImage();
    if (selectedDockerImage.isEmpty()) {
      return empty();
    }

    return of(new DockerSappBinariesExecutor(dockerSappCommands(selectedDockerImage)));
  }

  private DockerSappCommands dockerSappCommands(String selectedDockerImage) {
    if (isDefaultDockerImage()) {
      return new DefaultDockerSappCommands();
    } else {
      if (this.sappCommandsConfigurationPanel.selectedJavaPath().isPresent()) {
        return new DefaultDockerSappCommands(
          this.sappCommandsConfigurationPanel.selectedJavaPath().get() + "/java",
          this.sappCommandsConfigurationPanel.conversionJarPath(),
          this.sappCommandsConfigurationPanel.geneCallerJarPath(),
          selectedDockerImage
        );
      } else {
        return new DefaultDockerSappCommands(
          this.sappCommandsConfigurationPanel.conversionJarPath(),
          this.sappCommandsConfigurationPanel.geneCallerJarPath(),
          selectedDockerImage
        );
      }
    }
  }

  public void setSappCommands(DockerSappCommands dockerSappCommands) {
    this.setSelectedDockerImage(dockerSappCommands.dockerImage());
    this.sappCommandsConfigurationPanel.setSappCommands(dockerSappCommands);
  }
}
