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
package org.sing_group.seda.gui.configuration_panel.ncbi;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.Y_AXIS;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.io.File;

import javax.swing.*;

import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.transformation.provider.ncbi.NcbiRenameTransformationProvider;

public class NcbiRenameConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private FileRenameConfigurationPanel fileRenamePanel;
  private SequenceHeaderRenameConfigurationPanel sequenceHeaderRenamePanel;
  private ReplaceOptionsConfigurationPanel replaceOptionsConfigurationPanel;
  private NcbiTaxonomyConfigurationPanel ncbiTaxonomyConfigurationPanel;
  private NcbiRenameTransformationProvider transformationProvider;

  public NcbiRenameConfigurationPanel() {
    this.init();
    this.transformationProvider =
      new NcbiRenameTransformationProvider(
        this.fileRenamePanel.getPosition(), this.fileRenamePanel.getDelimiter(),
        this.sequenceHeaderRenamePanel.getPosition(), this.sequenceHeaderRenamePanel.getDelimiter(),
        this.sequenceHeaderRenamePanel.isAddIndex(), this.sequenceHeaderRenamePanel.getIndexDelimiter(),
        this.replaceOptionsConfigurationPanel.isReplaceBlankSpaces(),
        this.replaceOptionsConfigurationPanel.isReplaceSpecialCharacters(),
        this.replaceOptionsConfigurationPanel.getReplacementString(),
        this.replaceOptionsConfigurationPanel.isSaveReplacementsMap(),
        this.replaceOptionsConfigurationPanel.getReplacementsMapFile(),
        this.ncbiTaxonomyConfigurationPanel.getDelimiter(), this.ncbiTaxonomyConfigurationPanel.getFields()
      );
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, Y_AXIS));

    mainPanel.add(getFileRenameConfigurationPanel());
    mainPanel.add(getSequenceHeaderRenameConfigurationPanel());
    mainPanel.add(getReplaceOptionsConfigurationPanel());
    mainPanel.add(getNcbiTaxonomyConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private Component getFileRenameConfigurationPanel() {
    this.fileRenamePanel = new FileRenameConfigurationPanel();
    this.fileRenamePanel.addPropertyChangeListener(this::fileRenamePropertyChanged);
    addTitledBorder(this.fileRenamePanel, "File name");

    return this.fileRenamePanel;
  }

  private void fileRenamePropertyChanged(PropertyChangeEvent event) {
    switch (event.getPropertyName()) {
      case FileRenameConfigurationPanel.PROPERTY_POSITION:
        this.transformationProvider.setFilePosition(this.fileRenamePanel.getPosition());
        break;
      case FileRenameConfigurationPanel.PROPERTY_DELIMITER:
        this.transformationProvider.setFileDelimiter(this.fileRenamePanel.getDelimiter());
        break;
    }
  }

  private Component getSequenceHeaderRenameConfigurationPanel() {
    this.sequenceHeaderRenamePanel = new SequenceHeaderRenameConfigurationPanel();
    addTitledBorder(this.sequenceHeaderRenamePanel, "Sequence headers");
    this.sequenceHeaderRenamePanel.addPropertyChangeListener(this::sequenceHeaderRenamePropertyChanged);

    return this.sequenceHeaderRenamePanel;
  }

  private void sequenceHeaderRenamePropertyChanged(PropertyChangeEvent event) {
    switch (event.getPropertyName()) {
      case SequenceHeaderRenameConfigurationPanel.PROPERTY_POSITION:
        this.transformationProvider.setSequencePosition(this.sequenceHeaderRenamePanel.getPosition());
        break;
      case SequenceHeaderRenameConfigurationPanel.PROPERTY_DELIMITER:
        this.transformationProvider.setSequenceDelimiter(this.sequenceHeaderRenamePanel.getDelimiter());
        break;
      case SequenceHeaderRenameConfigurationPanel.PROPERTY_ADD_INDEX:
        this.transformationProvider.setSequenceAddIndex(this.sequenceHeaderRenamePanel.isAddIndex());
        break;
      case SequenceHeaderRenameConfigurationPanel.PROPERTY_INDEX_DELIMITER:
        this.transformationProvider.setSequenceIndexDelimiter(this.sequenceHeaderRenamePanel.getIndexDelimiter());
        break;
    }
  }

  private Component getReplaceOptionsConfigurationPanel() {
    this.replaceOptionsConfigurationPanel = new ReplaceOptionsConfigurationPanel();
    addTitledBorder(this.replaceOptionsConfigurationPanel, "Configuration");
    this.replaceOptionsConfigurationPanel.addPropertyChangeListener(this::replaceOptionsConfigurationPropertyChanged);

    return this.replaceOptionsConfigurationPanel;
  }

  private void replaceOptionsConfigurationPropertyChanged(PropertyChangeEvent event) {
    switch (event.getPropertyName()) {
      case ReplaceOptionsConfigurationPanel.PROPERTY_REPLACE_BLANK_SPACES:
        this.transformationProvider.setReplaceBlankSpaces(this.replaceOptionsConfigurationPanel.isReplaceBlankSpaces());
        break;
      case ReplaceOptionsConfigurationPanel.PROPERTY_REPLACE_SPECIAL_CHARACTERS:
        this.transformationProvider
          .setReplaceSpecialCharacters(this.replaceOptionsConfigurationPanel.isReplaceSpecialCharacters());
        break;
      case ReplaceOptionsConfigurationPanel.PROPERTY_REPLACEMENT_STRING:
        this.transformationProvider.setReplacementString(this.replaceOptionsConfigurationPanel.getReplacementString());
        break;
      case ReplaceOptionsConfigurationPanel.PROPERTY_REPLACEMENTS_MAP_FILE:
        File file = this.replaceOptionsConfigurationPanel.getReplacementsMapFile();
        if (file == null) {
          this.transformationProvider.clearReplacementsMapFile();
        } else {
          this.transformationProvider.setReplacementsMapFile(file);
        }
        break;
      case ReplaceOptionsConfigurationPanel.PROPERTY_SAVE_REPLACEMENTS_MAP:
        this.transformationProvider
          .setSaveReplacementsMap(this.replaceOptionsConfigurationPanel.isSaveReplacementsMap());
        break;
    }
  }

  private Component getNcbiTaxonomyConfigurationPanel() {
    this.ncbiTaxonomyConfigurationPanel = new NcbiTaxonomyConfigurationPanel();
    addTitledBorder(this.ncbiTaxonomyConfigurationPanel, "NCBI Taxonomy information");
    this.ncbiTaxonomyConfigurationPanel.addPropertyChangeListener(this::ncbiTaxonomyConfigurationPropertyChanged);

    return this.ncbiTaxonomyConfigurationPanel;
  }

  private void ncbiTaxonomyConfigurationPropertyChanged(PropertyChangeEvent event) {
    switch (event.getPropertyName()) {
      case NcbiTaxonomyConfigurationPanel.PROPERTY_DELIMITER:
        this.transformationProvider.setNcbiTaxonomyDelimiter(this.ncbiTaxonomyConfigurationPanel.getDelimiter());
        break;
      case NcbiTaxonomyConfigurationPanel.PROPERTY_FIELDS:
        this.transformationProvider.setNcbiTaxonomyFields(this.ncbiTaxonomyConfigurationPanel.getFields());
        break;
    }
  }

  private static void addTitledBorder(JComponent component, String title) {
    component.setBorder(createTitledBorder(title));
  }

  public NcbiRenameTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(NcbiRenameTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    this.fileRenamePanel.setPosition(this.transformationProvider.getFilePosition());
    this.fileRenamePanel.setDelimiter(this.transformationProvider.getFileDelimiter());

    this.sequenceHeaderRenamePanel.setPosition(this.transformationProvider.getSequencePosition());
    this.sequenceHeaderRenamePanel.setDelimiter(this.transformationProvider.getSequenceDelimiter());
    this.sequenceHeaderRenamePanel.setAddIndex(this.transformationProvider.isSequenceAddIndex());
    this.sequenceHeaderRenamePanel.setIndexDelimiter(this.transformationProvider.getSequenceIndexDelimiter());

    this.replaceOptionsConfigurationPanel.setReplaceBlankSpaces(this.transformationProvider.isReplaceBlankSpaces());
    this.replaceOptionsConfigurationPanel
      .setReplaceSpecialCharacters(this.transformationProvider.isReplaceSpecialCharacters());
    this.replaceOptionsConfigurationPanel.setReplacementString(this.transformationProvider.getReplacementString());
    this.replaceOptionsConfigurationPanel.setSaveReplacementsMap(this.transformationProvider.isSaveReplacementsMap());
    if (
      this.transformationProvider.isSaveReplacementsMap()
        && this.transformationProvider.getReplacementsMapFile() != null
    ) {
      this.replaceOptionsConfigurationPanel
        .setReplacementsMapFile(this.transformationProvider.getReplacementsMapFile());
    }

    this.ncbiTaxonomyConfigurationPanel.setDelimiter(this.transformationProvider.getNcbiTaxonomyDelimiter());
    this.ncbiTaxonomyConfigurationPanel.setFields(this.transformationProvider.getNcbiTaxonomyFields());
  }
}
