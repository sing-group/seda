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
package org.sing_group.seda.gui.split.regex;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createTitledBorder;
import static org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder.createSaveJFileChooserPanel;
import static org.sing_group.seda.plugin.core.info.plugin.RegexSplitSedaPluginInfo.PARAM_FILES_DIRECTORY_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RegexSplitSedaPluginInfo.PARAM_GROUP_NAMES_FILES_DIRECTORY_HELP_GUI;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.filtering.header.RegexHeaderMatcherConfigurationPanel;

public class RegexSplitConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String DESCRIPTION_HEADER_MATCHER =
    "This option allows to specify how sequences must be "
      + "grouped to form the new files.";

  private static final String DESCRIPTION_GROUP_NAMES_FILES_DIRECTORY = PARAM_GROUP_NAMES_FILES_DIRECTORY_HELP_GUI;

  private RegexHeaderMatcherConfigurationPanel headerMatcherPanel;
  private RegexSplitConfigurationTransformationProvider transformationProvider;
  private JFileChooserPanel saveGroupNamesDirectory;

  public RegexSplitConfigurationPanel() {
    this.transformationProvider = new RegexSplitConfigurationTransformationProvider();
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(getParametersPanel(), CENTER);

    return new CenteredJPanel(mainPanel);
  }

  private JPanel getParametersPanel() {
    return new InputParametersPanel(getInputParameters());
  }

  private InputParameter[] getInputParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getHeaderMatcherParameter());
    parameters.add(getSaveGroupNamesDirectoryParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getHeaderMatcherParameter() {
    this.headerMatcherPanel = new RegexHeaderMatcherConfigurationPanel();
    this.headerMatcherPanel.setBorder(createTitledBorder("Header matcher configuration"));
    this.headerMatcherPanel.addPropertyChangeListener(this::headerMatcherChanged);

    return new InputParameter("", headerMatcherPanel, DESCRIPTION_HEADER_MATCHER);
  }

  private void headerMatcherChanged(PropertyChangeEvent event) {
    if (RegexHeaderMatcherConfigurationPanel.PROPERTIES.contains(event.getPropertyName())) {
      Optional<RegexHeaderMatcher> headerMatcher = this.headerMatcherPanel.getRegexHeaderMatcher();
      if (headerMatcher.isPresent()) {
        this.transformationProvider.setHeaderMatcher(headerMatcher.get());
      } else {
        this.transformationProvider.removeHeaderMatcher();
      }
    }
  }

  private InputParameter getSaveGroupNamesDirectoryParameter() {
    this.saveGroupNamesDirectory =
      createSaveJFileChooserPanel()
        .withFileChooserSelectionMode(SelectionMode.DIRECTORIES)
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser()).withLabel("").build();
    this.saveGroupNamesDirectory.addFileChooserListener(this::saveGroupNamesDirectoryChanged);

    return new InputParameter(
      PARAM_FILES_DIRECTORY_DESCRIPTION + ": ", this.saveGroupNamesDirectory,
      DESCRIPTION_GROUP_NAMES_FILES_DIRECTORY
    );
  }

  private void saveGroupNamesDirectoryChanged(ChangeEvent event) {
    File selectedFile = this.saveGroupNamesDirectory.getSelectedFile();
    if (selectedFile != null) {
      this.transformationProvider.setSaveGroupNamesDirectory(selectedFile);
    } else {
      this.transformationProvider.removeSaveGroupNamesDirectory();
    }
  }

  public RegexSplitConfigurationTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(RegexSplitConfigurationTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    if (this.transformationProvider.getRegexHeaderMatcher() != null) {
      this.headerMatcherPanel.setRegexHeaderMatcher(this.transformationProvider.getRegexHeaderMatcher());
    }

    if (this.transformationProvider.getSaveGroupNamesDirectory() != null) {
      this.saveGroupNamesDirectory.setSelectedFile(this.transformationProvider.getSaveGroupNamesDirectory());
    } else {
      this.saveGroupNamesDirectory.clearSelectedFile();
    }
  }
}
