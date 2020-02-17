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
package org.sing_group.seda.gui.isoforms;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createTitledBorder;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector;
import org.sing_group.seda.core.rename.EmptySequenceHeadersJoiner;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.filtering.header.RegexHeaderMatcherConfigurationPanel;

public class RemoveIsoformsConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String DESCRIPTION_MINIMUM_WORD_LENGTH = "The minimum length of word to consider that two "
      + "sequences are isoforms.";
  private static final String DESCRIPTION_ISOFORM_FILES_DIRECTORY = "<html>Whether the removed isoform names should "
      + "be saved into a CSV file or not. <br/>This allows an easy identification of those sequences that "
      + "had isoforms in the output files.<br/>If you do not want to save them, leave this file empty. Otherwise, "
      + "choose the directory where such files should be created.</html>";
  private static final String DESCRIPTION_ISOFORM_SELECTION_CRITERIA = "The configuration of the criteria to select "
      + "which isoform should go to the output file.";
  private static final String DESCRIPTION_HEADER_MATCHER = "<html>This option allows to specify whether sequences must "
      + "be grouped before the identification of the isoforms. <br/>Leave it empty if isoforms must be removed at a "
      + "file level.<br/>In contrast, if you want to make groups of sequences before the identification of the "
      + "isoforms, here it is possible to configure <br/>how sequence headers must be matched in order to group sequences. "
      + "Check the manual for examples.</html>";
  private static final String DESCRIPTION_ADD_REMOVED_ISOFORMS =
    "This group of options allows to specify how removed isoforms should be processed.";

  private JIntegerTextField minimumWordLenthTf;
  private JFileChooserPanel removedIsoformsFilesDirectory;
  private DefaultSequenceIsoformConfigurationPanel isoformSelectorPanel;
  private RegexHeaderMatcherConfigurationPanel headerMatcherPanel;
  private RemovedIsoformHeadersConfigurationPanel addRemovedIsoformHeadersPanel;
  private RemoveIsoformsTransformationProvider transformationProvider;

  public RemoveIsoformsConfigurationPanel() {
    this.transformationProvider = new RemoveIsoformsTransformationProvider();
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
    parameters.add(getMinimumWordLengthParameter());
    parameters.add(getIsoformSelectorParameter());
    parameters.add(getHeaderMatcherParameter());
    parameters.add(getAddRemovedIsoformHeadersParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getMinimumWordLengthParameter() {
    this.minimumWordLenthTf = new JIntegerTextField(250);
    this.minimumWordLenthTf.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        minimumWordChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        minimumWordChanged();
      }
    });
    
    JPanel container = new JPanel();
    container.setLayout((new BoxLayout(container, BoxLayout.X_AXIS)));
    container.add(new JLabel("Minimum word length: "));
    container.add(this.minimumWordLenthTf);

    return new InputParameter("", container, DESCRIPTION_MINIMUM_WORD_LENGTH);
  }

  private void minimumWordChanged() {
    this.transformationProvider.setMinimumWordLength(this.minimumWordLenthTf.getValue());
  }

  private InputParameter getRemovedIsoformFilesDirectoryParameter() {
    this.removedIsoformsFilesDirectory = JFileChooserPanelBuilder.createSaveJFileChooserPanel()
        .withFileChooserSelectionMode(SelectionMode.DIRECTORIES)
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser()).withLabel("").build();
    this.removedIsoformsFilesDirectory.addFileChooserListener(this::addRemovedIsoformNamesChanged);

    return new InputParameter("Isoform files directory: ", this.removedIsoformsFilesDirectory,
        DESCRIPTION_ISOFORM_FILES_DIRECTORY);
  }

  private void addRemovedIsoformNamesChanged(ChangeEvent event) {
    File selectedFile = this.removedIsoformsFilesDirectory.getSelectedFile();
    if (selectedFile != null) {
      this.transformationProvider.setRemovedIsoformFilesDirectory(selectedFile);
    } else {
      this.transformationProvider.clearRemovedIsoformFilesDirectory();
    }
  }

  private InputParameter getIsoformSelectorParameter() {
    this.isoformSelectorPanel = new DefaultSequenceIsoformConfigurationPanel();
    this.isoformSelectorPanel.setBorder(createTitledBorder("Isoform selection criteria"));
    this.transformationProvider.setIsoformSelector(this.isoformSelectorPanel.getSelector());
    this.isoformSelectorPanel.addPropertyChangeListener(this::isoformSelectorChanged);

    return new InputParameter("", isoformSelectorPanel, DESCRIPTION_ISOFORM_SELECTION_CRITERIA);
  }

  private void isoformSelectorChanged(PropertyChangeEvent event) {
    if (DefaultSequenceIsoformConfigurationPanel.PROPERTIES.contains(event.getPropertyName())) {
      this.transformationProvider.setIsoformSelector(this.isoformSelectorPanel.getSelector()); 
    }
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
  
  private InputParameter getAddRemovedIsoformHeadersParameter() {
    this.addRemovedIsoformHeadersPanel = new RemovedIsoformHeadersConfigurationPanel(getRemovedIsoformFilesDirectoryParameter());
    this.addRemovedIsoformHeadersPanel.setBorder(createTitledBorder("Removed isoforms"));
    this.addRemovedIsoformHeadersPanel.addPropertyChangeListener(this::addRemovedIsoformHeadersChanged);
    
    return new InputParameter("", addRemovedIsoformHeadersPanel, DESCRIPTION_ADD_REMOVED_ISOFORMS);
  }
  
  private void addRemovedIsoformHeadersChanged(PropertyChangeEvent event) {
    if (RemovedIsoformHeadersConfigurationPanel.PROPERTIES.contains(event.getPropertyName())) {
      this.transformationProvider.setSequenceHeaderJoiner(this.addRemovedIsoformHeadersPanel.getSequenceHeadersJoiner());
    }
  }

  public RemoveIsoformsTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(RemoveIsoformsTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    this.minimumWordLenthTf.setValue(transformationProvider.getMinimumWordLengh());

    if (transformationProvider.getSelector() instanceof DefaultSequenceIsoformSelector) {
      this.isoformSelectorPanel.setSelector((DefaultSequenceIsoformSelector) transformationProvider.getSelector());
    } else {
      throw new IllegalArgumentException(
        "Trying to set a SequenceIsoformSelector different DefaultSequenceIsoformSelector"
      );
    }

    if (transformationProvider.getRegexHeaderMatcher() != null) {
      this.headerMatcherPanel.setRegexHeaderMatcher(transformationProvider.getRegexHeaderMatcher());
    } else {
      this.headerMatcherPanel.clearRegexHeaderMatcher();
    }

    if (transformationProvider.getRemovedIsoformsFilesDirectory() != null) {
      this.removedIsoformsFilesDirectory.setSelectedFile(transformationProvider.getRemovedIsoformsFilesDirectory());
    } else {
      this.removedIsoformsFilesDirectory.clearSelectedFile();
    }

    if (transformationProvider.getSequenceHeadersJoiner() != null) {
      this.addRemovedIsoformHeadersPanel.setSequenceHeadersJoiner(transformationProvider.getSequenceHeadersJoiner());
    } else {
      this.addRemovedIsoformHeadersPanel.setSequenceHeadersJoiner(new EmptySequenceHeadersJoiner());
    }
  }
}
