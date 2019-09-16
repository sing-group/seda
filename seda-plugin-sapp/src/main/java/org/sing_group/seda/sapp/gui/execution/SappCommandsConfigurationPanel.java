/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.sapp.execution.DefaultSappCommands;
import org.sing_group.seda.sapp.execution.SappCommands;
import org.sing_group.seda.sapp.execution.SappEnvironment;

public class SappCommandsConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String JAVA_PATH_PROPERTY = "commands.javapath";
  private static final String JAVA_PATH_LABEL = "Java path: ";
  private static final String JAVA_PATH_HELP = "The path to the directory that contains the java executable.";

  public static final String SAPP_JARS_PATH_PROPERTY = "commands.sappjarspath";
  private static final String SAPP_JARS_PATH_LABEL = "SAPP jars path: ";
  private static final String SAPP_JARS_PATH_HELP = "The path to the directory that contains the SAPP jar files.";

  private InputParametersPanel parametersPanel;
  private JFileChooserPanel javaPath;
  private JFileChooserPanel sappJarsPath;

  private File selectedJavaPath;
  private File selectedSappJarsPath;

  public SappCommandsConfigurationPanel() {
    this("", "");
  }

  public SappCommandsConfigurationPanel(String javaPath, String sappJarsPath) {
    this.init(javaPath, sappJarsPath);
  }

  private void init(String javaPath, String sappJarsPath) {
    this.setLayout(new BorderLayout());
    this.add(new CenteredJPanel(getParametersPanel(javaPath, sappJarsPath)));
  }

  private Component getParametersPanel(String javaPath, String sappJarsPath) {
    if (this.parametersPanel == null) {
      this.parametersPanel = new InputParametersPanel(getParameters(javaPath, sappJarsPath));
    }
    return this.parametersPanel;
  }

  private InputParameter[] getParameters(String javaPath, String sappJarsPath) {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getJavaPathParameter(javaPath));
    parameters.add(getSappJarsPathParameter(sappJarsPath));

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getJavaPathParameter(String javaPath) {
    this.javaPath =
      JFileChooserPanelBuilder
        .createOpenJFileChooserPanel()
        .withFileChooser(new JFileChooser())
        .withFileChooserSelectionMode(SelectionMode.DIRECTORIES)
        .withLabel("")
        .build();

    if (!javaPath.isEmpty()) {
      this.javaPath.setSelectedFile(new File(javaPath));
    }
    this.javaPath.addFileChooserListener(this::javaPathChanged);

    return new InputParameter(JAVA_PATH_LABEL, this.javaPath, JAVA_PATH_HELP);
  }

  private void javaPathChanged(ChangeEvent event) {
    File newValue = this.javaPath.getSelectedFile();
    this.firePropertyChange(JAVA_PATH_PROPERTY, this.javaPath, newValue);
    this.selectedJavaPath = newValue;
  }

  public Optional<File> selectedJavaPath() {
    return Optional.ofNullable(this.selectedJavaPath);
  }

  private InputParameter getSappJarsPathParameter(String sappJarsPath) {
    this.sappJarsPath =
      JFileChooserPanelBuilder
        .createOpenJFileChooserPanel()
        .withFileChooser(new JFileChooser())
        .withFileChooserSelectionMode(SelectionMode.DIRECTORIES)
        .withLabel("")
        .build();

    if (!sappJarsPath.isEmpty()) {
      this.sappJarsPath.setSelectedFile(new File(sappJarsPath));
    }
    this.sappJarsPath.addFileChooserListener(this::sappJarsPathChanged);

    return new InputParameter(SAPP_JARS_PATH_LABEL, this.sappJarsPath, SAPP_JARS_PATH_HELP);
  }

  private void sappJarsPathChanged(ChangeEvent event) {
    File newValue = this.sappJarsPath.getSelectedFile();
    this.firePropertyChange(SAPP_JARS_PATH_PROPERTY, this.selectedSappJarsPath, newValue);
    this.selectedSappJarsPath = newValue;
  }

  public Optional<File> selectedSappJarsPath() {
    return Optional.ofNullable(this.selectedSappJarsPath);
  }

  public SappCommands sappCommands() {
    if (this.selectedJavaPath().isPresent()) {
      return new DefaultSappCommands(this.selectedJavaPath.getAbsolutePath(), conversionJarPath(), geneCallerJarPath());
    } else {
      return new DefaultSappCommands(conversionJarPath(), geneCallerJarPath());
    }
  }

  public String conversionJarPath() {
    return this.selectedSappJarsPath().map(File::getAbsolutePath).orElse("")
      + "/" + SappEnvironment.getInstance().getConversionJar();
  }

  public String geneCallerJarPath() {
    return this.selectedSappJarsPath().map(File::getAbsolutePath).orElse("")
      + "/" + SappEnvironment.getInstance().getGeneCallerJar();
  }

  public void setControlsEnabled(boolean enabled) {
    this.javaPath.getBrowseAction().setEnabled(enabled);
    this.sappJarsPath.getBrowseAction().setEnabled(enabled);
  }
}
