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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;

public class DockerSappCommandsConfigurationPanel extends AbstractSappCommandsConfigurationPanel {
  private static final long serialVersionUID = 1L;

  private InputParametersPanel parametersPanel;
  private JTextField javaPath;
  protected JTextField sappJarsPath;

  private String selectedJavaPath;
  private String selectedSappJarsPath;

  public DockerSappCommandsConfigurationPanel() {
    this("", "");
  }

  public DockerSappCommandsConfigurationPanel(String javaPath, String sappJarsPath) {
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
    this.javaPath = new JTextField(30);

    if (!javaPath.isEmpty()) {
      this.javaPath.setText(javaPath);
    }

    this.javaPath.getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        javaPathChanged();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        javaPathChanged();
      }
    });

    return new InputParameter(JAVA_PATH_LABEL, this.javaPath, JAVA_PATH_HELP);
  }

  private void javaPathChanged() {
    String newValue = this.javaPath.getText();
    this.firePropertyChange(JAVA_PATH_PROPERTY, this.javaPath, newValue);
    this.selectedJavaPath = newValue;
  }

  protected Optional<String> selectedJavaPath() {
    if (this.selectedJavaPath == null || this.selectedJavaPath.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(this.selectedJavaPath);
    }
  }

  protected InputParameter getSappJarsPathParameter(String sappJarsPath) {
    this.sappJarsPath = new JTextField(30);

    if (!sappJarsPath.isEmpty()) {
      this.sappJarsPath.setText(sappJarsPath);
    }

    this.sappJarsPath.getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        sappJarsPathChanged();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        sappJarsPathChanged();
      }
    });

    return new InputParameter(SAPP_JARS_PATH_LABEL, this.sappJarsPath, SAPP_JARS_PATH_HELP);
  }

  private void sappJarsPathChanged() {
    String newValue = this.sappJarsPath.getText();
    this.firePropertyChange(SAPP_JARS_PATH_PROPERTY, this.selectedSappJarsPath, newValue);
    this.selectedSappJarsPath = newValue;
  }

  protected Optional<String> selectedSappJarsPath() {
    if (this.selectedSappJarsPath == null || this.selectedSappJarsPath.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(this.selectedSappJarsPath);
    }
  }

  public void setControlsEnabled(boolean enabled) {
    this.javaPath.setEnabled(enabled);
    this.javaPath.setEditable(enabled);
    this.sappJarsPath.setEnabled(enabled);
    this.sappJarsPath.setEditable(enabled);
  }

  public void setSappJarsPath(String path) {
    this.sappJarsPath.setText(path);
  }
}
